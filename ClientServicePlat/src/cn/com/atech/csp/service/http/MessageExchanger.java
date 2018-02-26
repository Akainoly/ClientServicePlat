package cn.com.atech.csp.service.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.atech.csp.tools.ReadPropTool;

public class MessageExchanger implements IPipeLineWorker {
	private DataChannel dc;
	private ByteBuffer requestByteBuffer = null; // ���HTTP����Ļ�����

	private boolean requestReceived = false; // �Ƿ��Ѿ����յ������е�HTTP����
	private Request request = null; // ��ʾHTTP����
	private Response response = null; // ��ʾHTTP��Ӧ
	private static Charset requestCharset = null;

	public MessageExchanger(DataChannel dc) {
		String http_service_charset = (String) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_charset",
				"GBK");
		try {
			requestCharset = Charset.forName(http_service_charset);
		} catch (IllegalArgumentException e) {
			System.out.println("�ַ��������ô���" + http_service_charset + " ����ʹ��Ĭ���ַ����룺" + DEFAULT_CHARSET);
			requestCharset = Charset.forName(DEFAULT_CHARSET);
			e.printStackTrace();
		}
		this.dc = dc;
	}

	/*
	 * ����HTTP��������Ѿ����յ������е�HTTP�������ݣ��ͷ���true,���򷵻�false
	 */
	private boolean receive(SelectionKey sk) throws IOException {
		if (requestReceived)
			return true; // ����Ѿ����յ�����HTTP�������ݣ�����true
		// ����Ѿ�����ͨ����ĩβ�������Ѿ�����HTTP�������ݵ�ĩβ��־��\r\n�����ͷ���true
		if ((dc.read() < 0) || isComplete(dc.getReadBuf())) {
			requestByteBuffer = dc.getReadBuf();
			return (requestReceived = true);
		}
		return false;
	}
	
	/*
	 * �ж�ByteBuffer�Ƿ������HTTP������������ݡ� HTTP�����ԡ�\r\n\r\n����β��
	 */
	public static boolean isComplete(ByteBuffer bb) {
		ByteBuffer temp = bb.asReadOnlyBuffer();
		temp.flip();
		String data = requestCharset.decode(temp).toString();
		if (data.indexOf("\r\n\r\n") != -1) {
			return true;
		}
		return false;
	}
	
	/*
	 * �趨���ڽ���HTTP������ַ���ƥ��ģʽ������������ʽ��HTTP����
	 *
	 * GET /dir/file HTTP/1.1 Host: hostname
	 *
	 * ����������:
	 *
	 * group[1] = "GET" group[2] = "/dir/file" group[3] = "1.1" group[4] =
	 * "hostname"
	 */
	private static Pattern requestPattern = Pattern.compile(
			"\\A([A-Z]+) +([^ ]+) +HTTP/([0-9\\.]+)$" + ".*^Host: ([^ ]+)$.*\r\n\r\n\\z",
			Pattern.MULTILINE | Pattern.DOTALL);

	/*
	 * ͨ��Request���parse()����������requestByteBuffer�е�HTTP�������ݣ�������Ӧ��Request����
	 */
	private boolean parse() throws IOException {
		try {
			CharBuffer cb = requestCharset.decode(requestByteBuffer); // ����
			Matcher m = requestPattern.matcher(cb); // �����ַ���ƥ��
			// ���HTTP������ָ�����ַ���ģʽ��ƥ�䣬˵���������ݲ���ȷ
			if (!m.matches())
				throw new IOException();
			// ����һ��Request���󣬲����䷵��
			String uri = "http://" + m.group(4) + m.group(2);
			request = new Request(m.group(1), m.group(3), uri,requestCharset);
			return true;
		} catch (IOException x) {
			// ���HTTP����ĸ�ʽ����ȷ���ͷ��ʹ�����Ϣ
			response = new Response(ResponseCode.BAD_REQUEST, new StringContent(x));
		}
		return false;
	}
	
	/*
	 * ɾ���������ģ������ӽ�֧��GET��HEAD����ʽ������HTTP�����е����Ĳ���
	 */
	private static ByteBuffer deleteContent(ByteBuffer bb) {
		ByteBuffer temp = bb.asReadOnlyBuffer();
		String data = requestCharset.decode(temp).toString();
		if (data.indexOf("\r\n\r\n") != -1) {
			data = data.substring(0, data.indexOf("\r\n\r\n") + 4);
			return requestCharset.encode(data);
		}
		return bb;
	}

	/*
	 * ����HTTP��Ӧ
	 */
	private void build() throws IOException {
		response = new Response(ResponseCode.OK, new FileContent(request.getUri()), request.getMethod());
	}

	/* ����HTTP���󣬷���HTTP��Ӧ */
	public void work(SelectionKey sk) throws IOException {
		try {
			if (request == null) { // �����û�н��յ�HTTP�������������
				// ����HTTP����
				if (!receive(sk))
					return;
				requestByteBuffer.flip();

				// ����ɹ�������HTTP���󣬾ʹ���һ��Response����
				if (parse())
					build();

				try {
					response.prepare(); // ׼��HTTP��Ӧ������
				} catch (IOException x) {
					response.release();
					response = new Response(ResponseCode.NOT_FOUND, new StringContent(x));
					response.prepare();
				}

				if (send()) {
					// ���HTTP��Ӧû�з�����ϣ�����Ҫע��д�����¼���
					// �Ա���д�����¼�����ʱ������������
					sk.interestOps(SelectionKey.OP_WRITE);
				} else {
					// ���HTTP��Ӧ������ϣ��ͶϿ��ײ�����ӣ������ͷ�Responseռ�õ���Դ
					dc.close();
					response.release();
				}
			} else { // ����Ѿ����յ�HTTP�������������
				if (!send()) { // ���HTTP��Ӧ�������
					dc.close();
					response.release();
				}
			}
		} catch (IOException x) {
			x.printStackTrace();
			dc.close();
			if (response != null) {
				response.release();
			}
		}
	}

	/* ����HTTP��Ӧ�����ȫ��������ϣ��ͷ���false�����򷵻�true */
	private boolean send() throws IOException {
		return response.send(dc);
	}
}

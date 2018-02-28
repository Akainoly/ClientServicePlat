package cn.com.atech.csp.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.atech.csp.invoke.AbstractMessageTranslator;
import cn.com.atech.csp.invoke.JsonTranslator;
import cn.com.atech.csp.invoke.XmlTranslator;
import cn.com.atech.csp.service.http.HttpRequest;
import cn.com.atech.csp.service.http.HttpResponse;
import cn.com.atech.csp.service.http.ResponseCode;
import cn.com.atech.csp.service.http.StringContent;
import cn.com.atech.csp.service.tcp.TcpRequest;

public class MessageExchanger implements IPipeLineWorker {
	
	private Charset charset = null;
	
	private ByteBuffer requestByteBuffer = null; // ���HTTP����Ļ�����
	
	private DataChannel dc;
	
	private AbstractRequest request=null;
	
	private AbstractResponse response=null;
	
	final Logger logger = LoggerFactory.getLogger(MessageExchanger.class);
	
	public MessageExchanger(DataChannel dc, Charset charset) {
		this.dc = dc;
		this.charset=charset;
	}
	
	private boolean requestReceived = false; // �Ƿ��Ѿ����յ������е�HTTP����
	
	/* ����HTTP���󣬷���HTTP��Ӧ */
	public void work(SelectionKey sk) throws IOException {
		try {
			if (request == null) { // �����û�н��յ�HTTP�������������
				// ����HTTP����
				if (!readData(sk))
					return;
				requestByteBuffer.flip();

				// ����ɹ�������HTTP���󣬾ʹ���һ��Response����
				request=parseRequset();
				
				if(request!=null) {
					logger.info("�������ݣ�"+request.getMessage());
					AbstractMessageTranslator msgTranslator=null;
					if(request.getMessageType().equals(MESSAGE_TYPE_XML)) {
						msgTranslator=new XmlTranslator();
					}else if(request.getMessageType().equals(MESSAGE_TYPE_JSON)) {
						msgTranslator=new JsonTranslator();
					}
					String respData=msgTranslator.translate(request);
					response = new HttpResponse(ResponseCode.OK, new StringContent(respData));
				}

				try {
					response.prepare(); // ׼��HTTP��Ӧ������
				} catch (IOException x) {
					response.release();
					response = new HttpResponse(ResponseCode.NOT_FOUND, new StringContent(x));
					response.prepare();
				}

				if (response()) {
					// ���HTTP��Ӧû�з�����ϣ�����Ҫע��д�����¼���
					// �Ա���д�����¼�����ʱ������������
					sk.interestOps(SelectionKey.OP_WRITE);
				} else {
					// ���HTTP��Ӧ������ϣ��ͶϿ��ײ�����ӣ������ͷ�Responseռ�õ���Դ
					dc.close();
					response.release();
				}
			} else { // ����Ѿ����յ�HTTP�������������
				if (!response()) { // ���HTTP��Ӧ�������
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
	
	/*
	 * ����HTTP��������Ѿ����յ������е�HTTP�������ݣ��ͷ���true,���򷵻�false
	 */
	private boolean readData(SelectionKey sk) throws IOException {
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
	private boolean isComplete(ByteBuffer bb) {
		ByteBuffer temp = bb.asReadOnlyBuffer();
		temp.flip();
		String data = charset.decode(temp).toString();
		if (data.indexOf("\r\n\r\n") != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * HttpЭ��ͷƥ��ģʽ
	 * GET /dir/file HTTP/1.1 Host: hostname
	 * �������
	 * group[1] = "GET" 
	 * group[2] = "/dir/file" 
	 * group[3] = "1.1" 
	 * group[4] ="hostname"
	 * 
	 */
	private static Pattern httpHeadPattern = Pattern.compile(
			"\\A([A-Z]+) +([^ ]+) +HTTP/([0-9\\.]+)$" + ".*^Host: ([^ ]+)$.*\r\n\r\n\\z",
			Pattern.MULTILINE | Pattern.DOTALL);
	
	public AbstractRequest parseRequset() {
		AbstractRequest temp_requset=null;
		try {
			ByteBuffer temp = requestByteBuffer.asReadOnlyBuffer();
			String data = charset.decode(temp).toString();
			String content=null;
			String head=null;
			if (data.indexOf("\r\n\r\n") != -1) {
				content = data.substring(data.indexOf("\r\n\r\n") + 4);
				head= data.substring(0,data.indexOf("\r\n\r\n")+4);
			}else {
				head=data;
			}
			Matcher m = httpHeadPattern.matcher(head); // �����ַ���ƥ��
			// ���HTTP������ָ�����ַ���ģʽ��ƥ�䣬˵���������ݲ���ȷ
			if (m.matches()) {
				// ����һ��Request���󣬲����䷵��
				String uri = "http://" + m.group(4) + m.group(2);
				temp_requset = new HttpRequest(m.group(1), m.group(3), uri ,content);	
				if(!temp_requset.checkValid()) {
					temp_requset = new TcpRequest(data);	
					if(!temp_requset.checkValid()) {
						temp_requset=null;
						throw new IOException("δ֧�ֵ���Ϣ��������");
					}
				}
				temp_requset.setCharset(charset);
			}
		} catch (IOException x) {
			// ���HTTP����ĸ�ʽ����ȷ���ͷ��ʹ�����Ϣ
			response = new HttpResponse(ResponseCode.BAD_REQUEST, new StringContent(x));
		}
		return temp_requset;
	}
	
	/* ����HTTP��Ӧ�����ȫ��������ϣ��ͷ���false�����򷵻�true */
	private boolean response() throws IOException {
		return response.send(dc);
	}

}

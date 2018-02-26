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
	private ByteBuffer requestByteBuffer = null; // 存放HTTP请求的缓冲区

	private boolean requestReceived = false; // 是否已经接收到了所有的HTTP请求
	private Request request = null; // 表示HTTP请求
	private Response response = null; // 表示HTTP响应
	private static Charset requestCharset = null;

	public MessageExchanger(DataChannel dc) {
		String http_service_charset = (String) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_charset",
				"GBK");
		try {
			requestCharset = Charset.forName(http_service_charset);
		} catch (IllegalArgumentException e) {
			System.out.println("字符编码配置错误：" + http_service_charset + " ，将使用默认字符编码：" + DEFAULT_CHARSET);
			requestCharset = Charset.forName(DEFAULT_CHARSET);
			e.printStackTrace();
		}
		this.dc = dc;
	}

	/*
	 * 接收HTTP请求，如果已经接收到了所有的HTTP请求数据，就返回true,否则返回false
	 */
	private boolean receive(SelectionKey sk) throws IOException {
		if (requestReceived)
			return true; // 如果已经接收到所有HTTP请求数据，返回true
		// 如果已经读到通道的末尾，或者已经读到HTTP请求数据的末尾标志“\r\n”，就返回true
		if ((dc.read() < 0) || isComplete(dc.getReadBuf())) {
			requestByteBuffer = dc.getReadBuf();
			return (requestReceived = true);
		}
		return false;
	}
	
	/*
	 * 判断ByteBuffer是否包含了HTTP请求的所有数据。 HTTP请求以“\r\n\r\n”结尾。
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
	 * 设定用于解析HTTP请求的字符串匹配模式。对于以下形式的HTTP请求：
	 *
	 * GET /dir/file HTTP/1.1 Host: hostname
	 *
	 * 将被解析成:
	 *
	 * group[1] = "GET" group[2] = "/dir/file" group[3] = "1.1" group[4] =
	 * "hostname"
	 */
	private static Pattern requestPattern = Pattern.compile(
			"\\A([A-Z]+) +([^ ]+) +HTTP/([0-9\\.]+)$" + ".*^Host: ([^ ]+)$.*\r\n\r\n\\z",
			Pattern.MULTILINE | Pattern.DOTALL);

	/*
	 * 通过Request类的parse()方法，解析requestByteBuffer中的HTTP请求数据，构造相应的Request对象
	 */
	private boolean parse() throws IOException {
		try {
			CharBuffer cb = requestCharset.decode(requestByteBuffer); // 解码
			Matcher m = requestPattern.matcher(cb); // 进行字符串匹配
			// 如果HTTP请求与指定的字符串模式不匹配，说明请求数据不正确
			if (!m.matches())
				throw new IOException();
			// 创建一个Request对象，并将其返回
			String uri = "http://" + m.group(4) + m.group(2);
			request = new Request(m.group(1), m.group(3), uri,requestCharset);
			return true;
		} catch (IOException x) {
			// 如果HTTP请求的格式不正确，就发送错误信息
			response = new Response(ResponseCode.BAD_REQUEST, new StringContent(x));
		}
		return false;
	}
	
	/*
	 * 删除请求正文，本例子仅支持GET和HEAD请求方式，忽略HTTP请求中的正文部分
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
	 * 创建HTTP响应
	 */
	private void build() throws IOException {
		response = new Response(ResponseCode.OK, new FileContent(request.getUri()), request.getMethod());
	}

	/* 接收HTTP请求，发送HTTP响应 */
	public void work(SelectionKey sk) throws IOException {
		try {
			if (request == null) { // 如果还没有接收到HTTP请求的所有数据
				// 接收HTTP请求
				if (!receive(sk))
					return;
				requestByteBuffer.flip();

				// 如果成功解析了HTTP请求，就创建一个Response对象
				if (parse())
					build();

				try {
					response.prepare(); // 准备HTTP响应的内容
				} catch (IOException x) {
					response.release();
					response = new Response(ResponseCode.NOT_FOUND, new StringContent(x));
					response.prepare();
				}

				if (send()) {
					// 如果HTTP响应没有发送完毕，则需要注册写就绪事件，
					// 以便在写就绪事件发生时继续发送数据
					sk.interestOps(SelectionKey.OP_WRITE);
				} else {
					// 如果HTTP响应发送完毕，就断开底层的连接，并且释放Response占用的资源
					dc.close();
					response.release();
				}
			} else { // 如果已经接收到HTTP请求的所有数据
				if (!send()) { // 如果HTTP响应发送完毕
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

	/* 发送HTTP响应，如果全部发送完毕，就返回false，否则返回true */
	private boolean send() throws IOException {
		return response.send(dc);
	}
}

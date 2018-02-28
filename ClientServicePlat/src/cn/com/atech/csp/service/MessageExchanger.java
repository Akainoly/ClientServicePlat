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
	
	private ByteBuffer requestByteBuffer = null; // 存放HTTP请求的缓冲区
	
	private DataChannel dc;
	
	private AbstractRequest request=null;
	
	private AbstractResponse response=null;
	
	final Logger logger = LoggerFactory.getLogger(MessageExchanger.class);
	
	public MessageExchanger(DataChannel dc, Charset charset) {
		this.dc = dc;
		this.charset=charset;
	}
	
	private boolean requestReceived = false; // 是否已经接收到了所有的HTTP请求
	
	/* 接收HTTP请求，发送HTTP响应 */
	public void work(SelectionKey sk) throws IOException {
		try {
			if (request == null) { // 如果还没有接收到HTTP请求的所有数据
				// 接收HTTP请求
				if (!readData(sk))
					return;
				requestByteBuffer.flip();

				// 如果成功解析了HTTP请求，就创建一个Response对象
				request=parseRequset();
				
				if(request!=null) {
					logger.info("请求数据："+request.getMessage());
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
					response.prepare(); // 准备HTTP响应的内容
				} catch (IOException x) {
					response.release();
					response = new HttpResponse(ResponseCode.NOT_FOUND, new StringContent(x));
					response.prepare();
				}

				if (response()) {
					// 如果HTTP响应没有发送完毕，则需要注册写就绪事件，
					// 以便在写就绪事件发生时继续发送数据
					sk.interestOps(SelectionKey.OP_WRITE);
				} else {
					// 如果HTTP响应发送完毕，就断开底层的连接，并且释放Response占用的资源
					dc.close();
					response.release();
				}
			} else { // 如果已经接收到HTTP请求的所有数据
				if (!response()) { // 如果HTTP响应发送完毕
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
	 * 接收HTTP请求，如果已经接收到了所有的HTTP请求数据，就返回true,否则返回false
	 */
	private boolean readData(SelectionKey sk) throws IOException {
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
	 * Http协议头匹配模式
	 * GET /dir/file HTTP/1.1 Host: hostname
	 * 拆解结果：
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
			Matcher m = httpHeadPattern.matcher(head); // 进行字符串匹配
			// 如果HTTP请求与指定的字符串模式不匹配，说明请求数据不正确
			if (m.matches()) {
				// 创建一个Request对象，并将其返回
				String uri = "http://" + m.group(4) + m.group(2);
				temp_requset = new HttpRequest(m.group(1), m.group(3), uri ,content);	
				if(!temp_requset.checkValid()) {
					temp_requset = new TcpRequest(data);	
					if(!temp_requset.checkValid()) {
						temp_requset=null;
						throw new IOException("未支持的消息请求类型");
					}
				}
				temp_requset.setCharset(charset);
			}
		} catch (IOException x) {
			// 如果HTTP请求的格式不正确，就发送错误信息
			response = new HttpResponse(ResponseCode.BAD_REQUEST, new StringContent(x));
		}
		return temp_requset;
	}
	
	/* 发送HTTP响应，如果全部发送完毕，就返回false，否则返回true */
	private boolean response() throws IOException {
		return response.send(dc);
	}

}

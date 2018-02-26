package cn.com.atech.csp.service.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import cn.com.atech.csp.constants.IClientServiceConstants;

public class Request {

	private String method;
	private String version;
	private URI uri;
	
	public Request(String method, String version, String uriStr,Charset charset) throws IOException {
		if (!method.equalsIgnoreCase(IClientServiceConstants.GET) 
				&& !method.equals(IClientServiceConstants.PUT)
				&& !method.equals(IClientServiceConstants.POST)
				&& !method.equals(IClientServiceConstants.HEAD))
			throw new IOException("未支持的请求方法：" + method);
		try {
			uri = new URI(uriStr);
		} catch (URISyntaxException | NullPointerException e) {
			e.printStackTrace();
			throw new IOException("uri地址不合法：" + uriStr);
		}
		this.method = method;
		this.version = version;
	}

	public String getMethod() {
		return method;
	}

	public String getVersion() {
		return version;
	}

	public URI getUri() {
		return uri;
	}

}

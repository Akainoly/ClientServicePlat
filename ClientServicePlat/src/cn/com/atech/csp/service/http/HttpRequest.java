package cn.com.atech.csp.service.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import cn.com.atech.csp.constants.IClientServiceConstants;
import cn.com.atech.csp.service.AbstractRequest;

public class HttpRequest extends AbstractRequest {

	private String method;
	private String version;
	private String uriStr;
	private URI uri;
	
	public HttpRequest(String method, String version, String uriStr,String content) throws IOException {
		this.method = method;
		this.version = version;
		this.message=content;
		this.uriStr=uriStr;
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

	@Override
	public boolean checkValid() {
		if (!method.equalsIgnoreCase(IClientServiceConstants.GET) 
				&& !method.equals(IClientServiceConstants.PUT)
				&& !method.equals(IClientServiceConstants.POST)
				&& !method.equals(IClientServiceConstants.HEAD))
			return false;
		try {
			uri = new URI(uriStr);
		} catch (URISyntaxException | NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		this.setMessageType(MESSAGE_TYPE_JSON);
		return true;
	}

}

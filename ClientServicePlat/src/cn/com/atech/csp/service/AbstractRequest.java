package cn.com.atech.csp.service;

import java.nio.charset.Charset;

public abstract class AbstractRequest {

	public String messageType;
	
	public String message;
	
	public Charset charset;
	
	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public abstract boolean checkValid();
	
}

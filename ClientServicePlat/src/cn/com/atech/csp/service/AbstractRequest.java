package cn.com.atech.csp.service;

import java.nio.charset.Charset;

import cn.com.atech.csp.constants.IClientServiceConstants;

public abstract class AbstractRequest implements IClientServiceConstants {

	public String messageType;
	
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Charset charset;
	
	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public abstract boolean checkValid();
	
}

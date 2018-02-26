package cn.com.atech.csp.service.tcp;

import cn.com.atech.csp.service.AbstractRequest;

public class TcpRequest extends AbstractRequest {
	
	public TcpRequest(String content) {
		this.message=content;
	}

	@Override
	public boolean checkValid() {
		return false;
	}

}

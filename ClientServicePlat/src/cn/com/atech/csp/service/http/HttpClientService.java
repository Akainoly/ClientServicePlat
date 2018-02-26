package cn.com.atech.csp.service.http;

import java.io.IOException;

import cn.com.atech.csp.service.ICommClientService;

public class HttpClientService implements ICommClientService {

	@Override
	public void serviceStart(int port) {
		ICommClientService.super.serviceStart(port);
		new Thread(){
			public void run() {
				HttpServer server;
				try {
					server = new HttpServer(port);
					server.service();
				} catch (IOException e) {
					System.out.println("HTTP服务启动异常...");
				}
			}
		}.start();
	}

}

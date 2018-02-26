package cn.com.atech.csp.service.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class HttpServer {

	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;
	
	public HttpServer(int port) throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("HTTP服务准备完成...");
	}

	public void service() throws IOException {
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new AccessAdapter());
		for (;;) {
			int n = selector.select();
			if (n == 0)
				continue;
			Set readyKeys = selector.selectedKeys();
			Iterator it = readyKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = null;
				try {
					key = (SelectionKey) it.next();
					it.remove();
					final IPipeLineWorker plWorker = (IPipeLineWorker) key.attachment();
					plWorker.work(key);
				} catch (IOException e) {
					e.printStackTrace();
					try {
						if (key != null) {
							key.cancel();
							key.channel().close();
						}
					} catch (Exception ex) {
						e.printStackTrace();
					}
				}
			} 
		} 
	}
	
	public static void main(String args[])throws Exception{
	    final HttpServer server = new HttpServer(80);
	    server.service();
	}
	
}

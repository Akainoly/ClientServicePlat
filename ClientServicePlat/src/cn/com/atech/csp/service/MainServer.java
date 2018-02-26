package cn.com.atech.csp.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import cn.com.atech.csp.constants.IClientServiceConstants;

public class MainServer {

	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;
	private static Charset charset=null;

	public MainServer(int port,String charsetName) throws IOException {
		try {
			charset = Charset.forName(charsetName);
		} catch (IllegalArgumentException e) {
			System.out.println("字符编码配置错误：" + charsetName 
					+ " ，将使用默认字符编码：" + IClientServiceConstants.DEFAULT_CHARSET);
			charset = Charset.forName(IClientServiceConstants.DEFAULT_CHARSET);
			e.printStackTrace();
		}
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("HTTP服务准备完成...");
	}

	public void service() throws IOException {
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new AccessAdapter(charset));
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
//							key.channel().close();
						}
					} catch (Exception ex) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String args[]) throws Exception {
		final MainServer server = new MainServer(80,"GBK");
		server.service();
	}

}

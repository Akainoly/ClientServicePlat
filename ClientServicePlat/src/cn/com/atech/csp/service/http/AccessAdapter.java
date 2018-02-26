package cn.com.atech.csp.service.http;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AccessAdapter implements IPipeLineWorker {

	@Override
	public void work(SelectionKey key) throws IOException {
		 ServerSocketChannel serverSocketChannel=(ServerSocketChannel)key.channel();
		 SocketChannel socketChannel = serverSocketChannel.accept();
		 if (socketChannel== null)return;
		 System.out.println("接收到客户连接，来自:" +
				 socketChannel.socket().getInetAddress() +
				 ":" + socketChannel.socket().getPort());

		 DataChannel dc =new DataChannel(socketChannel, false/*非阻塞模式*/);
		 MessageExchanger mer = new MessageExchanger(dc);
		 socketChannel.register(key.selector(), SelectionKey.OP_READ, mer);
	}

}

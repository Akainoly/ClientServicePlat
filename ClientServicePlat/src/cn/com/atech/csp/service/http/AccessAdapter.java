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
		 System.out.println("���յ��ͻ����ӣ�����:" +
				 socketChannel.socket().getInetAddress() +
				 ":" + socketChannel.socket().getPort());

		 DataChannel dc =new DataChannel(socketChannel, false/*������ģʽ*/);
		 MessageExchanger mer = new MessageExchanger(dc);
		 socketChannel.register(key.selector(), SelectionKey.OP_READ, mer);
	}

}

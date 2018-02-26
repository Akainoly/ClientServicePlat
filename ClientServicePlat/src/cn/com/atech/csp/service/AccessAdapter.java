package cn.com.atech.csp.service;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class AccessAdapter implements IPipeLineWorker {
	
	private static Charset charset=null;
	
	public AccessAdapter(Charset charset) {
		this.charset=charset;
	}

	@Override
	public void work(SelectionKey key) throws IOException {
		 ServerSocketChannel serverSocketChannel=(ServerSocketChannel)key.channel();
		 SocketChannel socketChannel = serverSocketChannel.accept();
		 if (socketChannel== null)return;
		 System.out.println("���յ��ͻ����ӣ�����:" +
				 socketChannel.socket().getInetAddress() +
				 ":" + socketChannel.socket().getPort());

		 DataChannel dc =new DataChannel(socketChannel, false/*������ģʽ*/);
		 MessageExchanger mer = new MessageExchanger(dc,charset);
		 socketChannel.register(key.selector(), SelectionKey.OP_READ, mer);
	}

}

package fileTransTool.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class CheckStatusUtil {
	
	public static void main(String[] args) throws Exception {
		String res=CheckStatusUtil.checkStatus("20.0.0.175", 5566, "001", "9900013");
		System.out.println(res);
	}
	
	/**
	 * 查询渠道柜员对文件传输服务的使用状态
	 * @param server 文件传输服务地址
	 * @param port 端口
	 * @param channelCode 渠道代码
	 * @param tellerNo 柜员号
	 * @return "true"-上传中，"false"-空闲
	 * @throws Exception
	 */
	public static String checkStatus(String server, int port,String channelCode,String tellerNo) throws Exception {
		Socket socket = null;
		OutputStream os = null;
		InputStream sis = null;
		try {
			socket = new Socket(server, port);
			sis=socket.getInputStream();
			os = socket.getOutputStream();
			os.write("AGREE-TECH-CHCK-FILE".getBytes());
			os.flush();
			Document doc=DocumentHelper.createDocument();
			Element root=DocumentHelper.createElement("fileServer");
			doc.setRootElement(root);
			Element ele=root.addElement("file_upload");
			ele.addElement("channelCode").setText(channelCode);
			ele.addElement("tellerNo").setText(tellerNo);
			String xml= doc.asXML();
			os.write(StringUtil.fixFill(xml.getBytes().length+"", "0", 8).getBytes());
			os.write(xml.getBytes());
			os.flush();
			byte[] dataLen=new byte[8];
			sis.read(dataLen);
			int len=Integer.parseInt(new String(dataLen));
			byte[] data=new byte[len];
			sis.read(data);
			doc=DocumentHelper.parseText(new String(data));
			return doc.getRootElement().element("file_upload").elementText("status");
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (os != null)
					os.close();
				if (sis != null)
					sis.close();
				if (socket != null && !socket.isClosed())
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

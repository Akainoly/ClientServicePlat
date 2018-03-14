package fileTransTool.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fileTransTool.base.CommKeys;

public class SendFileThread extends Thread {
	
	Logger logger=LoggerFactory.getLogger(SendFileThread.class);

	Socket socket = null;
	String targeFile = "";
	File local_file=null;
	Map dataMap=null;

	public SendFileThread(Socket socket, String targeFile,File local_file,Map dataMap) {
		this.socket = socket;
		this.targeFile = targeFile;
		this.local_file = local_file;
		this.dataMap=dataMap;
	}

	public void run() {
		InputStream iis = null;
		OutputStream os = null;
		InputStream sis = null;
		try {
			sis=socket.getInputStream();
			os = socket.getOutputStream();
			os.write(FileTransfer.SEND_HEADER);
			os.flush();
			FileTransfer.writeCommData(os, targeFile);
			String upload_rateStr=FileTransfer.readCommData(sis);
			upload_rateStr=upload_rateStr.split("\\|")[1].trim();
			if("fileCreateException".equals(upload_rateStr)){
				throw new IOException("文件服务器建立目标文件异常，请检查文件上传路径:"+targeFile);
			}
			int upload_rate=Integer.parseInt(upload_rateStr);
			if(upload_rate<=0){
				upload_rate=1;
			}

			FileTransfer.writeCommData(os, CommKeys.FILE_UPLOAD_CHANNEL+"|"+(String)dataMap.get(CommKeys.FILE_UPLOAD_CHANNEL));
			FileTransfer.writeCommData(os, CommKeys.FILE_UPLOAD_TELLER+"|"+(String)dataMap.get(CommKeys.FILE_UPLOAD_TELLER));
			iis = new FileInputStream(local_file);
			long file_size = local_file.length();
			byte[] buf = new byte[upload_rate*1024];
			int rl = iis.read(buf);
			logger.debug("开始上传,本地文件大小:"+file_size);
			while (rl > 0) {
				os.write(buf, 0, rl);
				os.flush();
				rl = iis.read(buf);
				if(upload_rate!=1){
					Thread.sleep(1000);
				}
			}
			os.flush();
			logger.debug("文件上传成功!");
		} catch (Exception e) {
			logger.error("异步上传文件异常...", e);
		} finally {
			try {
				if (os != null)
					os.close();
				if (sis != null)
					sis.close();
				if (socket != null && !socket.isClosed())
					socket.close();
				if (iis != null)
					iis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}

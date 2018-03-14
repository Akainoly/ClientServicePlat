package fileTransTool.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fileTransTool.base.CommKeys;
import fileTransTool.util.Md5Util;
import fileTransTool.util.SM3Util;
import fileTransTool.util.StringUtil;

public class FileTransfer {
	
	static Logger logger=LoggerFactory.getLogger(FileTransfer.class);
	
	public static final byte[] SEND_HEADER = "AGREE-TECH-SEND-FILE".getBytes();
	
	public static final byte[] RECV_HEADER = "AGREE-TECH-RECV-FILE".getBytes();
	
	public static final byte[] CHECK_HEADER = "AGREE-TECH-CHCK-FILE".getBytes();
	
	public static final byte[] RECV_FILEFLAG_HEADER = "AGREE-TECH-REFF-FILE".getBytes();
	
	public static void writeCommData(OutputStream os,String data) throws Exception{
		byte[] dataBuf = data.getBytes("GBK");
		int fls = dataBuf.length;
		byte[] datalen = new byte[] { 0, 0, 0, 0 };
		datalen[0] = (byte) ((fls >> 24) & 0xff);
		datalen[1] = (byte) ((fls >> 16) & 0xff);
		datalen[2] = (byte) ((fls >> 8) & 0xff);
		datalen[3] = (byte) ((fls >> 0) & 0xff);
		os.write(datalen);
		os.write(dataBuf);
		os.flush();
	}
	
	public static String readCommData(InputStream in) throws Exception{
		byte[] dataLen = new byte[4];
		int dlsc = in.read(dataLen, 0, 4);
		while (dlsc < 4) {
			dlsc += in.read(dataLen, dlsc, 4 - dlsc);
		}
		int data_length = (dataLen[0] & 0xff) << 24
				| (dataLen[1] & 0xff) << 16 | (dataLen[2] & 0xff) << 8
				| (dataLen[3] & 0xff);

		// 3
		byte[] data = new byte[data_length];
		int rfn_len = in.read(data, 0, data_length);
		while (rfn_len < data_length) {
			rfn_len += in.read(data, rfn_len, data_length - rfn_len);
		}
		String dataStr = new String(data, "GBK");
		return dataStr;
	}
	
	public static String sendFile2Server(File local_file, String targeFile,
			String server, int port, Map dataMap,boolean isSync) throws Exception {
		if(dataMap==null){
			dataMap=new HashMap();
		}
		if(isSync){
			return sendFileSyn(local_file, targeFile, server,dataMap, port);
		}else{
			return sendFileAsyn(local_file, targeFile, server,dataMap, port);
		}
	}
	
	private static String sendFileSyn(final File local_file, final String targeFile,
			final String server, Map dataMap, final int port) throws Exception{
		Socket socket = null;
		InputStream iis = null;
		OutputStream os = null;
		InputStream sis = null;
		try {
			socket = new Socket(server, port);
			sis=socket.getInputStream();
			os = socket.getOutputStream();
			os.write(SEND_HEADER);
			os.flush();
			writeCommData(os, targeFile);
			String upload_rateStr=readCommData(sis);
			upload_rateStr=upload_rateStr.split("\\|")[1].trim();
			if("fileCreateException".equals(upload_rateStr)){
				throw new IOException("文件服务器建立目标文件异常，请检查文件上传路径:"+targeFile);
			}
			int upload_rate=Integer.parseInt(upload_rateStr);
			if(upload_rate<=0){
				upload_rate=1;
			}

			writeCommData(os, CommKeys.FILE_UPLOAD_CHANNEL+"|"+(String)dataMap.get(CommKeys.FILE_UPLOAD_CHANNEL));
			writeCommData(os, CommKeys.FILE_UPLOAD_TELLER+"|"+(String)dataMap.get(CommKeys.FILE_UPLOAD_TELLER));
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
			return "success";
		} catch (Exception e) {
			logger.debug("文件上传失败:"+e.getMessage(),e);
			throw e;
		} finally{
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
	
	private static String sendFileAsyn(final File local_file, final String targeFile,
			final String server, Map dataMap, final int port) throws Exception{
		Socket socket=null;
		try {
			socket = new Socket(server, port);
			new SendFileThread(socket, targeFile, local_file,dataMap).start();
			return "success";
		} catch (Exception e) {
			logger.debug("文件上传失败:"+e.getMessage(),e);
			if (socket != null && !socket.isClosed())
				socket.close();
			throw e;
		}
	}
	
	public static String recvFileFromServer(File local_file, String server_file,
			String server, int port,Map dataMap) throws Exception {
		if(dataMap==null){
			dataMap=new HashMap();
		}
		Socket socket = null;
		InputStream iis = null;
		OutputStream os = null, fos = null;
		try {
			socket = new Socket(server, port);
			os = socket.getOutputStream();
			iis = socket.getInputStream();
			os.write(RECV_HEADER);
			os.flush();
			writeCommData(os, server_file);
			String file_stamp="";
			String checkMethod="";
			if((String)dataMap.get(CommKeys.FILE_CHECK_FLAG)!=null
					&& ((String)dataMap.get(CommKeys.FILE_CHECK_FLAG)).equals("true")){
				try{
					checkMethod=((String)dataMap.get(CommKeys.FILE_CHECK_METHOD)!=null
							&& !((String)dataMap.get(CommKeys.FILE_CHECK_METHOD)).equals("")) ? (String)dataMap.get(CommKeys.FILE_CHECK_METHOD) : "md5";
					if(local_file.exists() && local_file.length()>0){
						if(checkMethod.equals("sm3")){
							file_stamp=SM3Util.getSM3Value(local_file.getAbsolutePath());
						}else if(checkMethod.equals("md5")){
							file_stamp=Md5Util.getMD5(new File(local_file.getAbsolutePath()));
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			writeCommData(os, CommKeys.FILE_CHECK_FLAG+"|"+(String)dataMap.get(CommKeys.FILE_CHECK_FLAG)
					+"|"+file_stamp+"|"+checkMethod);
			String checkRes=readCommData(iis);
			if(checkRes.trim().equals("same")){
				return "success";
			}
			fos = new FileOutputStream(local_file);
			byte[] buf = new byte[1024];
			int rl = iis.read(buf);
			while (rl > 0) {
				fos.write(buf, 0, rl);
				rl = iis.read(buf);
			}
			os.flush();
			if(local_file.exists() && local_file.length()>0){
				logger.debug("文件下载成功!");
				return "success";
			}
			throw new IOException("远程文件获取失败");
		} catch (Exception e) {
			logger.debug("文件下载失败:"+e.getMessage(),e);
			throw e;
		} finally {
			try {
				if (os != null)
					os.close();
				if (socket != null && !socket.isClosed())
					socket.close();
				if (iis != null)
					iis.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String sendFile(String cmd,Map dataMap,boolean isSync) throws Exception {
		String[] pair = cmd.split(",");
		if (pair.length != 3)
			return "";
		String ipAndPort = pair[2];
		Pattern p = Pattern.compile("tcp://([^:]+):(\\d+)");
		Matcher m = p.matcher(ipAndPort);
		if (m.matches()) {
			String host = m.group(1);
			int port = Integer.parseInt(m.group(2));
			File file = new File(pair[0]);
			return sendFile2Server(file, pair[1], host, port,dataMap,isSync);
		}
		throw new IOException("传入的服务器ip或端口格式错误:"+ipAndPort);
	}
	
	public static String recvFile(String cmd,Map dataMap) throws Exception {
		String[] pair = cmd.split(",");
		if (pair.length != 3)
			return "";
		String ipAndPort = pair[2];
		Pattern p = Pattern.compile("tcp://([^:]+):(\\d+)");
		Matcher m = p.matcher(ipAndPort);
		if (m.matches()) {
			String host = m.group(1);
			int port = Integer.parseInt(m.group(2));
			File file = new File(pair[0]);
			if (!file.exists())
				file.createNewFile();
			return recvFileFromServer(file, pair[1], host, port,dataMap);
		}
		throw new IOException("传入的服务器ip或端口格式错误:"+ipAndPort);
	}
	
	private static String getCheckXml(Map dataMap){
		Document doc=DocumentHelper.createDocument();
		Element root=DocumentHelper.createElement("fileServer");
		doc.setRootElement(root);
		Element ele=root.addElement("file_upload");
		ele.addElement("channelCode").setText((String)dataMap.get(CommKeys.FILE_UPLOAD_CHANNEL));
		ele.addElement("tellerNo").setText((String)dataMap.get(CommKeys.FILE_UPLOAD_TELLER));
		return doc.asXML();
	}
	
	public static String getServerFileFlag(String server, int port,String server_file,Map dataMap) throws Exception {
		Socket socket = null;
		OutputStream os = null;
		InputStream sis = null;
		try {
			socket = new Socket(server, port);
			sis=socket.getInputStream();
			os = socket.getOutputStream();
			os.write(RECV_FILEFLAG_HEADER);
			os.flush();
			writeCommData(os, dataMap.get(CommKeys.FILE_CHECK_METHOD)+";"+server_file);
			String checkRes=readCommData(sis);
			logger.debug("文件校验值获取成功!");
			return checkRes;
		} catch (Exception e) {
			logger.debug("文件校验值获取失败:"+e.getMessage(),e);
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
	
	public static String checkStatus(String server, int port,Map dataMap) throws Exception {
		Socket socket = null;
		OutputStream os = null;
		InputStream sis = null;
		try {
			socket = new Socket(server, port);
			sis=socket.getInputStream();
			os = socket.getOutputStream();
			os.write(CHECK_HEADER);
			os.flush();
			String xml=getCheckXml(dataMap);
			os.write(StringUtil.fixFill(xml.getBytes().length+"", "0", 8).getBytes());
			os.write(xml.getBytes());
			os.flush();
			byte[] dataLen=new byte[8];
			sis.read(dataLen);
			int len=Integer.parseInt(new String(dataLen));
			byte[] data=new byte[len];
			sis.read(data);
			xml=new String(data);
			logger.debug("状态查询成功!");
			return xml;
		} catch (Exception e) {
			logger.debug("状态查询失败:"+e.getMessage(),e);
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
	
	public static void main(String[] args) throws Exception {
		Map dataMap=new HashMap();
		dataMap.put(CommKeys.FILE_UPLOAD_CHANNEL, "001");
		dataMap.put(CommKeys.FILE_UPLOAD_TELLER, "9900013");
		String xml=FileTransfer.checkStatus("192.168.126.129", 5566, dataMap);
		System.out.println(xml);
	}
	
}

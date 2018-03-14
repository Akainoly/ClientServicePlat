package fileTransTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fileTransTool.base.CommKeys;
import fileTransTool.handler.FileTransfer;
import fileTransTool.util.Md5Util;
import fileTransTool.util.SM3Util;
import sun.rmi.runtime.Log;

public class FileTransTool {
	
	public static void main(String[] args) throws Exception {
		String res=FileTransTool.getLocalFileSM3("G:\\SM_Soft.jar");
		System.out.println(res);
//		String res=FileTransTool.getServerFileSM3("20.0.0.175", 5566
//				, "/home/afa4sj/fileServer/sharedFiles/elecImage/001/001_0001.jpg", null);
//		System.out.println(res);
		Map dataMap=new HashMap();
		dataMap.put(CommKeys.FILE_UPLOAD_CHANNEL, "001");
		dataMap.put(CommKeys.FILE_UPLOAD_TELLER, "9900013");
		dataMap.put(CommKeys.FILE_CHECK_FLAG, "true");
		dataMap.put(CommKeys.FILE_CHECK_METHOD, "md5");
		long start=System.currentTimeMillis();
//		String res=FileTransTool.fileTransSyn("d:/test.pdf"
//				, "/home/afa4sj/fileServer/sharedFiles/test/20161221173503.pdf"
//			, "127.0.0.1", 5566,dataMap);
//		long end=System.currentTimeMillis();
//		System.out.println(end-start);
//		System.out.println(res);
//		while (true) {
//			Socket soc=new Socket("127.0.0.1", 5566);
//			soc.close();
//			
//			Socket soc1=new Socket("127.0.0.1", 5566);
//			soc1.close();
//			Thread.sleep(5000);
//		}
//		
		
//		sis=socket.getInputStream();
//		os = socket.getOutputStream();
//		os.write(SEND_HEADER);
		
		
//		for (int i = 0; i < 5; i++) {
//			String res=FileTransTool.fileTransAsyn("d:/20161221173503.pdf"
//					, "/home/afa4sj/fileServer/sharedFiles/test/20161221173503.pdf"
//					, "20.41.45.245", 5566,dataMap);
//			System.out.println(res);
//		}
//		Map dataMap=new HashMap();
//		dataMap.put(CommKeys.FILE_CHECK_FLAG, "true");
//		long start=System.currentTimeMillis();
//		String res=FileTransTool.fileDownloadSyn("d:/20161221173503.pdf"
//				, "/home/afa4sj/fileServer/sharedFiles/test/20161221173503.pdf"
//				, "20.41.45.245", 5566,dataMap);
//		long end=System.currentTimeMillis();
//		System.out.println(end-start);
//		System.out.println(res);
		
//		for (int i = 0; i < 7; i++) {
//			String res=FileTransTool.fileDownloadAsyn("g:/abs20130125bak_"+(i+1)+".rar"
//					, "/home/afa4j/ftp/abs20130125bak.rar"
//					, "192.168.126.129", 5566);
//			System.out.println(res);
//		}
	}
	
	static Logger logger=LoggerFactory.getLogger(FileTransTool.class);
	
	/**
	 * 获取本地文件sm3值
	 * @param filePath 本地文件绝对路径
	 * @return
	 * @throws Exception 文件不存在或传入的路径为非文件时抛出异常 
	 */
	public static String getLocalFileSM3(String filePath) throws Exception{
		File file=new File(filePath);
		if(file.exists() && file.isFile()){
			return SM3Util.getSM3Value(filePath);
		}
		throw new FileNotFoundException("传入的文件路径不存在" +
				"或路径有误:"+filePath);
	}
	
	/**
	 * 获取服务器文件sm3值
	 * @param ip 文件服务器ip
	 * @param port 文件服务器端口
	 * @param server_file 服务端文件路径
	 * @param dataMap 预留
	 * @return
	 * @throws Exception
	 */
	public static String getServerFileSM3(String ip
			,int port,String server_file, Map dataMap) throws Exception{
		String flagVal="";
		try{
			dataMap.put(CommKeys.FILE_CHECK_METHOD, "sm3");
			flagVal=FileTransfer.getServerFileFlag(ip, port, server_file, dataMap);
		}catch (Exception e) {
		}
		return flagVal;
	}
	
	/**
	 * 获取本地文件Md5值
	 * @param filePath 本地文件绝对路径
	 * @return
	 * @throws Exception 文件不存在或传入的路径为非文件时抛出异常 
	 */
	public static String getLocalFileMd5(String filePath) throws Exception{
		File file=new File(filePath);
		if(file.exists() && file.isFile()){
			return Md5Util.getMD5(file);
		}
		throw new FileNotFoundException("传入的文件路径不存在" +
				"或路径有误:"+filePath);
	}
	
	/**
	 * 获取服务器文件Md5值
	 * @param ip 文件服务器ip
	 * @param port 文件服务器端口
	 * @param server_file 服务端文件路径
	 * @param dataMap 预留
	 * @return
	 * @throws Exception
	 */
	public static String getServerFileMd5(String ip
			,int port,String server_file, Map dataMap) throws Exception{
		String flagVal="";
		try{
			dataMap.put(CommKeys.FILE_CHECK_METHOD, "md5");
			flagVal=FileTransfer.getServerFileFlag(ip, port, server_file, dataMap);
		}catch (Exception e) {
		}
		return flagVal;
	}
	
	/**
	 * 异步下载文件
	 * @param localFilePath 本地文件下载路径
	 * @param remoteFilePath 远程文件路径
	 * @param ip 文件服务器ip
	 * @param port 文件服务器端口
	 * @return 异步操作，直接返回success
	 */
	public static String fileDownloadAsyn(final String localFilePath
			,final String remoteFilePath, final String ip
			,final int port,final Map dataMap) {
		new Thread() {
			public void run() {
				try {
					FileTransTool.fileDownloadSyn(localFilePath
							, remoteFilePath, ip, port,dataMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		return "success";
	}
	
	/**
	 * 文件异步上传
	 * @param localFilePath 本地文件路径
	 * @param remoteFilePath 目标文件路径
	 * @param ip 文件服务器ip
	 * @param port 文件服务器端口
	 * @return 异步操作，直接返回success
	 * @throws Exception 
	 */
	public static String fileTransAsyn(	String localFilePath
			,String remoteFilePath
			,String ip, int port,Map dataMap)  throws Exception{
		File file = new File(localFilePath);
		if (!file.exists()) {
			logger.error("要上传的文件不存在...:"+localFilePath);
			throw new NullPointerException("要上传的文件不存在...:"+localFilePath);
		} else {
			String res = FileTransfer.sendFile(
					localFilePath + "," 
							+ remoteFilePath+",tcp://"+ip+":"+port,dataMap,false);
			return res;
		}
	}
	
	/**
	 * 文件同步下载
	 * @param localFilePath 本地文件下载路径
	 * @param remoteFilePath 远程文件路径
	 * @param ip 文件服务器ip
	 * @param port 文件服务器端口
	 * @return 成功返回 success，失败抛出异常
	 * @throws Exception 传输过程中出现异常，或文件服务器连接数超限等原因连接失败
	 */
	public static String fileDownloadSyn(String localFilePath,String remoteFilePath
			,String ip,int port,Map dataMap) throws Exception {
		localFilePath=localFilePath.replaceAll("\\\\", "/");
		String dir=localFilePath.substring(0,localFilePath.lastIndexOf("/"));
		File dirFile=new File(dir);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		String res = FileTransfer.recvFile(localFilePath + "," 
			+ remoteFilePath+",tcp://"+ip+":"+port,dataMap);
		return res;
	}
	
	/**
	 * 文件同步上传
	 * @param localFilePath 本地文件路径
	 * @param remoteFilePath 目标文件路径
	 * @param ip 文件服务器ip
	 * @param port 文件服务器端口
	 * @return 成功返回 success，失败抛出异常
	 * @throws Exception 传输过程中出现异常，或文件服务器连接数超限等原因连接失败
	 */
	public static String fileTransSyn(String localFilePath
			,String remoteFilePath,String ip,int port,Map dataMap) throws Exception{
		File file = new File(localFilePath);
		if (!file.exists()) {
			logger.error("要上传的文件不存在...:"+localFilePath);
			throw new NullPointerException("要上传的文件不存在...:"+localFilePath);
		} else {
			
			String res="success";
			if(dataMap.get(CommKeys.FILE_CHECK_FLAG)!=null
					&& ((String)dataMap.get(CommKeys.FILE_CHECK_FLAG)).equals("true")){
				if(dataMap.get(CommKeys.FILE_CHECK_METHOD)!=null
						&& ((String)dataMap.get(CommKeys.FILE_CHECK_METHOD)).equals("sm3")){
					if(getLocalFileSM3(localFilePath).equals(getServerFileSM3(ip, port, remoteFilePath, dataMap))){
						logger.error("文件校验一致...");
						return res;
					}
				}else if(dataMap.get(CommKeys.FILE_CHECK_METHOD)==null
						|| ((String)dataMap.get(CommKeys.FILE_CHECK_METHOD)).equals("md5")){
					logger.error("文件校验一致...");
					if(getLocalFileMd5(localFilePath).equals(getServerFileMd5(ip, port, remoteFilePath, dataMap))){
						return res;
					}
				}
			}
			res = FileTransfer.sendFile(
					localFilePath + "," 
							+ remoteFilePath+",tcp://"+ip+":"+port,dataMap,true);
			return res;
		}
	}
	
}

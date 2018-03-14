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
	 * ��ȡ�����ļ�sm3ֵ
	 * @param filePath �����ļ�����·��
	 * @return
	 * @throws Exception �ļ������ڻ����·��Ϊ���ļ�ʱ�׳��쳣 
	 */
	public static String getLocalFileSM3(String filePath) throws Exception{
		File file=new File(filePath);
		if(file.exists() && file.isFile()){
			return SM3Util.getSM3Value(filePath);
		}
		throw new FileNotFoundException("������ļ�·��������" +
				"��·������:"+filePath);
	}
	
	/**
	 * ��ȡ�������ļ�sm3ֵ
	 * @param ip �ļ�������ip
	 * @param port �ļ��������˿�
	 * @param server_file ������ļ�·��
	 * @param dataMap Ԥ��
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
	 * ��ȡ�����ļ�Md5ֵ
	 * @param filePath �����ļ�����·��
	 * @return
	 * @throws Exception �ļ������ڻ����·��Ϊ���ļ�ʱ�׳��쳣 
	 */
	public static String getLocalFileMd5(String filePath) throws Exception{
		File file=new File(filePath);
		if(file.exists() && file.isFile()){
			return Md5Util.getMD5(file);
		}
		throw new FileNotFoundException("������ļ�·��������" +
				"��·������:"+filePath);
	}
	
	/**
	 * ��ȡ�������ļ�Md5ֵ
	 * @param ip �ļ�������ip
	 * @param port �ļ��������˿�
	 * @param server_file ������ļ�·��
	 * @param dataMap Ԥ��
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
	 * �첽�����ļ�
	 * @param localFilePath �����ļ�����·��
	 * @param remoteFilePath Զ���ļ�·��
	 * @param ip �ļ�������ip
	 * @param port �ļ��������˿�
	 * @return �첽������ֱ�ӷ���success
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
	 * �ļ��첽�ϴ�
	 * @param localFilePath �����ļ�·��
	 * @param remoteFilePath Ŀ���ļ�·��
	 * @param ip �ļ�������ip
	 * @param port �ļ��������˿�
	 * @return �첽������ֱ�ӷ���success
	 * @throws Exception 
	 */
	public static String fileTransAsyn(	String localFilePath
			,String remoteFilePath
			,String ip, int port,Map dataMap)  throws Exception{
		File file = new File(localFilePath);
		if (!file.exists()) {
			logger.error("Ҫ�ϴ����ļ�������...:"+localFilePath);
			throw new NullPointerException("Ҫ�ϴ����ļ�������...:"+localFilePath);
		} else {
			String res = FileTransfer.sendFile(
					localFilePath + "," 
							+ remoteFilePath+",tcp://"+ip+":"+port,dataMap,false);
			return res;
		}
	}
	
	/**
	 * �ļ�ͬ������
	 * @param localFilePath �����ļ�����·��
	 * @param remoteFilePath Զ���ļ�·��
	 * @param ip �ļ�������ip
	 * @param port �ļ��������˿�
	 * @return �ɹ����� success��ʧ���׳��쳣
	 * @throws Exception ��������г����쳣�����ļ����������������޵�ԭ������ʧ��
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
	 * �ļ�ͬ���ϴ�
	 * @param localFilePath �����ļ�·��
	 * @param remoteFilePath Ŀ���ļ�·��
	 * @param ip �ļ�������ip
	 * @param port �ļ��������˿�
	 * @return �ɹ����� success��ʧ���׳��쳣
	 * @throws Exception ��������г����쳣�����ļ����������������޵�ԭ������ʧ��
	 */
	public static String fileTransSyn(String localFilePath
			,String remoteFilePath,String ip,int port,Map dataMap) throws Exception{
		File file = new File(localFilePath);
		if (!file.exists()) {
			logger.error("Ҫ�ϴ����ļ�������...:"+localFilePath);
			throw new NullPointerException("Ҫ�ϴ����ļ�������...:"+localFilePath);
		} else {
			
			String res="success";
			if(dataMap.get(CommKeys.FILE_CHECK_FLAG)!=null
					&& ((String)dataMap.get(CommKeys.FILE_CHECK_FLAG)).equals("true")){
				if(dataMap.get(CommKeys.FILE_CHECK_METHOD)!=null
						&& ((String)dataMap.get(CommKeys.FILE_CHECK_METHOD)).equals("sm3")){
					if(getLocalFileSM3(localFilePath).equals(getServerFileSM3(ip, port, remoteFilePath, dataMap))){
						logger.error("�ļ�У��һ��...");
						return res;
					}
				}else if(dataMap.get(CommKeys.FILE_CHECK_METHOD)==null
						|| ((String)dataMap.get(CommKeys.FILE_CHECK_METHOD)).equals("md5")){
					logger.error("�ļ�У��һ��...");
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

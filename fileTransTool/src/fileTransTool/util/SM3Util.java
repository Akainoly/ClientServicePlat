package fileTransTool.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import fileTransTool.util.sm3.Hex;
import fileTransTool.util.sm3.SM3Digest;

public class SM3Util {
	
	public static void main(String[] args) throws Exception {
		String value=SM3Util.getSM3Value("G:\\SM_Soft.jar");
		System.out.println("ժҪ���:" + value);
	}
	
	/**
	 * ��ȡ�ļ�SM3�㷨ժҪ������
	 * @param filePath �ļ�·��
	 * @return
	 * @throws Exception
	 */
	public static String getSM3Value(String filePath) throws Exception{
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		String value=null;
		try{
			fis=new FileInputStream(filePath);
			bos=new ByteArrayOutputStream();
			byte[] data=new byte[8*1024];
			while(fis.read(data)!=-1){
				bos.write(data);
			}
			bos.flush();
			byte[] md = new byte[32];  
			SM3Digest sm3 = new SM3Digest();  
	        sm3.update(bos.toByteArray(), 0, bos.toByteArray().length);  
	        sm3.doFinal(md, 0);  
	        value= new String(Hex.encode(md)).toUpperCase();  
			value=value.replaceAll(" ", "");
		}finally{
			if(fis!=null){
				fis.close();
				fis=null;
			}
			if(bos!=null){
				bos.close();
				bos=null;
			}
		}
		return value;
	}

}

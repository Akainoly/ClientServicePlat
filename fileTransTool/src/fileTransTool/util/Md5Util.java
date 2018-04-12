package fileTransTool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Util {
	
	public static Logger logger = LoggerFactory.getLogger(Md5Util.class);
	
	/**
	 * 计算MD5值
	 * 
	 * @param file
	 * @return MD5值的String
	 * @throws Exception 
	 */
	public static String getMD5(File f) throws Exception {
		String hashType = "MD5";
		InputStream ins=null;
		try {
			ins = new FileInputStream(f);
			byte[] buffer = new byte[8192];
			MessageDigest md5 = MessageDigest.getInstance(hashType);
			int len;
			while ((len = ins.read(buffer)) != -1) {
				md5.update(buffer, 0, len);
			}
			return new String(Base64.encodeBase64(md5.digest()));
		}catch (Exception e) {
			logger.error(f.getAbsolutePath()+"获取文件md5值失败:"+e.getMessage(),e);
			throw e;
		}finally {
			if(ins!=null) {
				try {
					ins.close();
				} catch (IOException e) {
					ins=null;
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		System.out.println(Md5Util.getMD5(new File("./dasda.sas")));
	}
}

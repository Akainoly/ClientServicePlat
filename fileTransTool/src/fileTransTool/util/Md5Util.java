package fileTransTool.util;

import java.io.ByteArrayOutputStream;
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
	 */
	public static String getMD5(File file) {
		if(!file.exists()){
			return "";
		}
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		byte[] result = null;
		String md5String = "";
		try {
			baos = new ByteArrayOutputStream();
			is = new FileInputStream(file);
			byte[] buff = new byte[2048];
			int length = 0;
			while ((length = is.read(buff)) > 0) {
				baos.write(buff, 0, length);
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = md.digest(baos.toByteArray());
			if (result != null) {
				md5String = new String(Base64.encodeBase64(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("",e);
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("",e);
				}
			}
		}
		return md5String;
	}
	
}

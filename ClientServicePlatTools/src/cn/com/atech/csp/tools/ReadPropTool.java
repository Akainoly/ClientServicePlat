package cn.com.atech.csp.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * properties配置文件解析工具
 * @author Akainoyu
 *
 */
public class ReadPropTool {
	
	public static Map<String,Map<String,String>> props=new HashMap<String,Map<String,String>>();
	
	public static Object getParamValue(String propFilePath,String key ,Object defaultVal) {
		if(props.get(propFilePath)==null) {
			cacheProperties(propFilePath);
		}
		String value=props.get(propFilePath).get(key);
		Object newValue=null;
		if(value!=null) {
			if(defaultVal instanceof Integer) {
				newValue=Integer.parseInt(value);
			}else if(defaultVal instanceof String) {
				newValue=new String(value);
			}else if(defaultVal instanceof Boolean) {
				newValue=Boolean.parseBoolean(value);
			}else if(defaultVal instanceof Long) {
				newValue=Long.parseLong(value);
			}
		}
		return value==null ? defaultVal : newValue;
	}
	
	private static void cacheProperties(String propFilePath) {
		Path path=Paths.get(propFilePath);
		if(Files.notExists(path, LinkOption.NOFOLLOW_LINKS)){
			System.out.println("配置文件不存在："+propFilePath);
		}else{
			try(InputStream ins=Files.newInputStream(path, StandardOpenOption.READ)){
				Properties prop=new Properties();
				prop.load(ins);
				Map<String,String> params=new HashMap<String,String>();
				prop.keySet().forEach(key->params.put((String) key, prop.getProperty((String) key)));
				props.put(propFilePath, params);
			}catch (Exception e) {
				System.out.println("读取配置文件异常："+propFilePath);
			}
		}
	}
	
	public static void main(String[] args) {
		ReadPropTool.getParamValue("./configuration/csp.properties", "test", "test");
		System.out.println(ReadPropTool.props);
	}
	
}

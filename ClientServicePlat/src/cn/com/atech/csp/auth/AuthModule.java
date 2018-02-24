package cn.com.atech.csp.auth;

import java.util.HashMap;
import java.util.Map;

public class AuthModule {
	
	/**
	 * 主服务授权信息
	 */
	private static boolean mainAuthState=false;
	
	/**
	 * 功能模块授权信息
	 * Map<模块名,授权码>
	 */
	private static Map<String,String> moduleAuthInfo=new HashMap<String,String>();
	
	/**
	 * 检查主模块授权信息
	 * @return
	 */
	public static boolean checkMainAuthInfo() {
		if(true) {
			mainAuthState=true;
			gatherModuleAuthInfo();
		}
		return false;
	}
	
	/**
	 * 缓存功能模块授权信息
	 */
	private static void gatherModuleAuthInfo() {
		moduleAuthInfo.put("", "");
	}
	
	/**
	 * 获取主服务授权状态
	 * @return
	 */
	public static boolean isMainAuthState() {
		return mainAuthState;
	}

	/**
	 * 获取功能模块授权信息
	 * @param moduleName 模块名
	 * @return
	 */
	public static String getModuleAuthInfo(String moduleName) {
		return moduleAuthInfo.get(moduleName);
	}
	
}

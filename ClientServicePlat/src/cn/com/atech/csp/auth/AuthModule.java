package cn.com.atech.csp.auth;

import java.util.HashMap;
import java.util.Map;

public class AuthModule {
	
	/**
	 * ��������Ȩ��Ϣ
	 */
	private static boolean mainAuthState=false;
	
	/**
	 * ����ģ����Ȩ��Ϣ
	 * Map<ģ����,��Ȩ��>
	 */
	private static Map<String,String> moduleAuthInfo=new HashMap<String,String>();
	
	/**
	 * �����ģ����Ȩ��Ϣ
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
	 * ���湦��ģ����Ȩ��Ϣ
	 */
	private static void gatherModuleAuthInfo() {
		moduleAuthInfo.put("", "");
	}
	
	/**
	 * ��ȡ��������Ȩ״̬
	 * @return
	 */
	public static boolean isMainAuthState() {
		return mainAuthState;
	}

	/**
	 * ��ȡ����ģ����Ȩ��Ϣ
	 * @param moduleName ģ����
	 * @return
	 */
	public static String getModuleAuthInfo(String moduleName) {
		return moduleAuthInfo.get(moduleName);
	}
	
}

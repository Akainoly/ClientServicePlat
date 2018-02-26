package cn.com.atech.csp;

import java.io.IOException;

import cn.com.atech.csp.auth.AuthModule;
import cn.com.atech.csp.constants.IClientServiceConstants;
import cn.com.atech.csp.service.MainServer;
import cn.com.atech.csp.tools.ReadPropTool;

/**
 * csp主服务入口
 * @author Akainoyu
 *
 */
public class ClientServiceMain implements IClientServiceConstants {
	
	/**
	 * 主服务端口
	 */
	private int service_port=5564;
	
	/**
	 * 主服务通讯字符编码
	 */
	private String service_charset=DEFAULT_CHARSET;
	
	private static ClientServiceMain csmImpl=null;
	
	private ClientServiceMain() {
		//1.检查主服务授权
		boolean authState=AuthModule.checkMainAuthInfo();
		if(authState) {
			//2.授权成功，加载配置
			initConfiguration();
			//3.启动服务
			new Thread() {
				public void run() {
					MainServer server;
					try {
						server = new MainServer(service_port,service_charset);
						server.service();
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("CSP主服务启动异常："+e.getMessage());
					}
				}
			}.start();
		}
	}
	
	public static ClientServiceMain getInstance() {
		if(csmImpl==null) {
			csmImpl=new ClientServiceMain();
		}
		return csmImpl;
	}
	
	/**
	 * 初始化参数配置
	 */
	private void initConfiguration(){
		service_port=(int) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_port", 5564);
		service_charset=(String) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_port", "GBK");
		System.out.println("初始化参数配置： service_port="+service_port
				+" | service_charset="+service_charset);
	}
	
	public static void main(String[] args) {
		
	}
	
}

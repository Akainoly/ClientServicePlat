package cn.com.atech.csp;

import cn.com.atech.csp.auth.AuthModule;
import cn.com.atech.csp.constants.IClientServiceConstants;
import cn.com.atech.csp.service.http.HttpClientService;
import cn.com.atech.csp.service.tcp.TcpClientService;
import cn.com.atech.csp.tools.ReadPropTool;

/**
 * csp主服务入口
 * @author Akainoyu
 *
 */
public class ClientServiceMain implements IClientServiceConstants {
	
	/**
	 * http服务开关(默认开启，防止因为获取开关配置异常，导致服务无法启动)
	 */
	private boolean http_service_switch=true;
	
	/**
	 * tcp服务开关(默认开启，防止因为获取开关配置异常，导致服务无法启动)
	 */
	private boolean tcp_service_switch=true;
	
	/**
	 * http服务端口
	 */
	private int http_service_port=5564;
	
	/**
	 * tcp服务端口
	 */
	private int tcp_service_port=5562;
	
	private static ClientServiceMain csmImpl=null;
	
	private ClientServiceMain() {
		//1.检查主服务授权
		boolean authState=AuthModule.checkMainAuthInfo();
		if(authState) {
			//2.授权成功，加载配置
			initConfiguration();
			//3.启动服务
			if(http_service_switch){
				HttpClientService hcs=new HttpClientService();
				hcs.serviceStart();
			}
			if(tcp_service_switch){
				TcpClientService tcs=new TcpClientService();
				tcs.serviceStart();
			}
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
		http_service_switch=(boolean) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_switch", true);
		tcp_service_switch=(boolean) ReadPropTool.getParamValue(MAIN_PROPERTIES, "tcp_service_switch", true);
		http_service_port=(int) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_port", 5564);
		tcp_service_port=(int) ReadPropTool.getParamValue(MAIN_PROPERTIES, "tcp_service_port", 5562);
		System.out.println("初始化参数配置：http_service_switch="+http_service_switch
				+" | tcp_service_switch="+tcp_service_switch+" | http_service_port="+http_service_port
				+" | tcp_service_port="+tcp_service_port);
	}
	
	public static void main(String[] args) {
		
	}
	
}

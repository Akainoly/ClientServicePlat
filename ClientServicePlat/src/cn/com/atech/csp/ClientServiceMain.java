package cn.com.atech.csp;

import cn.com.atech.csp.auth.AuthModule;
import cn.com.atech.csp.constants.IClientServiceConstants;
import cn.com.atech.csp.service.http.HttpClientService;
import cn.com.atech.csp.service.tcp.TcpClientService;
import cn.com.atech.csp.tools.ReadPropTool;

/**
 * csp���������
 * @author Akainoyu
 *
 */
public class ClientServiceMain implements IClientServiceConstants {
	
	/**
	 * http���񿪹�(Ĭ�Ͽ�������ֹ��Ϊ��ȡ���������쳣�����·����޷�����)
	 */
	private boolean http_service_switch=true;
	
	/**
	 * tcp���񿪹�(Ĭ�Ͽ�������ֹ��Ϊ��ȡ���������쳣�����·����޷�����)
	 */
	private boolean tcp_service_switch=true;
	
	/**
	 * http����˿�
	 */
	private int http_service_port=5564;
	
	/**
	 * tcp����˿�
	 */
	private int tcp_service_port=5562;
	
	private static ClientServiceMain csmImpl=null;
	
	private ClientServiceMain() {
		//1.�����������Ȩ
		boolean authState=AuthModule.checkMainAuthInfo();
		if(authState) {
			//2.��Ȩ�ɹ�����������
			initConfiguration();
			//3.��������
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
	 * ��ʼ����������
	 */
	private void initConfiguration(){
		http_service_switch=(boolean) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_switch", true);
		tcp_service_switch=(boolean) ReadPropTool.getParamValue(MAIN_PROPERTIES, "tcp_service_switch", true);
		http_service_port=(int) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_port", 5564);
		tcp_service_port=(int) ReadPropTool.getParamValue(MAIN_PROPERTIES, "tcp_service_port", 5562);
		System.out.println("��ʼ���������ã�http_service_switch="+http_service_switch
				+" | tcp_service_switch="+tcp_service_switch+" | http_service_port="+http_service_port
				+" | tcp_service_port="+tcp_service_port);
	}
	
	public static void main(String[] args) {
		
	}
	
}

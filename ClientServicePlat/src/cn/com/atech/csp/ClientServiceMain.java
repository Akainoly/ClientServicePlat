package cn.com.atech.csp;

import java.io.IOException;

import cn.com.atech.csp.auth.AuthModule;
import cn.com.atech.csp.constants.IClientServiceConstants;
import cn.com.atech.csp.service.MainServer;
import cn.com.atech.csp.tools.ReadPropTool;

/**
 * csp���������
 * @author Akainoyu
 *
 */
public class ClientServiceMain implements IClientServiceConstants {
	
	/**
	 * ������˿�
	 */
	private int service_port=5564;
	
	/**
	 * ������ͨѶ�ַ�����
	 */
	private String service_charset=DEFAULT_CHARSET;
	
	private static ClientServiceMain csmImpl=null;
	
	private ClientServiceMain() {
		//1.�����������Ȩ
		boolean authState=AuthModule.checkMainAuthInfo();
		if(authState) {
			//2.��Ȩ�ɹ�����������
			initConfiguration();
			//3.��������
			new Thread() {
				public void run() {
					MainServer server;
					try {
						server = new MainServer(service_port,service_charset);
						server.service();
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("CSP�����������쳣��"+e.getMessage());
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
	 * ��ʼ����������
	 */
	private void initConfiguration(){
		service_port=(int) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_port", 5564);
		service_charset=(String) ReadPropTool.getParamValue(MAIN_PROPERTIES, "http_service_port", "GBK");
		System.out.println("��ʼ���������ã� service_port="+service_port
				+" | service_charset="+service_charset);
	}
	
	public static void main(String[] args) {
		
	}
	
}

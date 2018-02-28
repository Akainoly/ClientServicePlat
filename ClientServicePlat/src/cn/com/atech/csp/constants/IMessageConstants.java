package cn.com.atech.csp.constants;

public interface IMessageConstants extends IClientServiceConstants {

	public static final String SYS_HEAD="SYS_HEAD";
	
	public static final String SYS_HEAD_SERVICETYPE="ServiceType";
	
	public static final String APP_HEAD="APP_HEAD";
	
	public static final String APP_HEAD_METHOD="Method";
	
	public static final String REQUEST_BODY="REQ_BODY";
	
	public static final String RESPONSE_BODY="responseBody";
	
	public static final String RESPONSE_ERRORCODE="errorCode";
	
	public static final String RESPONSE_ERRORMSG="errorMsg";
	
	public static final String XML_ROOT_ELEMENT="clientServicePlat";
	
	public static final String ERRCODE_OK="000000-ͨѶ�ɹ�";
	
	public static final String ERRCODE_FAIL="999999-ͨѶʧ��";
	
	/**
	 * ������Ϊ��
	 */
	public static final String ERRCODE_EMPTY_OP="900001-��������Ϊ��";
	
	/**
	 * �����ڵĲ�����	
	 */
	public static final String ERRCODE_NOTEXIST_OP="900002-�����ڵĲ�������";
	
	/**
	 * �����ڵ�handler��
	 */
	public static final String ERRCODE_NOTEXIST_HANDLER="100003-�����ڵ�handler����";
	
	/**
	 * Ӧ���Զ������
	 */
	public static final String ERRCODE_APPDEFINE="200001";
	
}

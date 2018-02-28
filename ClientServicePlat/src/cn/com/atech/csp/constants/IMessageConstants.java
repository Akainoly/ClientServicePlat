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
	
	public static final String ERRCODE_OK="000000-通讯成功";
	
	public static final String ERRCODE_FAIL="999999-通讯失败";
	
	/**
	 * 操作名为空
	 */
	public static final String ERRCODE_EMPTY_OP="900001-服务类型为空";
	
	/**
	 * 不存在的操作名	
	 */
	public static final String ERRCODE_NOTEXIST_OP="900002-不存在的操作名：";
	
	/**
	 * 不存在的handler名
	 */
	public static final String ERRCODE_NOTEXIST_HANDLER="100003-不存在的handler名：";
	
	/**
	 * 应用自定义错误
	 */
	public static final String ERRCODE_APPDEFINE="200001";
	
}

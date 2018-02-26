package cn.com.atech.csp.constants;

/**
 * 主服务引用的常量定义
 * @author Akainoyu
 *
 */
public interface IClientServiceConstants {

	/**
	 * 主服务配置文件相对路径
	 */
	public static final String MAIN_PROPERTIES="./configuration/csp.properties";
	
	/**
	 * 默认通讯字符集(字符集配置异常时使用)
	 */
	public static final String DEFAULT_CHARSET="GBK";
	
	/**
	 * 	HTTP_REQUEST_METHOD_PUT
	 */
	public static final String PUT="PUT";
	
	/**
	 * 	HTTP_REQUEST_METHOD_GET
	 */
	public static final String GET="GET";
	
	/**
	 * 	HTTP_REQUEST_METHOD_HEAD
	 */
	public static final String HEAD="HEAD";
	
	/**
	 * 	HTTP_REQUEST_METHOD_POST
	 */
	public static final String POST="POST";
	
}

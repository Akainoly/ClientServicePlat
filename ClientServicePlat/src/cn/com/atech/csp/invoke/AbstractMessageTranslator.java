package cn.com.atech.csp.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.atech.csp.constants.IMessageConstants;
import cn.com.atech.csp.service.AbstractRequest;
import cn.com.atech.csp.tools.StringTool;

public abstract class AbstractMessageTranslator implements IMessageConstants {
	
	public String serviceType;
	
	public String method;
	
	public AbstractRequest requestObj;
	
	final Logger logger = LoggerFactory.getLogger(AbstractMessageTranslator.class);
	
	public AbstractRequest getRequestObj() {
		return requestObj;
	}

	public abstract Object getRequest(String requestData) throws Exception;

	public String translate(AbstractRequest requestObj) {
		this.requestObj=requestObj;
		String requestData=requestObj.getMessage();
		Map<String,Object> bodyMap=new HashMap<String,Object>();
		try {
			Object request=getRequest(requestData);
			if(StringTool.isNull(serviceType, false)){
				bodyMap.put(RESPONSE_ERRORCODE, ERRCODE_EMPTY_OP);
			}else{
				Map<String,Object> reqMap=handleReq(request);
				bodyMap=runTrade(method, serviceType, reqMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求处理异常...", e);
			bodyMap.put(RESPONSE_ERRORCODE, ERRCODE_FAIL);
			bodyMap.put(RESPONSE_ERRORMSG, "请求处理异常!");
		}
		logger.info("模块返回数据："+bodyMap.toString());
		String resData=assembleResp(bodyMap);
		return resData;
	}
	
	public abstract Map<String,Object> handleReq(Object request);
	
	public abstract String assembleResp(Map<String,Object> bodyMap);
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> runTrade(String tradeHandlerPath,String operation,Map<String,Object> reqMap) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		logger.info("模块请求数据："+reqMap);
		Map<String, Object> bodyMap=new HashMap<String, Object>();
		try {
			Object tradeHandler = Class.forName(tradeHandlerPath).newInstance();
			Method m = tradeHandler.getClass().getDeclaredMethod(operation, Map.class);
			bodyMap=(Map<String, Object>) m.invoke(tradeHandler, reqMap);
			if(!bodyMap.containsKey(RESPONSE_ERRORCODE)){
				bodyMap.put(RESPONSE_ERRORCODE, ERRCODE_OK);
			}
		}catch (NoSuchMethodException e) {
			e.printStackTrace();
			bodyMap.put(RESPONSE_ERRORCODE, ERRCODE_NOTEXIST_OP+operation);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			bodyMap.put(RESPONSE_ERRORCODE, ERRCODE_NOTEXIST_HANDLER+tradeHandlerPath);
		}
		return bodyMap;
	}
	
}

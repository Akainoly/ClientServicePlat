package cn.com.atech.csp.invoke;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.atech.csp.tools.StringTool;

public class JsonTranslator extends AbstractMessageTranslator{

	public Object getRequest(String requestData) {
		Map<?,?> request=JSON.parseObject(requestData, Map.class);
		JSONObject sysHead=(JSONObject) request.get(SYS_HEAD);
		JSONObject appHead=(JSONObject) request.get(APP_HEAD);
		serviceType=(String) sysHead.get(SYS_HEAD_SERVICETYPE);
		method=(String) appHead.get(APP_HEAD_METHOD);
		return request;
	}
	
	public Map<String,Object> handleReq(Object request){
		Map<String,Object> reqMap=new HashMap<String,Object>();
		if (request instanceof Map<?,?>) {
			Map<?,?> requestMap = (Map<?,?>) request;
			JSONObject body=(JSONObject) requestMap.get(REQUEST_BODY);
			reqMap=handleFamilyJson(body);
		}
		return reqMap;
	}
	
	private Map<String,Object> handleFamilyJson(Object body){
		Map<String,Object> reqMap=new HashMap<String, Object>(); 
		String key="";
		Object value=null;
		List<String> list=null;
		if (body instanceof JSONObject) {
			JSONObject json_body = (JSONObject) body;
			for (Iterator<String> iterator = json_body.keySet().iterator(); iterator.hasNext();) {
				key = iterator.next();
				value=json_body.get(key);
				if(value instanceof String){
					reqMap.put(key, value);
				}else if(value instanceof JSONArray){
					list=((JSONArray) value).toJavaList(String.class);
					reqMap.put(key, list);
				}else if(value instanceof JSONObject){
					reqMap.put(key, handleFamilyJson((JSONObject) value));
				}
			}
		}
		return reqMap;
	}
	
	public String assembleResp(Map<String,Object> bodyMap){
		Map<String,Object> respMap=new HashMap<String,Object>();
		JSONObject sysHead=new JSONObject();
		
		String errorCode=(String) bodyMap.get(RESPONSE_ERRORCODE);
		String errorMsg=(String) bodyMap.get(RESPONSE_ERRORMSG);
		bodyMap.remove(RESPONSE_ERRORCODE);
		bodyMap.remove(RESPONSE_ERRORMSG);
		
		sysHead.put(RESPONSE_ERRORCODE, errorCode.split("-")[0]);
		if(StringTool.isNull(errorMsg, true)){
			sysHead.put(RESPONSE_ERRORMSG, errorCode.split("-")[1]);
		}else{
			sysHead.put(RESPONSE_ERRORMSG, errorMsg);
		}
		respMap.put(SYS_HEAD, sysHead);
		
		JSONObject appHead=new JSONObject();
		appHead.put(APP_HEAD_METHOD, method);
		respMap.put(APP_HEAD, appHead);
		
		if(!bodyMap.isEmpty()){
			JSONObject body=new JSONObject();
			assembleFamilyJson(body, bodyMap);
			respMap.put(RESPONSE_BODY, body);
		}
		return JSON.toJSONString(respMap);
	}
	
	@SuppressWarnings("unchecked")
	private void assembleFamilyJson(JSONObject theone,Map<String,Object> dataMap){
		String key;
		Object value;
		JSONArray jArray=null;
		JSONObject newOne=null;
		for (Iterator<String> iterator = dataMap.keySet().iterator(); iterator.hasNext();) {
			key = iterator.next();
			value=dataMap.get(key);
			if(value instanceof String){
				theone.put(key, value);
			}else if(value instanceof Map<?,?>){
				newOne=new JSONObject();
				assembleFamilyJson(newOne, (Map<String,Object>)value);
				theone.put(key, newOne);
			}else if(value instanceof List<?>){
				jArray=new JSONArray((List<Object>) value);
				theone.put(key, jArray);
			}
		}
	}

}

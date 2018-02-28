package cn.com.atech.csp.invoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.com.atech.csp.tools.StringTool;

public class XmlTranslator extends AbstractMessageTranslator {
	
	public Object getRequest(String requestData) throws DocumentException {
		Document document=DocumentHelper.parseText(requestData);
		serviceType=document.getRootElement().element(SYS_HEAD)
				.elementText(SYS_HEAD_SERVICETYPE);
		method=document.getRootElement()
				.element(APP_HEAD).elementText(APP_HEAD_METHOD);
		return document;
	}
	
	public Map<String, Object> handleReq(Object request){
		Map<String, Object> reqMap=new HashMap<String, Object>();
		if (request instanceof Document) {
			Element body=((Document) request).getRootElement().element(REQUEST_BODY);
			reqMap=handleFamilyEle(body);
		}
		return reqMap;
	}
	
	private Map<String, Object> handleFamilyEle(Element body){
		Map<String, Object> reqMap=new HashMap<String, Object>();
		List<String> list=null;
		Set<String> handledKey=new HashSet<String>();
		Element ele_body = (Element) body;
		List<Element> eles=ele_body.elements();
		for (Element ele : eles) {
			if(handledKey.contains(ele.getName())){
				continue;
			}
			if(ele.elements().size()==0){
				if(ele_body.elements(ele.getName()).size()==1){
					reqMap.put(ele.getName(), ele.getText());
				}else{
					list=new ArrayList<String>();
					for (Element ele1 : ele_body.elements(ele.getName())) {
						list.add(ele1.getText());
					}
					reqMap.put(ele.getName(), list);
				}
			}else{
				reqMap.put(ele.getName(), handleFamilyEle(ele));
			}
			handledKey.add(ele.getName());
		}
		return reqMap;
	}
	
	public String assembleResp(Map<String,Object> bodyMap){
		Document document=DocumentHelper.createDocument();
		document.setXMLEncoding(requestObj.getCharset().name());
		Element root=document.addElement(XML_ROOT_ELEMENT);
		document.setRootElement(root);
		Element sysHead=root.addElement(SYS_HEAD);
		
		String errorCode=(String) bodyMap.get(RESPONSE_ERRORCODE);
		String errorMsg=(String) bodyMap.get(RESPONSE_ERRORMSG);
		bodyMap.remove(RESPONSE_ERRORCODE);
		bodyMap.remove(RESPONSE_ERRORMSG);
		
		sysHead.addElement(RESPONSE_ERRORCODE).setText(errorCode.split("-")[0]);
		if(StringTool.isNull(errorMsg, true)){
			sysHead.addElement(RESPONSE_ERRORMSG).setText(errorCode.split("-")[1]);
		}else{
			sysHead.addElement(RESPONSE_ERRORMSG).setText(errorMsg);
		}
		
		Element appHead=root.addElement(APP_HEAD);
		appHead.addElement(APP_HEAD_METHOD).setText(method);
		
		if(!bodyMap.isEmpty()){
			Element body=root.addElement(RESPONSE_BODY);
			assembleFamilyEle(body, bodyMap);
		}
		return document.asXML();
	}
	
	@SuppressWarnings("unchecked")
	private void assembleFamilyEle(Element theone,Map<String,Object> dataMap){
		String key;
		Object val;
		Element ele=null;
		for (Iterator<String> iterator = dataMap.keySet().iterator(); iterator.hasNext();) {
			key = iterator.next();
			val=dataMap.get(key);
			if(val instanceof String){
				ele=theone.addElement(key);
				ele.setText((String)val);
			}else if(val instanceof Map<?,?>){
				ele=theone.addElement(key);
				assembleFamilyEle(ele, (Map<String,Object>)val);
			}else if(val instanceof List<?>){
				for (String value : (List<String>) val) {
					ele=theone.addElement(key);
					ele.setText(value);
				}
			}
		}
	}

}

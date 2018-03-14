package cn.com.agree.ab.customer.issuecall;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.agree.ab.exterior.Domain;
import cn.com.agree.ab.gui.IIssueCallHandler;
import cn.com.agree.commons.csv.CsvUtil;
import fileTransTool.FileTransTool;

public class CallFileServerHandler implements IIssueCallHandler {
	
	Log logger=LogFactory.getLog(CallFileServerHandler.class);

	public String call(Domain domain, String type, String parameter) {
		String res="fail";
		try{
			logger.info("�����ļ��������:"+parameter);
			Map map=CsvUtil.csvToMap(parameter);
			//�ļ����������ip
			String[] ips=((String) map.get("ip")).split("\\|");
			//�ļ�����������˿�
			String[] ports=((String)map.get("port")).split("\\|");
			if(ips.length!=ports.length){
				throw new RuntimeException("����ķ�������ַ����������鿴�ͻ�����־!");
			}
			//abc�����ļ�·��
			String localFilePath=(String) map.get("localFilePath");
			//�ļ�����������ļ�·��
			String remoteFilePath=(String) map.get("remoteFilePath");
			//�ļ���������
			String transType=(String) map.get("transType");
			
			Map address=new HashMap();
			for (int i = 0; i < ips.length; i++) {
				address.put(ips[i], ports[i]);
			}
			//������ӣ�ϴ��ģʽ
			List connectionUrlList = Arrays.asList(ips);
            Collections.shuffle(connectionUrlList);
            ips = (String[]) connectionUrlList
                    .toArray(new String[ips.length]);
            
            int port=0;
            for (int i = 0; i < ips.length; i++) {
            	port=Integer.parseInt((String)address.get(ips[i]));
            	try{
            		if(transType.equals("download_asyn")){
        				//1.�첽����
        				res=FileTransTool.fileDownloadAsyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else if(transType.equals("download_syn")){
        				//2.ͬ������
        				res=FileTransTool.fileDownloadSyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else if(transType.equals("upload_asyn")){
        				//3.�첽�ϴ�
        				res=FileTransTool.fileTransAsyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else if(transType.equals("upload_syn")){
        				//3.ͬ���ϴ�
        				res=FileTransTool.fileTransSyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else{
        				logger.error("δ֪���ļ���������:"+transType);
        				return res;
        			}
            	}catch (Exception e) {
            		logger.error("��"+i+"�γ������ӣ������ļ�����ʧ��:"+ips[i]+":"+port, e);
            		if(i==ips.length-1){
            			throw e;
            		}
				}
            	if(res.equals("success")){
            		break;
            	}
			}
			logger.info("�ļ��������:"+res);
		}catch (Exception e) {
			logger.error("�ļ���������쳣:"+e.getMessage(), e);
		}
		return res;
	}

}

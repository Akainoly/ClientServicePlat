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
			logger.info("调用文件传输参数:"+parameter);
			Map map=CsvUtil.csvToMap(parameter);
			//文件传输服务器ip
			String[] ips=((String) map.get("ip")).split("\\|");
			//文件传输服务器端口
			String[] ports=((String)map.get("port")).split("\\|");
			if(ips.length!=ports.length){
				throw new RuntimeException("传入的服务器地址参数有误，请查看客户端日志!");
			}
			//abc本地文件路径
			String localFilePath=(String) map.get("localFilePath");
			//文件传输服务器文件路径
			String remoteFilePath=(String) map.get("remoteFilePath");
			//文件传输类型
			String transType=(String) map.get("transType");
			
			Map address=new HashMap();
			for (int i = 0; i < ips.length; i++) {
				address.put(ips[i], ports[i]);
			}
			//随机连接，洗牌模式
			List connectionUrlList = Arrays.asList(ips);
            Collections.shuffle(connectionUrlList);
            ips = (String[]) connectionUrlList
                    .toArray(new String[ips.length]);
            
            int port=0;
            for (int i = 0; i < ips.length; i++) {
            	port=Integer.parseInt((String)address.get(ips[i]));
            	try{
            		if(transType.equals("download_asyn")){
        				//1.异步下载
        				res=FileTransTool.fileDownloadAsyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else if(transType.equals("download_syn")){
        				//2.同步下载
        				res=FileTransTool.fileDownloadSyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else if(transType.equals("upload_asyn")){
        				//3.异步上传
        				res=FileTransTool.fileTransAsyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else if(transType.equals("upload_syn")){
        				//3.同步上传
        				res=FileTransTool.fileTransSyn(localFilePath
        						, remoteFilePath, ips[i], port);
        			}else{
        				logger.error("未知的文件传输类型:"+transType);
        				return res;
        			}
            	}catch (Exception e) {
            		logger.error("第"+i+"次尝试连接，进行文件传输失败:"+ips[i]+":"+port, e);
            		if(i==ips.length-1){
            			throw e;
            		}
				}
            	if(res.equals("success")){
            		break;
            	}
			}
			logger.info("文件传输结束:"+res);
		}catch (Exception e) {
			logger.error("文件传输调用异常:"+e.getMessage(), e);
		}
		return res;
	}

}

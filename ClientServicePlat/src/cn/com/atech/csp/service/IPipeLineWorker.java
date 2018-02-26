package cn.com.atech.csp.service;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import cn.com.atech.csp.constants.IClientServiceConstants;

public interface IPipeLineWorker extends IClientServiceConstants {
	
	public void work(SelectionKey key) throws IOException;

}

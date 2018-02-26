package cn.com.atech.csp.service;

import java.io.IOException;

import cn.com.atech.csp.service.DataChannel;

public interface IMessageSender {
	// ׼�����͵�����
	public void prepare() throws IOException;

	// ����ͨ�����Ͳ������ݣ�����������ݷ�����ϣ��ͷ���false
	// �����������δ���ͣ��ͷ���true
	// ������ݻ�û��׼���ã����׳�IllegalStateException
	public boolean send(DataChannel dc) throws IOException;

	// ������������������ϣ��͵��ô˷������ͷ�����ռ�õ���Դ
	public void release() throws IOException;
}

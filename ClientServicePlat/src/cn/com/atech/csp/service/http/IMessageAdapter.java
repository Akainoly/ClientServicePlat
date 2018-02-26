package cn.com.atech.csp.service.http;

import java.io.IOException;

public interface IMessageAdapter {
	// ׼�����͵�����
	public void prepare() throws IOException;

	// ����ͨ�����Ͳ������ݣ�����������ݷ�����ϣ��ͷ���false
	// �����������δ���ͣ��ͷ���true
	// ������ݻ�û��׼���ã����׳�IllegalStateException
	public boolean send(DataChannel dc) throws IOException;

	// ������������������ϣ��͵��ô˷������ͷ�����ռ�õ���Դ
	public void release() throws IOException;
}

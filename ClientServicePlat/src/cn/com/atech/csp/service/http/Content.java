package cn.com.atech.csp.service.http;

/**
 * ��ʾ���������͸��ͻ�����������
 */
public interface Content extends IMessageAdapter {
  //���ݵ�����
  String type();

  //�����ݻ�û��׼��֮ǰ������û�е���prepare()����֮ǰ��length()��������-1��
  long length();
}


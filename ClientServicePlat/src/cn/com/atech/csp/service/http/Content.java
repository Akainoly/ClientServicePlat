package cn.com.atech.csp.service.http;

import cn.com.atech.csp.service.IMessageSender;

/**
 * ��ʾ���������͸��ͻ�����������
 */
public interface Content extends IMessageSender {
  //���ݵ�����
  String type();

  //�����ݻ�û��׼��֮ǰ������û�е���prepare()����֮ǰ��length()��������-1��
  long length();
}


package cn.com.atech.csp.service.http;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;

import cn.com.atech.csp.constants.IClientServiceConstants;

public class Response implements IMessageAdapter {

  private ResponseCode code;  //״̬����
  private Content content;  //��Ӧ����
  private boolean headersOnly;  //��ʾHTTP��Ӧ���Ƿ��������Ӧͷ
  private ByteBuffer headerBuffer = null;  //��Ӧͷ
  
  public Response(Content c) {
	  
  }

  public Response(ResponseCode rc, Content c) {
    this(rc, c, null);
  }

  public Response(ResponseCode rc, Content c, String head) {
    code = rc;
    content = c;
    headersOnly = (head!=null && head.equalsIgnoreCase(IClientServiceConstants.HEAD));
  }

  private static String CRLF = "\r\n";
  private static Charset responseCharset = Charset.forName("GBK");

  /* ������Ӧͷ�����ݣ�������ŵ�һ��ByteBuffer�� */
  private ByteBuffer headers() {
    CharBuffer cb = CharBuffer.allocate(1024);
    for (;;) {
        try {
            cb.put("HTTP/1.1 ").put(code.toString()).put(CRLF);
            cb.put("Server: nio/1.1").put(CRLF);
            cb.put("Content-type: ").put(content.type()).put(CRLF);
            cb.put("Content-length: ")
                .put(Long.toString(content.length())).put(CRLF);
            cb.put(CRLF);
            break;
        } catch (BufferOverflowException x) {
            assert(cb.capacity() < (1 << 16));
            cb = CharBuffer.allocate(cb.capacity() * 2);
            continue;
        }
    }
    cb.flip();
    return responseCharset.encode(cb);  //����
  }

  /* ׼��HTTP��Ӧ�е������Լ���Ӧͷ������ */
  public void prepare() throws IOException {
    content.prepare();
    headerBuffer= headers();
  }

  /* ����HTTP��Ӧ�����ȫ��������ϣ�����false�����򷵻�true */
  public boolean send(DataChannel dc) throws IOException {
    if (headerBuffer == null)
        throw new IllegalStateException();

    //������Ӧͷ
    if (headerBuffer.hasRemaining()) {
        if (dc.write(headerBuffer) <= 0)
            return true;
    }

    //������Ӧ����
    if (!headersOnly) {
        if (content.send(dc))
            return true;
    }

    return false;
  }

  /* �ͷ���Ӧ����ռ�õ���Դ */
  public void release() throws IOException {
    content.release();
  }
}

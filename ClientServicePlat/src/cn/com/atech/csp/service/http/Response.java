package cn.com.atech.csp.service.http;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;

import cn.com.atech.csp.constants.IClientServiceConstants;

public class Response implements IMessageAdapter {

  private ResponseCode code;  //状态代码
  private Content content;  //响应正文
  private boolean headersOnly;  //表示HTTP响应中是否仅包含响应头
  private ByteBuffer headerBuffer = null;  //响应头
  
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

  /* 创建响应头的内容，把它存放到一个ByteBuffer中 */
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
    return responseCharset.encode(cb);  //编码
  }

  /* 准备HTTP响应中的正文以及响应头的内容 */
  public void prepare() throws IOException {
    content.prepare();
    headerBuffer= headers();
  }

  /* 发送HTTP响应，如果全部发送完毕，返回false，否则返回true */
  public boolean send(DataChannel dc) throws IOException {
    if (headerBuffer == null)
        throw new IllegalStateException();

    //发送响应头
    if (headerBuffer.hasRemaining()) {
        if (dc.write(headerBuffer) <= 0)
            return true;
    }

    //发送响应正文
    if (!headersOnly) {
        if (content.send(dc))
            return true;
    }

    return false;
  }

  /* 释放响应正文占用的资源 */
  public void release() throws IOException {
    content.release();
  }
}

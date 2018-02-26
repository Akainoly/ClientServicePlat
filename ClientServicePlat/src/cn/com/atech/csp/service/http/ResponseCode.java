package cn.com.atech.csp.service.http;

public class ResponseCode {

	private int number;
	private String reason;

	public ResponseCode(int i, String r) {
		number = i;
		reason = r;
	}

	public String toString() {
		return number + " " + reason;
	}
	
	public static final ResponseCode OK = new ResponseCode(200, "OK");
	public static final ResponseCode BAD_REQUEST = new ResponseCode(400, "Bad Request");
	public static final ResponseCode NOT_FOUND = new ResponseCode(404, "Not Found");
	public static final ResponseCode METHOD_NOT_ALLOWED = new ResponseCode(405, "Method Not Allowed");

}

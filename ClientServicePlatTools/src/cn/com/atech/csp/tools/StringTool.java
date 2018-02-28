package cn.com.atech.csp.tools;

public class StringTool {

	public static boolean isNull(String str,boolean exactly){
		if(str==null || str.trim().equals("")){
			return true;
		}else{
			if(exactly){
				if(str.trim().equalsIgnoreCase("null")){
					return true;
				}
			}
		}
		return false;
	}
	
}

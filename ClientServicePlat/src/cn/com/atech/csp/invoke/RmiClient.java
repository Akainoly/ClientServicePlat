package cn.com.atech.csp.invoke;
 
import java.lang.reflect.Method;
import java.rmi.Naming;

import test.rmi.IModuleInvoker;
 
public class RmiClient {
 
    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost:1099/recordModule";
        IModuleInvoker mInvoker = (IModuleInvoker) Naming.lookup(url);
        Method m=mInvoker.getClass().getDeclaredMethod("invoke", String.class);
        String result = (String) m.invoke(mInvoker, "module invoke!");
//        String result = mInvoker.invoke("module invoke!");
        System.out.println(result);
    }
}
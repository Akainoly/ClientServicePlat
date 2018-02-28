package test.rmi;
 
import java.rmi.Naming;
 
public class RmiClient {
 
    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost:1099/recordModule";
        IModuleInvoker mInvoker = (IModuleInvoker) Naming.lookup(url);
        String result = mInvoker.invoke("module invoke!");
        System.out.println(result);
    }
}
package test.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiServer {

	public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {
		int port = 1099;
		String url = "rmi://localhost:"+port+"/recordModule";
		LocateRegistry.createRegistry(port);
		Naming.rebind(url,new RecordModuleInvoker());
		while(true) {
			
		}
	}

}

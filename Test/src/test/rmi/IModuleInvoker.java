package test.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IModuleInvoker extends Remote {
	
	public String invoke(String paraXml) throws RemoteException;

}

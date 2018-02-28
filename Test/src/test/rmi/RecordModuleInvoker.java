package test.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RecordModuleInvoker extends UnicastRemoteObject implements IModuleInvoker ,Serializable {

	protected RecordModuleInvoker() throws RemoteException {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String invoke(String paraXml) throws RemoteException {
		System.out.println(paraXml);
		return " ’µΩ«Î«Û!";
	}

}

package Server;

import java.io.File;
import java.rmi.RemoteException;

public class PeerInterfaceImplementation implements PeerInterface {

	@Override
	public void backup(File file, int replicationDeg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(File file) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restore(File file) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reclaim(int space) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String state() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}


}

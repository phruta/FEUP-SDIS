package Server;

import java.io.File;
import java.rmi.RemoteException;

public class PeerInterfaceImplementation implements PeerInterface {

	@Override
	public void backup(File file, int replicationDeg) throws RemoteException {
		System.out.println(file.toString()+ "\n" + Integer.toString(replicationDeg));
		return;
		
	}

	@Override
	public void delete(File file) throws RemoteException {
		System.out.println(file.toString());
		return;
		
	}

	@Override
	public void restore(File file) throws RemoteException {
		System.out.println(file.toString());
		return;

	}

	@Override
	public void reclaim(int space) throws RemoteException {
		System.out.println(Integer.toString(space));
		return;
		
	}

	@Override
	public String state() throws RemoteException {
		System.out.println("state working");
		return "lol";
	}


}

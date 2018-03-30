package Server;

import java.io.File;
import java.rmi.RemoteException;

import protocol.*;


public class PeerInterfaceImplementation implements PeerInterface {

	@Override
	public void backup(File file, int replicationDeg) throws RemoteException {
		new Thread(new Backup(file, replicationDeg)).start();
		
	}

	@Override
	public void delete(File file) throws RemoteException {
		new Thread(new Remove(file)).start();
	}

	@Override
	public void restore(File file) throws RemoteException {
		new Thread(new Restore(file)).start();
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

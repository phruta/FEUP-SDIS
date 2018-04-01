package Server;

import java.io.File;
import java.rmi.RemoteException;

import protocol.*;
import utils.Utils;

public class PeerInterfaceImplementation implements PeerInterface {

	@Override
	public void backup(String fileStr, int replicationDeg) throws RemoteException {
		File file = Utils.getFile(fileStr);

		if (file != null)
			new Thread(new Backup(file, replicationDeg)).start();

	}

	@Override
	public void delete(String fileStr) throws RemoteException {
		File file = Utils.getFile(fileStr);

		if (file != null)
			new Thread(new Delete(file)).start();
	}

	@Override
	public void restore(String fileStr) throws RemoteException {
		File file = Utils.getFile(fileStr);

		if (file != null)
			new Thread(new Restore(file)).start();
	}

	@Override
	public void reclaim(int space) throws RemoteException {
		new Thread(new Reclaim(space)).start();

	}

	@Override
	public void state() throws RemoteException {
		new Thread(new State()).start();
	}

}

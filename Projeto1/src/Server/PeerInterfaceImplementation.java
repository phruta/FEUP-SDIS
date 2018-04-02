package Server;

import java.io.File;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import protocol.*;
import utils.Utils;

public abstract class PeerInterfaceImplementation implements PeerInterface {
	protected static ExecutorService threadpool;
	@Override
	public void  backup(String fileStr, int replicationDeg) throws RemoteException {
		File file = Utils.getFile(fileStr);

		if (file != null)
		threadpool.execute(new Backup(file, replicationDeg));	
	}

	@Override
	public void delete(String fileStr) throws RemoteException {
		File file = Utils.getFile(fileStr);

		if (file != null)
		threadpool.execute(new Delete(file));	
	}

	@Override
	public void restore(String fileStr) throws RemoteException {
		File file = Utils.getFile(fileStr);

		if (file != null)
		threadpool.execute(new Restore(file));	
	}

	@Override
	public void reclaim(int space) throws RemoteException {
		threadpool.execute(new Reclaim(space));	
		
	}

	@Override
	public void state() throws RemoteException {
		threadpool.execute(new State());	
	}


}

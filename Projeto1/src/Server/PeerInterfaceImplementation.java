package Server;

import java.io.File;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import protocol.*;


public abstract class PeerInterfaceImplementation implements PeerInterface {
	protected static ExecutorService threadpool;
	@Override
	public void backup(File file, int replicationDeg) throws RemoteException {
		threadpool.execute(new Backup(file, replicationDeg));	
	}

	@Override
	public void delete(File file) throws RemoteException {
		threadpool.execute(new Delete(file));	
	}

	@Override
	public void restore(File file) throws RemoteException {
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

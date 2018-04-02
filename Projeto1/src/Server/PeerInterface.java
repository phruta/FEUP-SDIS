package Server;

import java.rmi.*;

public interface PeerInterface extends Remote {
	void backup(String file, int replicationDeg) throws RemoteException;

	void delete(String file) throws RemoteException;

	void restore(String file) throws RemoteException;

	void reclaim(int space) throws RemoteException;

	void state() throws RemoteException;
}

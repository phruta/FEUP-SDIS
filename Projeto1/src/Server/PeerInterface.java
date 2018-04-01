package Server;

import java.io.File;
import java.rmi.*;

public interface PeerInterface extends Remote {
    void backup(File file,int replicationDeg) throws RemoteException;
    void delete(File file) throws RemoteException;
    void restore(File file) throws RemoteException;
    void reclaim(int space) throws RemoteException;
    void state() throws  RemoteException;
}

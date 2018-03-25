package Server;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import multicastChannels.*;

public class Peer extends PeerInterfaceImplementation {

	public final static int PROTOCOL_VERSION = 0;
	public final static int SERVER_ID = 1;
	public final static int ACCESS_POINT = 2;
	public final static int MC_ADDRESS = 3;
	public final static int MC_PORT = 4;
	public final static int MDB_ADDRESS = 5;
	public final static int MDB_PORT = 6;
	public final static int MDR_ADDRESS = 7;
	public final static int MDR_PORT = 8;
	public final static int MC_CHANNEL = 0;
	public final static int MDB_CHANNEL = 1;
	public final static int MDR_CHANNEL = 2;
	

	private static Integer PeerID;
	private static String version;
	private static String accessPoint;
	private static Channel[] MulticastChannels= new Channel[3];

	public static void main(String[] args) {
		if (!validArgs(args))
			return;
		
		if(!loadRMI())
			return;
        System.err.println("Server ready"); 
	}
	
	private static boolean loadRMI() {
	      try { 
	          // Instantiating the implementation class 
	          PeerInterfaceImplementation obj = new PeerInterfaceImplementation(); 
	     
	          // Exporting the object of implementation class  
	          // (here we are exporting the remote object to the stub) 
	          PeerInterface stub = (PeerInterface) UnicastRemoteObject.exportObject(obj, 0);  
	          
	          // Binding the remote object (stub) in the registry 
	          Registry registry = LocateRegistry.getRegistry(); 
	          
	          registry.rebind(accessPoint, stub);  
	       } catch (Exception e) { 
	          System.err.println("Server exception: " + e.toString()); 
	          e.printStackTrace();
	          return false;
	       } 
		return true;
	}

	private static boolean validArgs(String[] args) {
		try {
			if (args.length != 9) {
				System.out.println(
						"Error: Peer should have the following arguments: \n Peer <version> <ID> <Access Point> <MC address> <MC port> <MDB address> <MDB port> <MDR address> <MDR port>");
				return false;
			}
			
			PeerID=Integer.parseInt(args[SERVER_ID]);
			version=args[PROTOCOL_VERSION];
			accessPoint= args[ACCESS_POINT];
			
			InetAddress mcAddress = InetAddress.getByName(args[MC_ADDRESS]);
			int mcPort = Integer.parseInt(args[MC_PORT]);
			MulticastChannels[MC_CHANNEL]=new MC(mcAddress,mcPort);

			InetAddress mdbAddress = InetAddress.getByName(args[MDB_ADDRESS]);
			int mdbPort = Integer.parseInt(args[MDB_PORT]);
			MulticastChannels[MDB_CHANNEL]=new MDB(mdbAddress,mdbPort);

			InetAddress mdrAddress = InetAddress.getByName(args[MDR_ADDRESS]);
			int mdrPort = Integer.parseInt(args[MDR_PORT]);
			MulticastChannels[MDR_CHANNEL]=new MDR(mdrAddress,mdrPort);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
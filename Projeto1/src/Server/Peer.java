package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import files.DataBase;
import files.DiskSpace;
import multicastChannels.*;
import utils.Utils;

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
	public final static String DATABASE_PATH="../Databases/";
	public final static String DS=".ds";
	public final static String DB=".cdb";

	private static volatile DataBase db;
	private static volatile DiskSpace ds;
	private static volatile String peerID;
	private static volatile String version;
	

	public static Channel[] MulticastChannels = new Channel[3];

	private static String accessPoint;

	public static void main(String[] args) {
		if (!validArgs(args))
			return;

		if (!loadRMI())
			return;
		loadDatabase();
		loadDiskSpace();

		new Thread(MulticastChannels[MC_CHANNEL]).start();
		new Thread(MulticastChannels[MDB_CHANNEL]).start();
		new Thread(MulticastChannels[MDR_CHANNEL]).start();
		threadpool=Executors.newFixedThreadPool(3);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		       for(Channel channel: MulticastChannels)
		        	channel.shutdown();
		       threadpool.shutdown();
		       try {
				threadpool.awaitTermination(3, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.getMessage();
				e.printStackTrace();
			}
		    }
		});
		
		System.err.println("Server ready");
	}

	private static void loadDiskSpace() {
		if((setDs((DiskSpace)load(DATABASE_PATH+getPeerID()+DS)))==null) {
			setDs(new DiskSpace());
			save(getDs(),DATABASE_PATH+getPeerID()+DS);
		}
	}

	private static void loadDatabase() {
		if((setDb((DataBase)load(DATABASE_PATH+getPeerID()+DB)))==null) {
			setDb(new DataBase());
			save(getDb(),DATABASE_PATH+getPeerID()+DB);
		}
		getDb().clearFilesRestored();
		getDb().clearRemovedPutChunks();
		getDb().clearRestoredChunks();
	}
	
	public synchronized static void saveDatabases() {
		save(getDs(),DATABASE_PATH+getPeerID()+DS);
    	save(getDb(),DATABASE_PATH+getPeerID()+DB);
	}

	private static boolean loadRMI() {
		try {
			// Instantiating the implementation class
			Peer obj = new Peer();

			// Exporting the object of implementation class
			// (here we are exporting the remote object to the stub)
			PeerInterface stub = (PeerInterface) UnicastRemoteObject.exportObject(obj, 0);

			// Binding the remote object (stub) in the registry

			LocateRegistry.getRegistry().rebind(accessPoint, stub);
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

			setPeerID(args[SERVER_ID]);
			setVersion(args[PROTOCOL_VERSION]);
			accessPoint = args[ACCESS_POINT];

			InetAddress mcAddress = InetAddress.getByName(args[MC_ADDRESS]);
			int mcPort = Integer.parseInt(args[MC_PORT]);
			MulticastChannels[MC_CHANNEL] = new Channel(mcAddress, mcPort);

			InetAddress mdbAddress = InetAddress.getByName(args[MDB_ADDRESS]);
			int mdbPort = Integer.parseInt(args[MDB_PORT]);
			MulticastChannels[MDB_CHANNEL] = new Channel(mdbAddress, mdbPort);

			InetAddress mdrAddress = InetAddress.getByName(args[MDR_ADDRESS]);
			int mdrPort = Integer.parseInt(args[MDR_PORT]);
			MulticastChannels[MDR_CHANNEL] = new Channel(mdrAddress, mdrPort);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void save(Object obj, String path) {
		try {
			new File(path).getParentFile().mkdirs(); // if file already exists will do nothing 
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		} catch (Exception i) {
			i.getMessage();
			i.printStackTrace();
		}
	}

	private static Object load(String path) {

		try {
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);

			Object to_load = ois.readObject();
			ois.close();
			fis.close();
			return to_load;
		} catch (Exception e) {
			return null;
		}
	}

	public static DataBase getDb() {
		return db;
	}

	private static DataBase setDb(DataBase db) {
		Peer.db = db;
		return db;
	}

	public static DiskSpace getDs() {
		return ds;
	}

	private static DiskSpace setDs(DiskSpace ds) {
		Peer.ds = ds;
		return ds;
	}

	public static String getPeerID() {
		return peerID;
	}

	private static void setPeerID(String peerID) {
		Peer.peerID = peerID;
	}

	public static String getVersion() {
		return version;
	}

	private static void setVersion(String version) {
		Peer.version = version;
	}
}

package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import files.DataBase;
import files.DiskSpace;
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
	public final static String DATABASE_PATH="./Databases/";
	public final static String DS=".ds";
	public final static String DB=".cdb";

	public static volatile DataBase db;
	public static volatile DiskSpace ds;
	public static volatile String peerID;
	public static volatile String version;

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

		System.err.println("Server ready");

		Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
            	MulticastChannels[MC_CHANNEL].shutdown();
            	MulticastChannels[MDB_CHANNEL].shutdown();
            	MulticastChannels[MDR_CHANNEL].shutdown();

                try {
                	save(ds,DATABASE_PATH+peerID+DS);
                	save(db,DATABASE_PATH+peerID+DB);
                } catch (Exception e) {
                    System.err.println("Error saving databases");
                }
                System.exit(0);
            }
        });
	}

	private static void loadDiskSpace() {
		if((ds=(DiskSpace)load(DATABASE_PATH+peerID+DS))==null) {
			ds = new DiskSpace();
			save(ds,DATABASE_PATH+peerID+DS);
		}
	}

	private static void loadDatabase() {
		if((db=(DataBase)load(DATABASE_PATH+peerID+DB))==null) {
			db = new DataBase();
			save(db,DATABASE_PATH+peerID+DB);
		}
		db.clearFilesRestored();
		db.clearRemovedPutChunks();
		db.clearRestoredChunks();
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

			peerID = args[SERVER_ID];
			version = args[PROTOCOL_VERSION];
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

	public static void save(Object obj, String path) {
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

	public static Object load(String path) {

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
}

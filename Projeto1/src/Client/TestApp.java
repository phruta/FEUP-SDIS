package Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.io.File;
import java.lang.reflect.*;


import utils.Pair;
import Server.PeerInterface;

public class TestApp {

	private static HashMap<String, Pair<Integer, Method>> map = new HashMap<>();
	private static String[] args;
	private static PeerInterface stub;

	private static final int ACCESS_POINT = 0;
	private static final int PROTOCOL = 1;
	private static final int PATH = 2;
	private static final int DISK_SPACE = 2;
	private static final int REPLICATION_DEGREE = 3;

	static {
		try {
			map.put("BACKUP", new Pair<>(4, TestApp.class.getMethod("callBackup")));
			map.put("RESTORE", new Pair<>(3, TestApp.class.getMethod("callRestore")));
			map.put("DELETE", new Pair<>(3, TestApp.class.getMethod("callDelete")));
			map.put("RECLAIM", new Pair<>(3, TestApp.class.getMethod("callReclame")));
			map.put("STATE", new Pair<>(2, TestApp.class.getMethod("callState")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] arguments) {
		args = arguments;
		if (loadRMI() == null)
			return;
		RMIcall();
	}

	public static PeerInterface loadRMI() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			stub = (PeerInterface) registry.lookup(args[ACCESS_POINT]);
			return stub;
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
			return null;
		}
	}

	public static void RMIcall() {
		Pair<Integer, Method> OperationValue = map.get(args[PROTOCOL].toUpperCase());

		if (OperationValue == null || args.length != OperationValue.getKey()) {
			System.out.println(
					"Error: TestApp should have the following arguments: \n TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2> ");
			return;
		}

		try {
			OperationValue.getValue().invoke(null);
		} catch (Exception e) {
			System.out.println("An unexpected error ocurred");
		}
		return;
	}

	public static void callBackup() {
		File file = getFile();
		if (file==null)
			return;

		try {
			int replicationDegree = Integer.parseInt(args[REPLICATION_DEGREE]);
			stub.backup(file, replicationDegree);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}

	}

	public static void callRestore() {
		File file = getFile();
		if (file==null)
			return;

		try {
			stub.restore(file);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	public static void callDelete() {
		File file = getFile();
		if (file==null)
			return;

		try {
			stub.delete(file);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	public static void callReclame() {
		try {
			int space = Integer.parseInt(args[DISK_SPACE]);
			stub.reclaim(space);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	public static void callState() {
		try {
			stub.state();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	private static File getFile() {
		File file = new File(args[PATH]);

		if (!file.exists() || !file.isFile()) {
			System.out.println("ERROR: The path you indicated is not a file");
			return null;
		}

		return file;
	}

}

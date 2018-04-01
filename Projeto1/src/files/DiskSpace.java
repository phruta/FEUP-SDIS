package files;

import java.io.Serializable;

import Server.Peer;
import utils.Utils;

public class DiskSpace implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3654172505116195725L;
	public final static String DATABASE_PATH = "../Databases/";
	public final static String DS = ".ds";

	private static final int DEFAULT_CAPACITY = 8388608;

	public volatile static DiskSpace ds = null;

	private volatile int capacitySpace;
	private volatile int usedSpace;

	public static DiskSpace getInstance() {
		if (ds == null) {
			loadDiskSpace();
		}
		return ds;
	}

	private static void loadDiskSpace() {
		if ((ds = (DiskSpace) Utils.load(DATABASE_PATH + Peer.peerID + DS)) == null) {
			ds = new DiskSpace();
			saveDiskSpace();
		}
	}

	private static void saveDiskSpace() {
		Utils.save(ds, DATABASE_PATH + Peer.peerID + DS);
	}

	public DiskSpace() {
		super();
		this.capacitySpace = DEFAULT_CAPACITY;
		this.usedSpace = 0;
	}

	public synchronized int getCapacitySpace() {
		return capacitySpace;
	}

	public synchronized void setCapacitySpace(int capacitySpace) {
		this.capacitySpace = capacitySpace;
		saveDiskSpace();
	}

	public synchronized int getUsedSpace() {
		return usedSpace;
	}

	public synchronized int getSpaceLeft() {
		return capacitySpace - usedSpace;
	}

	public synchronized void setUsedSpace(int usedSpace) {
		this.usedSpace = usedSpace;
		saveDiskSpace();
	}

	public synchronized void addUsedSpace(int usedSpace) {
		this.usedSpace += usedSpace;
		saveDiskSpace();
	}

	public synchronized void removeUsedSpace(int usedSpace) {
		this.usedSpace -= usedSpace;
		saveDiskSpace();
	}

	@Override
	public String toString() {
		return "DiskSpace [capacitySpace=" + Integer.toString(capacitySpace) + ", usedSpace="
				+ Integer.toString(usedSpace) + "]";
	}

}

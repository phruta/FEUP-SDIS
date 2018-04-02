package files;

import java.io.Serializable;

import Server.Peer;

public class DiskSpace implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3654172505116195725L;

	private static final int DEFAULT_CAPACITY = 8000000;
	
	private volatile int capacitySpace;
	
	public DiskSpace() {
		super();
		this.capacitySpace = DEFAULT_CAPACITY;
	}

	public synchronized int getCapacitySpace() {
		return capacitySpace;
	}

	public synchronized void setCapacitySpace(int capacitySpace) {
		this.capacitySpace = capacitySpace;
	}

	@Override
	public String toString() {
		return "DiskSpace [capacitySpace=" + Integer.toString(capacitySpace) + ", usedSpace=" + Peer.getDb().chunksDataSize() + "]";
	}
	
}

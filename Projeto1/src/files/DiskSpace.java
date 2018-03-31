package files;

import java.io.Serializable;

public class DiskSpace implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3654172505116195725L;

	private static final int DEFAULT_CAPACITY = 8000000;
	
	private volatile int capacitySpace;
	private volatile int usedSpace;
	
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
	}

	public synchronized int getUsedSpace() {
		return usedSpace;
	}
	
	public synchronized int getSpaceLeft() {
		return capacitySpace-usedSpace;
	}

	public synchronized void setUsedSpace(int usedSpace) {
		this.usedSpace = usedSpace;
	}
	
	public synchronized void addUsedSpace(int usedSpace) {
		this.usedSpace += usedSpace;
	}
	
	public synchronized void removeUsedSpace(int usedSpace) {
		this.usedSpace -= usedSpace;
	}
	
	@Override
	public String toString() {
		return "DiskSpace [capacitySpace=" + Integer.toString(capacitySpace) + ", usedSpace=" + Integer.toString(usedSpace) + "]";
	}
	
}

package files;

public class DiskSpace {
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
	
}

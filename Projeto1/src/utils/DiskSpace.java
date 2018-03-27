package utils;

public class DiskSpace {
	private static final int DEFAULT_CAPACITY = 8000000;
	
	private int capacitySpace;
	private int usedSpace;
	
	public DiskSpace() {
		super();
		this.capacitySpace = DEFAULT_CAPACITY;
		this.usedSpace = 0;
	}

	public int getCapacitySpace() {
		return capacitySpace;
	}

	public void setCapacitySpace(int capacitySpace) {
		this.capacitySpace = capacitySpace;
	}

	public int getUsedSpace() {
		return usedSpace;
	}

	public void setUsedSpace(int usedSpace) {
		this.usedSpace = usedSpace;
	}
	
	public void addUsedSpace(int usedSpace) {
		this.usedSpace += usedSpace;
	}
	
	public void removeUsedSpace(int usedSpace) {
		this.usedSpace -= usedSpace;
	}
	
}

package files;


public class File {
	private String fileID;
	private int replicationDegree;
	private int numChunks;
	
	@Override
	public String toString() {
		return "FileInfo [fileID=" + fileID + ", replicationDegree=" + replicationDegree + ", numChunks=" + numChunks
				+ "]";
	}

	public File(String fileID, int replicationDegree, int numChunks) {
		this.fileID = fileID;
		this.replicationDegree = replicationDegree;
		this.numChunks = numChunks;
	}

	public String getFileID() {
		return fileID;
	}

	public int getReplicationDegree() {
		return replicationDegree;
	}

	public int getNumChunks() {
		return numChunks;
	}

}


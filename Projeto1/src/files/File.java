package files;

import java.util.HashMap;

public class File {
	
	private String fileID;
	private int replicationDegree;
	private HashMap<Integer, Chunk> chunks = new  HashMap<Integer, Chunk>();
	
	public File(String fileID, int replicationDegree) {
		this.fileID = fileID;
		this.replicationDegree = replicationDegree;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public int getNumChunks() {
		return chunks.size();
	}

	public int getReplicationDegree() {
		return replicationDegree;
	}

	public void setReplicationDegree(int replicationDegree) {
		this.replicationDegree = replicationDegree;
	}

}

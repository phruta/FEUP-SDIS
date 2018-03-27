package files;

import java.util.ArrayList;


public class Chunk {
	public static final int MAX_SIZE = 64000;

	private int chunkNo;
	
	private String fileID;

	private int replicationDegree;

	private byte[] body;
	
	private ArrayList<Integer> peersID =  new ArrayList<>();
	

	public Chunk(int chunkNo, String fileID, int replicationDegree, byte[] body) {
		super();
		this.chunkNo = chunkNo;
		this.fileID = fileID;
		this.replicationDegree = replicationDegree;
		this.body = body;
	}
	
	public Chunk(int chunkNo, String fileID) {
		super();
		this.chunkNo = chunkNo;
		this.fileID = fileID;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chunkNo;
		result = prime * result + ((fileID == null) ? 0 : fileID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chunk other = (Chunk) obj;
		if (chunkNo != other.chunkNo)
			return false;
		if (fileID == null) {
			if (other.fileID != null)
				return false;
		} else if (!fileID.equals(other.fileID))
			return false;
		return true;
	}

	public int getChunkNo() {
		return chunkNo;
	}

	public void setChunkNo(int chunkNo) {
		this.chunkNo = chunkNo;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public int getReplicationDegree() {
		return replicationDegree;
	}

	public void setReplicationDegree(int replicationDegree) {
		this.replicationDegree = replicationDegree;
	}

	public byte[] getData() {
		return body;
	}

	public void setData(byte[] data) {
		this.body = data;
	}

	public static int getMaxSize() {
		return MAX_SIZE;
	}
	
	public void addPeer(int PeerID) {
		if(!peersID.contains(PeerID))
			peersID.add(PeerID);
	}
	
	public void removePeer(int PeerID) {
		peersID.remove(PeerID);
	}
	
	public int peesrIDsSize() {
		return peersID.size();
	}
}

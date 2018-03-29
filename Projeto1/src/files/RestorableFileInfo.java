package files;

import java.util.HashMap;
import java.util.HashSet;

public class RestorableFileInfo {
	private HashMap<Integer,HashSet<String>> chunk_Peers = new HashMap<>();
	private String fileID;
	private String fileName;
	private int replicationDegree;
	private int numChunks;
	
	
	@Override
	public String toString() {
		return "FileInfo [fileID=" + fileID + ", replicationDegree=" + replicationDegree + ", numChunks=" + numChunks
				+ "]";
	}

	public RestorableFileInfo(String fileID, String fileName, int replicationDegree, int numChunks) {
		this.fileID = fileID;
		this.fileName = fileName;
		this.replicationDegree = replicationDegree;
		this.numChunks = numChunks;
	}

	public String getFileID() {
		return fileID;
	}
	public String getFileName() {
		return fileName;
	}

	public int getReplicationDegree() {
		return replicationDegree;
	}

	public int getNumChunks() {
		return numChunks;
	}
	
	public boolean addChunk_Peer(int chunkNo, String PeerID) {
		if(chunk_Peers.containsKey(chunkNo)) {
			return chunk_Peers.get(chunkNo).add(PeerID);
		}else {
			HashSet <String> tempHashSet= new HashSet<>();
			tempHashSet.add(PeerID);
			chunk_Peers.put(chunkNo, tempHashSet);
			return true;
		}
	}
	
	public int getChunk_PeerSize(int chunkNo) {
		return chunk_Peers.get(chunkNo).size()+1;
	}

}


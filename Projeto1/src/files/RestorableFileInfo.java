package files;


import java.io.Serializable;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class RestorableFileInfo implements Serializable{

	private static final long serialVersionUID = 5646836793796622676L;
	
	private ConcurrentHashMap<Integer,HashSet<String>> chunk_Peers = new ConcurrentHashMap<>();
	private String fileID;
	private String fileName;
	private int replicationDegree;
	private int numChunks;
	
	
	@Override
	public String toString() {
		String str = "File [fileID=" + fileID + ", desired replication degree=" + Integer.toString(replicationDegree) + ", numChunks=" + Integer.toString(numChunks)
				+ "]\n";
		for (Integer i : chunk_Peers.keySet()) {
			str += "[chunkNo=" + Integer.toString(i)+ ", perceived replication degree=" + Integer.toString(chunk_Peers.get(i).size())+ "]\n";
		}
		return str;
	}

	public RestorableFileInfo(String fileID, String fileName, int replicationDegree, int numChunks) {
		this.fileID = fileID;
		this.fileName = fileName;
		this.replicationDegree = replicationDegree;
		this.numChunks = numChunks;
	}

	public  String getFileID() {
		return fileID;
	}
	public  String getFileName() {
		return fileName;
	}

	public  int getReplicationDegree() {
		return replicationDegree;
	}

	public int getNumChunks() {
		return numChunks;
	}
	
	public synchronized boolean addChunk_Peer(int chunkNo, String PeerID) {
		if(chunk_Peers.containsKey(chunkNo)) {
			return chunk_Peers.get(chunkNo).add(PeerID);
		}else {
			HashSet <String> tempHashSet= new HashSet<>();
			tempHashSet.add(PeerID);
			chunk_Peers.put(chunkNo, tempHashSet);
			return true;
		}
	}
	
	public synchronized int getChunk_PeerSize(int chunkNo) {
		HashSet<String> temp;
		if((temp=chunk_Peers.get(chunkNo))!=null)
			return temp.size();
		return 0;
	}

	public synchronized boolean removeChunkPeer(int chunkNo, String peerID) {
		if(chunk_Peers.containsKey(chunkNo)) {
			return chunk_Peers.get(chunkNo).remove(peerID);
		}
		return false;
	}

}


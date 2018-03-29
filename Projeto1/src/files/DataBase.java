package files;


import java.util.ArrayList;
import java.util.HashMap;


public class DataBase {
    private volatile ArrayList<Chunk> chunks = new ArrayList<Chunk>();
    private volatile HashMap<String, RestorableFileInfo> restorableFiles = new HashMap<>();

	public DataBase() {
		super();
	}
	
	public synchronized boolean hasChunk(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo,fileID);
		return chunks.contains(tempChunk);
	}
	
	public synchronized boolean addChunk(int chunkNo, String fileID, int replicationDegree, byte[] body) {
		Chunk tempChunk = new Chunk(chunkNo, fileID, replicationDegree, body);
		boolean to_return= chunks.contains(tempChunk);
		
		if(!to_return)
			chunks.add(tempChunk);
		
		return !to_return;		
	}
	
	public synchronized boolean removeChunk(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo,fileID);
		return chunks.remove(tempChunk);
	}
	
	public synchronized boolean addChunkPeerID(int chunkNo, String fileID, String peerID) {
		Chunk tempChunk = new Chunk(chunkNo,fileID);
		for(Chunk chunk:chunks) {
			if(chunk.equals(tempChunk)) {
				chunk.addPeer(peerID);
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean removeChunkPeerID(int chunkNo, String fileID, String peerID) {
		Chunk tempChunk = new Chunk(chunkNo,fileID);
		for(Chunk chunk:chunks) {
			if(chunk.equals(tempChunk)) {
				chunk.removePeer(peerID);
				return true;
			}
		}
		return false;
	}
	
	public synchronized int getChunkReplicationDegree(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo,fileID);
		for(Chunk chunk:chunks) {
			if(chunk.equals(tempChunk)) {
				return chunk.getReplicationDegree();
			}
		}
		return -1;
	}
	
	public synchronized int getChunkPeesrIDsSize(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo,fileID);
		for(Chunk chunk:chunks) {
			if(chunk.equals(tempChunk)) {
				return chunk.peesrIDsSize();
			}
		}
		return -1;
	}
	
	public synchronized void addRestorableFile(String fileID,RestorableFileInfo fileInfo) {
		restorableFiles.put(fileID, fileInfo);
	}
	
	public synchronized void removeRestorableFile(String fileID) {
		restorableFiles.remove(fileID);
	}
	
	public synchronized boolean containsRestorableFile(String fileID) {
		return restorableFiles.containsKey(fileID);
	}
	
	public synchronized RestorableFileInfo getRestorableFileInformation(String fileID) {
		return restorableFiles.get(fileID);
	}
	
	public synchronized boolean addChunkPeer_RetorableFile(int chunkNo, String fileID, String PeerID) {
			return restorableFiles.get(fileID).addChunk_Peer(chunkNo, PeerID);
	}
	
	public  synchronized int getChunkPeerSize_RetorableFile(int chunkNo, String fileID) {
		return restorableFiles.get(fileID).getChunk_PeerSize(chunkNo);
	}
}

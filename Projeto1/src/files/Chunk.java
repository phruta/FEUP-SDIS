package files;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.concurrent.CopyOnWriteArrayList;

import Server.Peer;
import utils.Utils;


public class Chunk implements Serializable {

	private static final long serialVersionUID = -669885110003358924L;

	public static final int MAX_SIZE = 64000;

	private int chunkNo;
	
	private String fileID;

	private int replicationDegree;

	private int bodySize;
	
	private String pathToChunk;
	
	private CopyOnWriteArrayList<String> peersID =  new CopyOnWriteArrayList<>();
	

	public Chunk(int chunkNo, String fileID, int replicationDegree, byte[] body) {
		super();
		this.chunkNo = chunkNo;
		this.fileID = fileID;
		this.replicationDegree = replicationDegree;
		this.bodySize = body.length;
		this.pathToChunk="./Chunks/"+Peer.peerID+"-"+fileID+"-"+Integer.toString(chunkNo)+".chunk";
		this.saveChunk(body);
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


	public String getFileID() {
		return fileID;
	}


	public int getReplicationDegree() {
		return replicationDegree;
	}


	public byte[] getData() {
			File f= new File(pathToChunk);
			return Utils.getFileData(f);
	}
	
	public int getDataSize() {
		return bodySize;
	}

	@Override
	public String toString() {
		return "Chunk [chunkNo=" + Integer.toString(chunkNo)+ ", fileID=" + fileID + ", perceived replication degree=" + Integer.toString(peesrIDsSize())+ "]";
	}

	public static int getMaxSize() {
		return MAX_SIZE;
	}
	
	public void addPeer(String PeerID) {
		if(!peersID.contains(PeerID))
			peersID.add(PeerID);
	}
	
	public void removePeer(String PeerID) {
		peersID.remove(PeerID);
	}
	
	public int peesrIDsSize() {
		return peersID.size()+1;
	}
	
	private boolean saveChunk(byte[] data) {	
		try {
			File f= new File(pathToChunk);
			f.getParentFile().mkdirs();
			Files.write(f.toPath(), data);
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
			return false;
		
		}
		return true;
	}
	
	public boolean removeBodyData() {
		File f= new File(pathToChunk);
		return f.delete();
	}
}

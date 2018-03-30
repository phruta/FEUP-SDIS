package multicastChannels;


import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import utils.Pair;




public class MDR extends Channel {
	private volatile CopyOnWriteArrayList<Pair<String,Integer>> restoredChunks = new CopyOnWriteArrayList<>();
	
	public MDR(InetAddress address, int port) {
		super(address, port);
	}
	
	public synchronized void addRestoredChunk(String fileID, int chunkNo) {
		if(!containsRestoredChunk(fileID,chunkNo))
			restoredChunks.add(new Pair<String, Integer>(fileID,chunkNo));
	}
	
	public synchronized boolean containsRestoredChunk(String fileID, int chunkNo) {
		return restoredChunks.contains(new Pair<String, Integer>(fileID,chunkNo));
	}
	
	public synchronized void removeRestoredChunk(String fileID, int chunkNo) {
		restoredChunks.remove(new Pair<String, Integer>(fileID,chunkNo));
	}
	

}

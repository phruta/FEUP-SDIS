package files;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import Server.Peer;
import utils.Pair;
import utils.Utils;

public class DataBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7778786538094336934L;

	public final static String DATABASE_PATH = "../Databases/";
	public final static String DB = ".cdb";

	private volatile CopyOnWriteArrayList<Chunk> chunks = new CopyOnWriteArrayList<>();
	private volatile ConcurrentHashMap<String, RestorableFileInfo> restorableFiles = new ConcurrentHashMap<>();
	private volatile CopyOnWriteArrayList<RestoredFile> filesRestored = new CopyOnWriteArrayList<>();
	private volatile ConcurrentHashMap<Chunk, Boolean> removedPutChunks = new ConcurrentHashMap<>();
	private volatile CopyOnWriteArrayList<Pair<String, Integer>> restoredChunks = new CopyOnWriteArrayList<>();
	private volatile static DataBase db = null;

	public static DataBase getInstance() {
		if (db == null) {
			loadDatabase();
		}
		return db;
	}

	private static void loadDatabase() {
		if ((db = (DataBase) Utils.load(DATABASE_PATH + Peer.peerID + DB)) == null) {
			db = new DataBase();
			saveDatabase();
		}
		db.clearFilesRestored();
		db.clearRemovedPutChunks();
		db.clearRestoredChunks();
	}

	private static void saveDatabase() {
		Utils.save(db, DATABASE_PATH + Peer.peerID + DB);
	}

	private DataBase() {
		super();
	}

	public synchronized boolean hasChunk(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		return chunks.contains(tempChunk);
	}

	public synchronized boolean addChunk(int chunkNo, String fileID, int replicationDegree, byte[] body) {
		Chunk tempChunk = new Chunk(chunkNo, fileID, replicationDegree, body);
		boolean to_return = chunks.contains(tempChunk);

		if (!to_return) {
			chunks.add(tempChunk);
			DiskSpace.getInstance().addUsedSpace(body.length);
		}

		saveDatabase();
		return !to_return;
	}

	public synchronized boolean removeChunk(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		tempChunk.removeBodyData();
		boolean to_return = chunks.remove(tempChunk);
		saveDatabase();
		return to_return;
	}

	public synchronized void removeChunksByFileID(String fileID) {
		for (Chunk chunk : chunks) {
			if (chunk.getFileID().equals(fileID)) {
				DiskSpace.getInstance().removeUsedSpace(chunk.getDataSize());
				chunk.removeBodyData();
				chunks.remove(chunk);
			}
		}
		saveDatabase();
	}

	public synchronized boolean addChunkPeerID(int chunkNo, String fileID, String peerID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		for (Chunk chunk : chunks) {
			if (chunk.equals(tempChunk)) {
				chunk.addPeer(peerID);
				saveDatabase();
				return true;
			}
		}
		return false;
	}

	public synchronized boolean removeChunkPeerID(int chunkNo, String fileID, String peerID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		for (Chunk chunk : chunks) {
			if (chunk.equals(tempChunk)) {
				chunk.removePeer(peerID);
				saveDatabase();
				return true;
			}
		}
		return false;
	}

	public synchronized Chunk getChunk(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		for (Chunk chunk : chunks) {
			if (chunk.equals(tempChunk)) {
				return chunk;
			}
		}
		return null;
	}

	public synchronized Pair<Integer, String> saveDiskSpaceRemove() {
		for (Chunk chunk : chunks) {
			if (chunk.getReplicationDegree() < chunk.peesrIDsSize()) {
				return new Pair<Integer, String>(chunk.getChunkNo(), chunk.getFileID());
			}
		}
		for (Chunk chunk : chunks) {
			if (chunk.getReplicationDegree() == chunk.peesrIDsSize()) {
				return new Pair<Integer, String>(chunk.getChunkNo(), chunk.getFileID());
			}
		}
		if (!chunks.isEmpty())
			return new Pair<Integer, String>(chunks.get(0).getChunkNo(), chunks.get(0).getFileID());

		return null;
	}

	public synchronized int getChunkDataSize(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		for (Chunk chunk : chunks) {
			if (chunk.equals(tempChunk)) {
				return chunk.getDataSize();
			}
		}
		return 0;
	}

	public synchronized int getChunkReplicationDegree(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		for (Chunk chunk : chunks) {
			if (chunk.equals(tempChunk)) {
				return chunk.getReplicationDegree();
			}
		}
		return -1;
	}

	public synchronized int getChunkPeesrIDsSize(int chunkNo, String fileID) {
		Chunk tempChunk = new Chunk(chunkNo, fileID);
		for (Chunk chunk : chunks) {
			if (chunk.equals(tempChunk)) {
				return chunk.peesrIDsSize();
			}
		}
		return -1;
	}

	public synchronized void addRestorableFile(String fileID, RestorableFileInfo fileInfo) {
		restorableFiles.put(fileID, fileInfo);
		saveDatabase();
	}

	public synchronized void removeRestorableFile(String fileID) {
		restorableFiles.remove(fileID);
		saveDatabase();
	}

	public synchronized boolean containsRestorableFile(String fileID) {
		return restorableFiles.containsKey(fileID);
	}

	public synchronized RestorableFileInfo getRestorableFileInformation(String fileID) {
		return restorableFiles.get(fileID);
	}

	public synchronized boolean addChunkPeer_RetorableFile(int chunkNo, String fileID, String PeerID) {
		boolean to_return = restorableFiles.get(fileID).addChunk_Peer(chunkNo, PeerID);
		saveDatabase();
		return to_return;
	}

	public synchronized boolean removeChunkPeer_RetorableFile(int chunkNo, String fileID, String PeerID) {
		boolean to_return = restorableFiles.get(fileID).removeChunkPeer(chunkNo, PeerID);
		saveDatabase();
		return to_return;
	}

	public synchronized int getChunkPeerSize_RetorableFile(int chunkNo, String fileID) {
		return restorableFiles.get(fileID).getChunk_PeerSize(chunkNo);
	}

	public synchronized RestorableFileInfo getRestorableFileByFilename(String filename) {
		for (String key : restorableFiles.keySet()) {
			if (restorableFiles.get(key).getFileName().equals(filename))
				return restorableFiles.get(key);
		}
		return null;
	}

	public synchronized boolean containsRestoredFile(String fileID) {
		return filesRestored.contains(new RestoredFile(fileID));
	}

	public synchronized boolean addRestoredFile(String fileID, String filename) {
		return filesRestored.add(new RestoredFile(fileID, filename));
	}

	public synchronized RestoredFile getRestoredFile(String fileID) {
		for (RestoredFile rf : filesRestored) {
			if (rf.equals(new RestoredFile(fileID)))
				return rf;
		}
		return null;
	}

	public synchronized boolean removeRestoredFile(RestoredFile file) {
		return filesRestored.remove(file);
	}

	public synchronized void addChunkRemovedPutChunk(Chunk chunk) {
		if (!removedPutChunks.containsKey(chunk))
			this.removedPutChunks.put(chunk, false);
	}

	public synchronized void setTrueChunkRemovedPutChunk(Chunk chunk) {
		if (removedPutChunks.containsKey(chunk))
			this.removedPutChunks.put(chunk, true);
	}

	public synchronized boolean getChunkRemovedPutChunk(Chunk chunk) {
		return this.removedPutChunks.get(chunk);
	}

	public synchronized void removeChunkRemovedPutChunk(Chunk chunk) {
		removedPutChunks.remove(chunk);
	}

	public void clearRemovedPutChunks() {
		removedPutChunks = new ConcurrentHashMap<>();
	}

	public void clearFilesRestored() {
		filesRestored = new CopyOnWriteArrayList<>();
	}

	public void clearRestoredChunks() {
		restoredChunks = new CopyOnWriteArrayList<>();
	}

	public synchronized void addRestoredChunk(String fileID, int chunkNo) {
		if (!containsRestoredChunk(fileID, chunkNo))
			restoredChunks.add(new Pair<String, Integer>(fileID, chunkNo));
	}

	public synchronized boolean containsRestoredChunk(String fileID, int chunkNo) {
		return restoredChunks.contains(new Pair<String, Integer>(fileID, chunkNo));
	}

	public synchronized void removeRestoredChunk(String fileID, int chunkNo) {
		restoredChunks.remove(new Pair<String, Integer>(fileID, chunkNo));
	}

	@Override
	public String toString() {
		String str = "Files whose backup this Peer has initiated:";
		for (String i : restorableFiles.keySet())
			str += "\n" + restorableFiles.get(i).toString();
		str += "\n\nStored Chunks:\n";

		for (Chunk i : chunks)
			str += i.toString() + "\n";
		return str;
	}

}

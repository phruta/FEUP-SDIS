package protocol;

import Server.Peer;
import utils.Utils;

public class BackupChunk implements Runnable {
	private static final int DEFAULT_SLEEP_TIME = 1000;
	private static final int TRYS_PUTCHUNK_NUMBER = 5;
	
	private int chunkNo;
	private int replicationDegree;
	private String fileId;
	private byte[] message;

	
	public BackupChunk(int chunkNo, int replicationDegree, String fileId, byte[] message) {
		super();
		this.chunkNo = chunkNo;
		this.replicationDegree = replicationDegree;
		this.fileId = fileId;
		this.message = message;
	}

	@Override
	public void run() {

		for(int j=1; j<TRYS_PUTCHUNK_NUMBER+1;j++) {
			Utils.threadSleep(DEFAULT_SLEEP_TIME*j);
			if(Peer.getDb().getChunkPeerSize_RetorableFile(chunkNo, fileId)<replicationDegree) {
				Peer.MulticastChannels[Peer.MDB_CHANNEL].send(message);
			}
			else break;
		}

		
	}

}

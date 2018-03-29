package protocol;

import java.io.File;
import java.util.Arrays;

import Server.Peer;
import files.Chunk;
import files.RestorableFileInfo;
import utils.Convert;
import utils.HeaderCreater;

public class Backup implements Runnable{
	private static final int DEFAULT_SLEEP_TIME = 1000;
	private static final int TRYS_PUTCHUNK_NUMBER = 5;
	private File file;
	private int replicationDegree;

	public Backup(File file, int replicationDegree) {
		this.file = file;
		this.replicationDegree = replicationDegree;
	}

	@Override
	public void run() {
		String fileId = Convert.getFileId(file);
			
		byte[] data = Convert.getFileData(file);
		
		int numChunks= data.length/Chunk.MAX_SIZE + 1;
		
		Peer.db.addRestorableFile(fileId, 
				new RestorableFileInfo(fileId,file.getName(),replicationDegree, numChunks));
		
		for (int i=0; i<numChunks;i++) {
			byte [] chunkdata;
			int indexStart= Chunk.MAX_SIZE*i;
			if(indexStart+Chunk.MAX_SIZE<data.length) {		
				chunkdata = Arrays.copyOfRange(data,indexStart,indexStart+Chunk.MAX_SIZE);
			}else if(indexStart<data.length) {
				chunkdata=Arrays.copyOfRange(data,indexStart,data.length);
			}else {
				chunkdata= new byte[0];
			}
			byte[] message= Convert.concatenateArrays(HeaderCreater.putChunk(fileId, i, replicationDegree), chunkdata);
			Peer.MulticastChannels[Peer.MDB_CHANNEL].send(message);
			

			for(int j=1; j<TRYS_PUTCHUNK_NUMBER+1;j++) {
				try {
					Thread.sleep(DEFAULT_SLEEP_TIME*j);
				} catch (InterruptedException e) {
					e.getMessage();
					e.printStackTrace();
				}
				if(Peer.db.getChunkPeerSize_RetorableFile(i, fileId)<replicationDegree) {
					Peer.MulticastChannels[Peer.MDB_CHANNEL].send(message);
				}
				else break;
			}
		}
		
	}
	
	

}

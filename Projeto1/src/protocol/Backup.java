package protocol;

import java.io.File;
import java.util.Arrays;

import Server.Peer;
import files.Chunk;
import files.RestorableFileInfo;
import utils.Utils;
import utils.HeaderCreater;

public class Backup implements Runnable{
	private static final int DEFAULT_SLEEP_TIME = 1000;
	private File file;
	private int replicationDegree;

	public Backup(File file, int replicationDegree) {
		this.file = file;
		this.replicationDegree = replicationDegree;
	}

	@Override
	public void run() {
		
		String fileId = Utils.getFileId(file);
		System.out.println("Sending PutChunks for File id: " +fileId);
		
		byte[] data = Utils.getFileData(file);
		
		int numChunks= data.length/Chunk.MAX_SIZE + 1;
		
		Peer.getDb().addRestorableFile(fileId, 
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
			byte[] message= Utils.concatenateArrays(HeaderCreater.putChunk(fileId, i, replicationDegree), chunkdata);	
	
			new Thread(new SendChunk(i, replicationDegree, fileId, message)).start();
			if(i%16==0) {
				Utils.threadSleep(DEFAULT_SLEEP_TIME);
			}
		}

		Peer.saveDatabases();
		return;
	}
	
	

}

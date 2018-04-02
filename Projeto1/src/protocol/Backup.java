package protocol;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Server.Peer;
import files.Chunk;
import files.RestorableFileInfo;
import utils.Utils;
import utils.HeaderCreater;

public class Backup implements Runnable{
	private File file;
	private int replicationDegree;
	private ExecutorService threadpool;

	public Backup(File file, int replicationDegree) {
		this.file = file;
		this.replicationDegree = replicationDegree;
		threadpool=Executors.newFixedThreadPool(16);
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
	
			threadpool.execute(new SendChunk(i, replicationDegree, fileId, message));
			
		}

		Peer.saveDatabases();
		return;
	}
	
	

}

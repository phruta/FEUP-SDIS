package protocol;

import java.io.File;

import Server.Peer;
import files.Chunk;
import files.RestorableFileInfo;
import files.RestoredFile;
import utils.Utils;
import utils.HeaderCreater;

public class Restore implements Runnable{
	private static final int DEFAULT_SLEEP_TIME = 1000;
	File file;
	String fileID;
	public Restore(File file) {
		super();
		this.file = file;
	}
	@Override
	public void run() {
		fileID = Utils.getFileId(file);
		String fileName = file.getName();
		int numChunks= Utils.getFileData(file).length/Chunk.MAX_SIZE + 1;
		
		System.out.println("Restoring file with the file id: " +fileID);
		RestorableFileInfo fileToRestore;
		
		if (((fileToRestore = Peer.getDb().getRestorableFileInformation(fileID)) != null)
				|| ((fileToRestore = Peer.getDb().getRestorableFileByFilename(file.getName())) != null)) {
			fileID = fileToRestore.getFileID();
			fileName = fileToRestore.getFileName();
			numChunks = fileToRestore.getNumChunks();
		} else
			System.out.println(
					"WARNING: This Peer didn't initiate the backup for this file, there's no guarantee for this restore");

		
		
		Peer.getDb().addRestoredFile(fileID,fileName);
		
		for (int i=0; i<numChunks;i++) {
			byte[] message= HeaderCreater.getChunk(fileID, i);
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(message);
			Utils.threadSleep(50);
		}
		Utils.threadSleep(DEFAULT_SLEEP_TIME);
		
		
		RestoredFile restoredFile=Peer.getDb().getRestoredFile(fileID);
		
		for(int i=0; i<3;i++) {
			if(restoredFile.dataSize()==numChunks) {
				if(!restoredFile.saveFile())
					System.out.print("ERROR: Couldn't save the file properly, try again");
				if(!Peer.getDb().removeRestoredFile(restoredFile))
					System.out.print("ERROR: Couldn't remove restoredFile from database");
				System.out.print("Restore Successful");
				return;
			}
		Utils.threadSleep(DEFAULT_SLEEP_TIME);		
		}
		System.out.print("ERROR: Couldn't restore the file properly, try again");
		Peer.saveDatabases();
	}
	
	

}

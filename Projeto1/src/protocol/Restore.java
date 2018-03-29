package protocol;

import java.io.File;

import Server.Peer;
import files.RestorableFileInfo;
import files.RestoredFile;
import utils.Convert;
import utils.HeaderCreater;

public class Restore implements Runnable{
	File file;
	String fileID;
	public Restore(File file) {
		super();
		this.file = file;
	}
	@Override
	public void run() {
		fileID = Convert.getFileId(file);
		RestorableFileInfo fileToRestore;
		
		if((fileToRestore=Peer.db.getRestorableFileInformation(fileID))==null) {
			if((fileToRestore=Peer.db.getRestorableFileByFilename(file.getName()))==null)
			{
				System.out.println("ERROR: This Peer cannot restore the file" + file.getName());
				return;
			}else
				fileID=fileToRestore.getFileID();
		}

		
		
		for (int i=0; i<fileToRestore.getNumChunks();i++) {
			byte[] message= HeaderCreater.getChunk(fileID, i);
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(message);
			threadSleep(50);
		}
		threadSleep(1000);
		RestoredFile restoredFile=Peer.db.getRestoredFile(fileID);
		
		for(int i=0; i<3;i++) {
			if(restoredFile.dataSize()==fileToRestore.getNumChunks()) {
				if(!restoredFile.saveFile())
					System.out.print("ERROR: Couldn't save the file properly, try again");
				if(!Peer.db.removeRestoredFile(restoredFile))
					System.out.print("ERROR: Couldn't remove file from database");
				return;
			}
		threadSleep(1000);		
		}
		System.out.print("ERROR: Couldn't restore the file properly, try again");
	}
	
	private void threadSleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

}

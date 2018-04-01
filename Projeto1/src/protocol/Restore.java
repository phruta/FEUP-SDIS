package protocol;

import java.io.File;

import Server.Peer;
import files.Chunk;
import files.DataBase;
import files.RestorableFileInfo;
import files.RestoredFile;
import utils.Utils;
import utils.HeaderCreater;

public class Restore implements Runnable {
	File file;
	String fileID;

	public Restore(File file) {
		super();
		this.file = file;
	}

	@Override
	public void run() {
		String fileName = file.getName();
		int numChunks = Utils.getFileData(file).length / Chunk.MAX_SIZE + 1;
		fileID = Utils.getFileId(file);

		System.out.println("Restoring file with the file id: " + fileID);
		RestorableFileInfo fileToRestore;

		if (((fileToRestore = DataBase.getInstance().getRestorableFileInformation(fileID)) != null)
				|| ((fileToRestore = DataBase.getInstance().getRestorableFileByFilename(file.getName())) != null)) {
			fileID = fileToRestore.getFileID();
			fileName = fileToRestore.getFileName();
			numChunks = fileToRestore.getNumChunks();
		} else
			System.out.println(
					"WARNING: This Peer didn't initiate the backup for this file, there's no guarantee for this restore");

		DataBase.getInstance().addRestoredFile(fileID, fileName);

		for (int i = 0; i < numChunks; i++) {
			byte[] message = HeaderCreater.getChunk(fileID, i);
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(message);
			Utils.threadSleep(100);
		}
		Utils.threadSleep(1000);

		RestoredFile restoredFile = DataBase.getInstance().getRestoredFile(fileID);

		for (int i = 0; i < 3; i++) {
			if (restoredFile.dataSize() == numChunks) {
				if (!restoredFile.saveFile())
					System.out.print("ERROR: Couldn't save the file properly, try again");
				if (!DataBase.getInstance().removeRestoredFile(restoredFile))
					System.out.print("ERROR: Couldn't remove file from database");
				System.out.print("Restore Completed\n");
				return;
			}
			Utils.threadSleep(1000);
		}
		System.out.print("ERROR: Couldn't restore the file properly, try again");
	}

}

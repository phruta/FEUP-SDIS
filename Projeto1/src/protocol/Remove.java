package protocol;

import java.io.File;

import Server.Peer;
import utils.Convert;
import utils.HeaderCreater;

public class Remove implements Runnable {
	
	private File file;
	public Remove(File file) {
		super();
		this.file = file;
	}
	@Override
	public void run() {
		String fileID = Convert.getFileId(file);
		Peer.db.removeRestorableFile(fileID);
		Peer.db.removeChunksByFileID(fileID);
		
		Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.delete(fileID));
	}

}

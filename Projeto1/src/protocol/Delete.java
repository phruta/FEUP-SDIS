package protocol;

import java.io.File;

import Server.Peer;
import files.DataBase;
import utils.Utils;
import utils.HeaderCreater;

public class Delete implements Runnable {

	private File file;

	public Delete(File file) {
		super();
		this.file = file;
	}

	@Override
	public void run() {
		String fileID = Utils.getFileId(file);
		DataBase.getInstance().removeRestorableFile(fileID);
		DataBase.getInstance().removeChunksByFileID(fileID);

		Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.delete(fileID));
		System.out.println("Deleted File with the File ID: " + fileID);
	}

}

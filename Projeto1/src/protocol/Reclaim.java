package protocol;

import Server.Peer;
import files.DataBase;
import files.DiskSpace;
import utils.HeaderCreater;
import utils.Pair;

public class Reclaim implements Runnable {
	int capacity;

	public Reclaim(int capacity) {
		super();
		this.capacity = capacity;
	}

	@Override
	public void run() {
		DiskSpace.getInstance().setCapacitySpace(capacity);
		System.out.println("The space is set to " +Integer.toString(capacity)+" removing chunks in the database if needed");
		while(DiskSpace.getInstance().getSpaceLeft()<0) {
			Pair<Integer,String> temp= DataBase.getInstance().saveDiskSpaceRemove();
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.removed(temp.getValue(), temp.getKey()));
			DiskSpace.getInstance().removeUsedSpace(DataBase.getInstance().getChunkDataSize(temp.getKey(), temp.getValue()));
			DataBase.getInstance().removeChunk(temp.getKey(), temp.getValue());
			System.out.println("Removed chunk\nFile id: " + temp.getValue()+ "\nChunk Number"+ temp.getKey().toString());
		}
	}

}

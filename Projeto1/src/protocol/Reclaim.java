package protocol;

import Server.Peer;
import utils.HeaderCreater;
import utils.Pair;
import utils.Utils;

public class Reclaim implements Runnable {
	int capacity;

	public Reclaim(int capacity) {
		super();
		this.capacity = capacity;
	}

	@Override
	public void run() {
		Peer.getDs().setCapacitySpace(capacity);
		System.out.println("The space is set to " +Integer.toString(capacity)+" removing chunks in the database if needed");
		while((Peer.getDs().getCapacitySpace() - Peer.getDb().chunksDataSize()) <0) {
			Pair<Integer,String> temp= Peer.getDb().saveDiskSpaceRemove();
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.removed(temp.getValue(), temp.getKey()));
			Peer.getDb().removeChunk(temp.getKey(), temp.getValue());
			System.out.println("Removed chunk\nFile id: " + temp.getValue()+ "\nChunk Number"+ temp.getKey().toString());
			Utils.threadSleep(50);
		}
		Peer.saveDatabases();
	}

}

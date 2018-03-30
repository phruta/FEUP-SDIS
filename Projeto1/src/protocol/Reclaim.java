package protocol;

import Server.Peer;
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
		Peer.ds.setCapacitySpace(capacity);
		
		while(Peer.ds.getSpaceLeft()<0) {
			Pair<Integer,String> temp= Peer.db.saveDiskSpaceRemove();
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.removed(temp.getValue(), temp.getKey()));
			Peer.ds.removeUsedSpace(Peer.db.getChunkDataSize(temp.getKey(), temp.getValue()));
			Peer.db.removeChunk(temp.getKey(), temp.getValue());
		}
	}

}

package protocol;

import Server.Peer;

public class State implements Runnable {

	@Override
	public void run() {
		String str= Peer.db.toString()+ "\n" + Peer.ds.toString();
		System.out.print(str);
	}

}

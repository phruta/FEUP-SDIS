package protocol;

import Server.Peer;

public class State implements Runnable {

	@Override
	public void run() {
		String str= Peer.getDb().toString()+ "\n" + Peer.getDs().toString() + "-" +Peer.getDb().chunksDataSize();
		System.out.print("\033[H\033[2J");  
	    System.out.flush();  
		System.out.print(str);
	}

}

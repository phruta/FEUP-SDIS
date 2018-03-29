package multicastChannels;


import java.net.InetAddress;


public class MDB extends Channel {

	public MDB(InetAddress address, int port) {
		super(address, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addRestoredChunk(String fileID, int chunkNo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsRestoredChunk(String fileID, int chunkNo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeRestoredChunk(String fileID, int chunkNo) {
		// TODO Auto-generated method stub
		
	}


}

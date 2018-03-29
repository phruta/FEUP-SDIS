package multicastChannels;


import java.net.InetAddress;


public class MC extends Channel {

	public MC(InetAddress address, int port) {
		super(address, port);
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

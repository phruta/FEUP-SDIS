package utils;

import Server.Peer;

public class HeaderCreater extends MessageType {
	private static final String SPACE = " ";
	public static final String CRLF = "\r\n";
	
	public static byte[] putChunk(String fileId, int chunkNo, int replicationDeg) {
		String Header = PUTCHUNK + SPACE + Peer.version + SPACE + Peer.peerID + SPACE + fileId + SPACE
				+ Integer.toString(chunkNo) + SPACE + Integer.toString(replicationDeg) + SPACE + CRLF + CRLF;

		return Header.getBytes();
	}
	
	public static byte[] stored(String fileId, String chunkNo) {
		String Header = STORED + SPACE + Peer.version + SPACE + Peer.peerID + SPACE + fileId + SPACE
				+ chunkNo + SPACE + CRLF + CRLF;

		return Header.getBytes();
	}
	
	public static byte[] getChunk(String fileId, int chunkNo) {
		String Header = GETCHUNK + SPACE + Peer.version + SPACE + Peer.peerID + SPACE + fileId + SPACE
				+ chunkNo + SPACE + CRLF + CRLF;

		return Header.getBytes();
	}
	
	public static byte[] chunk(String fileId, int chunkNo) {
		String Header = CHUNK + SPACE + Peer.version + SPACE + Peer.peerID + SPACE + fileId + SPACE
				+ Integer.toString(chunkNo) + SPACE + CRLF + CRLF;

		return Header.getBytes();
	}
	
	public static byte[] delete(String fileId) {
		String Header = DELETE + SPACE + Peer.version + SPACE + Peer.peerID + SPACE + fileId + SPACE + CRLF + CRLF;

		return Header.getBytes();
	}
	
	public static byte[] removed(String fileId, int chunkNo) {
		String Header = REMOVED + SPACE + Peer.version + SPACE + Peer.peerID + SPACE + fileId + SPACE
				+ Integer.toString(chunkNo) + SPACE + CRLF + CRLF;

		return Header.getBytes();
	}
}

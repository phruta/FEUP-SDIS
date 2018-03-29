package utils;

import Server.Peer;

public class HeaderCreater {
	private static final String SPACE = " ";
	public static final String CRLF = "\r\n";
	private static final String PUTCHUNK = "PUTCHUNK";
	private static final String STORED = "STORED";

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
}
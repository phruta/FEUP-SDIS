package protocol;

import java.net.DatagramPacket;
import java.util.Random;

import utils.HeaderCreater;
import Server.Peer;


public class MulticastHandler implements Runnable {
	public static final int HEADER = 0;
	public static final int BODY = 0;

	public static final int MESSAGE_TYPE = 0;
	public static final int VERSION = 1;
	public static final int SENDER_ID = 2;
	public static final int FILE_ID = 3;
	public static final int CHUNK_NO = 4;
	public static final int REPLICATION_DEG = 5;

	DatagramPacket packet;

	public MulticastHandler(DatagramPacket packet) {
		super();
		this.packet = packet;
	}

	@Override
	public void run() {
		String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
		;
		String[] header_body = data.split(HeaderCreater.CRLF + HeaderCreater.CRLF, 2);
		String[] header = header_body[HEADER].split(" ");

		if (header[SENDER_ID].equals(Peer.peerID))
			return;

		switch (header[MESSAGE_TYPE].toUpperCase()) {
		case "PUTCHUNK":
			handlePutchunk(header, header_body[BODY]);
		case "STORED":
			handleStored(header);
		default:
			System.out.println("Not a valid Protocol");
		}
	}

	private void handleStored(String[] header) {
		boolean problem = true;
		
		if (Peer.db.containsRestorableFile(header[FILE_ID])) {
			problem = Peer.db.addChunkPeer_RetorableFile(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
					header[SENDER_ID]);
		} else {
			try {
				Thread.sleep(400);
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
			if (Peer.db.hasChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID])) {
				problem = Peer.db.addChunkPeerID(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID], header[SENDER_ID]);
			}
		}

		if (!problem) {
			System.out.println("An Expected Error ocurred handling Stored message");
		}
	}

	private void handlePutchunk(String[] header, String body) {
		byte[] bodyData = body.getBytes();
		
		if (Peer.db.containsRestorableFile(header[FILE_ID])||bodyData.length<=Peer.ds.getSpaceLeft()) {
			return;
		}
		
		if (Peer.db.addChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
				Integer.parseInt(header[REPLICATION_DEG]), bodyData)) {
			Peer.ds.addUsedSpace(bodyData.length);
			
			Random ran = new Random();
			int sleepTime = ran.nextInt(401);
			try {
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
			
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.stored(header[FILE_ID], header[CHUNK_NO]));
		}
	}

}

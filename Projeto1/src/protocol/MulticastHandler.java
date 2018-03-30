package protocol;

import java.net.DatagramPacket;
import java.util.Random;

import utils.Convert;
import utils.HeaderCreater;
import Server.Peer;
import files.Chunk;
import files.RestorableFileInfo;
import files.RestoredFile;


public class MulticastHandler implements Runnable {
	public static final int HEADER = 0;
	public static final int BODY = 1;

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
		
		String[] header_body = data.split(HeaderCreater.CRLF + HeaderCreater.CRLF, 2);
		String[] header = header_body[HEADER].split(" ");

		if (header[SENDER_ID].equals(Peer.peerID))
			return;
		
		if(header[MESSAGE_TYPE].toUpperCase().equals("PUTCHUNK"))
			handlePutchunk(header, header_body[BODY]);
		else if(header[MESSAGE_TYPE].toUpperCase().equals("STORED"))
			handleStored(header);
		else if(header[MESSAGE_TYPE].toUpperCase().equals("GETCHUNK")) 
			handleGetChunk(header);
		else if(header[MESSAGE_TYPE].toUpperCase().equals("CHUNK"))
			handleChunk(header,header_body[BODY]);
		else if(header[MESSAGE_TYPE].toUpperCase().equals("DELETE"))
			handleDelete(header);
		else
			System.out.println("Not a valid Protocol");
	}

	private void handleDelete(String[] header) {
		Peer.db.removeRestorableFile(header[FILE_ID]);
		Peer.db.removeChunksByFileID(header[FILE_ID]);
		return;
	}

	private void handleChunk(String[] header, String body) {
		if (Peer.db.containsRestorableFile(header[FILE_ID])) {
			if(Peer.db.containsRestoredFile(header[FILE_ID])){	
			RestoredFile to_add_chunk=Peer.db.getRestoredFile(header[FILE_ID]);
			to_add_chunk.addData(Integer.parseInt(header[CHUNK_NO]), body);
			}
		}else {
			Peer.MulticastChannels[Peer.MDR_CHANNEL].addRestoredChunk(header[FILE_ID], Integer.parseInt(header[CHUNK_NO]));
		}

	}

	private void handleGetChunk(String[] header) {
		if(!Peer.db.hasChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID])) 
			return;
		threadSleep();
		
		if(!Peer.MulticastChannels[Peer.MDR_CHANNEL].containsRestoredChunk(header[FILE_ID], Integer.parseInt(header[CHUNK_NO]))){
			Chunk tempChunk;
			if((tempChunk=Peer.db.getChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID]))==null){
				System.out.println("Error: unexpected error ocurred");
				return;
			}
			byte[] message= Convert.concatenateArrays(HeaderCreater.chunk(tempChunk.getFileID(),tempChunk.getChunkNo()), tempChunk.getData());
			Peer.MulticastChannels[Peer.MDR_CHANNEL].send(message);
		}
	}

	private void handleStored(String[] header) {	
		if (Peer.db.containsRestorableFile(header[FILE_ID])) {
			Peer.db.addChunkPeer_RetorableFile(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
					header[SENDER_ID]);
		} else {
			if (Peer.db.hasChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID])) {
				Peer.db.addChunkPeerID(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID], header[SENDER_ID]);
			}
		}
	}

	private void handlePutchunk(String[] header, String body) {
		byte[] bodyData = body.getBytes();
		
		if (Peer.db.containsRestorableFile(header[FILE_ID])||bodyData.length>Peer.ds.getSpaceLeft()) {
			return;
		}
		
		if (Peer.db.addChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
				Integer.parseInt(header[REPLICATION_DEG]), bodyData)) {
			Peer.ds.addUsedSpace(bodyData.length);
			
			threadSleep();
			
			Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.stored(header[FILE_ID], header[CHUNK_NO]));
		}
	}

	private void threadSleep() {
		Random ran = new Random();
		int sleepTime = ran.nextInt(401);
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
}

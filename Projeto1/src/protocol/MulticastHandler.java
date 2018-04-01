package protocol;

import java.net.DatagramPacket;
import java.nio.charset.Charset;

import utils.Utils;
import utils.HeaderCreater;
import utils.MessageType;
import Server.Peer;
import files.Chunk;
import files.DataBase;
import files.DiskSpace;
import files.RestoredFile;

public class MulticastHandler extends MessageType implements Runnable {
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
		String data = new String(packet.getData(), Charset.forName("ISO_8859_1"));

		String[] header_body = data.split(HeaderCreater.CRLF + HeaderCreater.CRLF, 2);
		System.out.println("received message with the header: " + header_body[HEADER]);
		String[] header = header_body[HEADER].split(" ");

		if (header[SENDER_ID].equals(Peer.peerID))
			return;

		if (header[MESSAGE_TYPE].toUpperCase().equals(PUTCHUNK))
			handlePutchunk(header, header_body[BODY]);
		else if (header[MESSAGE_TYPE].toUpperCase().equals(STORED))
			handleStored(header);
		else if (header[MESSAGE_TYPE].toUpperCase().equals(GETCHUNK))
			handleGetChunk(header);
		else if (header[MESSAGE_TYPE].toUpperCase().equals(CHUNK))
			handleChunk(header, header_body[BODY]);
		else if (header[MESSAGE_TYPE].toUpperCase().equals(DELETE))
			handleDelete(header);
		else if (header[MESSAGE_TYPE].toUpperCase().equals(REMOVED))
			handleRemoved(header);
		else
			System.out.println("Not a valid Protocol");
	}

	private void handleRemoved(String[] header) {
		if (DataBase.getInstance().containsRestorableFile(header[FILE_ID])) {
			DataBase.getInstance().removeChunkPeer_RetorableFile(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
					header[SENDER_ID]);
			System.out.println("Removed peer from restorableFile:" + header[FILE_ID] + " with the Chunk Number: "
					+ header[CHUNK_NO]);
		} else {
			Chunk chunkPutChunk = new Chunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID]);
			DataBase.getInstance().addChunkRemovedPutChunk(chunkPutChunk);
			DataBase.getInstance().removeChunkPeerID(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
					header[SENDER_ID]);

			System.out.println("Removed peer from chunk.peersIDs\n" + "File id:" + header[FILE_ID] + "\nChunk Number:"
					+ header[CHUNK_NO]);

			Utils.threadRandomSleep(401);
			int replicationDeg = DataBase.getInstance().getChunkReplicationDegree(Integer.parseInt(header[CHUNK_NO]),
					header[FILE_ID]);
			if ((DataBase.getInstance().getChunkPeesrIDsSize(Integer.parseInt(header[CHUNK_NO]),
					header[FILE_ID]) < replicationDeg)
					&& !DataBase.getInstance().getChunkRemovedPutChunk(chunkPutChunk)) {

				Chunk tempChunk;
				if ((tempChunk = DataBase.getInstance().getChunk(Integer.parseInt(header[CHUNK_NO]),
						header[FILE_ID])) == null) {
					System.out.println("Error: unexpected error ocurred");
					return;
				}
				byte[] message = Utils.concatenateArrays(
						HeaderCreater.putChunk(header[FILE_ID], Integer.parseInt(header[CHUNK_NO]), replicationDeg),
						tempChunk.getData());
				Peer.MulticastChannels[Peer.MDB_CHANNEL].send(message);
				System.out.println("Sending putChunk message after removed request");
			}
			DataBase.getInstance().removeChunkRemovedPutChunk(chunkPutChunk);
		}
	}

	private void handleDelete(String[] header) {
		DataBase.getInstance().removeRestorableFile(header[FILE_ID]);
		DataBase.getInstance().removeChunksByFileID(header[FILE_ID]);
		System.out.println("Deleted File with the File ID: " + header[FILE_ID]);
		return;
	}

	private void handleChunk(String[] header, String body) {

		if (DataBase.getInstance().containsRestoredFile(header[FILE_ID])) {
			RestoredFile to_add_chunk = DataBase.getInstance().getRestoredFile(header[FILE_ID]);
			to_add_chunk.addData(Integer.parseInt(header[CHUNK_NO]), body.getBytes(Charset.forName("ISO_8859_1")));
			System.out
					.println("Restored chunk with File id: " + header[FILE_ID] + "\nChunk Number:" + header[CHUNK_NO]);
		} else {
			DataBase.getInstance().addRestoredChunk(header[FILE_ID], Integer.parseInt(header[CHUNK_NO]));
			System.out.println("Chunk Restored\nFile id: " + header[FILE_ID] + "\nChunk Number:" + header[CHUNK_NO]);
		}

	}

	private void handleGetChunk(String[] header) {
		if (!DataBase.getInstance().hasChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID]))
			return;
		Utils.threadRandomSleep(401);

		if (!DataBase.getInstance().containsRestoredChunk(header[FILE_ID], Integer.parseInt(header[CHUNK_NO]))) {
			Chunk tempChunk;
			if ((tempChunk = DataBase.getInstance().getChunk(Integer.parseInt(header[CHUNK_NO]),
					header[FILE_ID])) == null) {
				System.out.println("Error: unexpected error ocurred");
				return;
			}
			byte[] message = Utils.concatenateArrays(HeaderCreater.chunk(tempChunk.getFileID(), tempChunk.getChunkNo()),
					tempChunk.getData());
			Peer.MulticastChannels[Peer.MDR_CHANNEL].send(message);
			System.out.println(
					"Sending Chunk to be restored\nFile id: " + header[FILE_ID] + "\nChunk Number:" + header[CHUNK_NO]);
		}
		DataBase.getInstance().removeRestoredChunk(header[FILE_ID], Integer.parseInt(header[CHUNK_NO]));
	}

	private void handleStored(String[] header) {
		if (DataBase.getInstance().containsRestorableFile(header[FILE_ID])) {
			DataBase.getInstance().addChunkPeer_RetorableFile(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
					header[SENDER_ID]);
		} else {
			if (DataBase.getInstance().hasChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID])) {
				DataBase.getInstance().addChunkPeerID(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
						header[SENDER_ID]);
			}
		}
		System.out.println("Added to PeersID/ChunkPeers " + header[SENDER_ID]);
	}

	private void handlePutchunk(String[] header, String body) {
		DataBase.getInstance()
				.setTrueChunkRemovedPutChunk(new Chunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID]));

		byte[] bodyData = body.getBytes(Charset.forName("ISO_8859_1"));

		if (DataBase.getInstance().containsRestorableFile(header[FILE_ID])
				|| bodyData.length > DiskSpace.getInstance().getSpaceLeft()) {
			return;
		}

		if (DataBase.getInstance().addChunk(Integer.parseInt(header[CHUNK_NO]), header[FILE_ID],
				Integer.parseInt(header[REPLICATION_DEG]), bodyData)) {

			Utils.threadRandomSleep(401);

			Peer.MulticastChannels[Peer.MC_CHANNEL].send(HeaderCreater.stored(header[FILE_ID], header[CHUNK_NO]));
			System.out.println("Stored Chunk\nFile id: " + header[FILE_ID] + "\nChunk Number:" + header[CHUNK_NO]);
		}
	}

}

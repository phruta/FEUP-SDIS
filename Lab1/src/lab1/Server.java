/**
 * 
 */
package lab1;

import java.util.HashMap;
import java.io.IOException;
import java.net.*;

/**
 * @author RTorres
 *
 */
public class Server {

	private static final int MaxSize = 256;
	private final int port = 4445;
	private HashMap<String, String> database;
	private DatagramSocket socket;
	

	public static void main(String[] args) {

		try {

			Server server = new Server();
			server.start();

		} catch (Exception e) {
			System.out.println("Error opening server.\n");
		}
	}

	public Server() throws SocketException {
		this.database = new HashMap<String, String>();
		this.socket = new DatagramSocket(this.port);
	}

	private void start() throws IOException {

		while (true) {
			DatagramPacket packet = new DatagramPacket(new byte[MaxSize], MaxSize);

			socket.receive(packet);
			
			String buffer = new String(packet.getData());
			String[] buffer_splited = buffer.split("\\s");

			System.out.println("COMMAND: " + buffer);

			if (buffer_splited[0].equals("REGISTER")) {
				int num = this.register(buffer_splited[1], buffer_splited[2]);

				String result = Integer.toString(num);

				try {
					this.sendAnswer(result, packet);
				} catch (Exception e) {
					System.out.println("Server response failed.");
				}

			} else if (buffer_splited[0].equals("LOOKUP")) {

				String owner = this.lookup(buffer_splited[1]);

				try {
					this.sendAnswer(owner, packet);
				} catch (Exception e) {
					System.out.println("Server response failed.");
				}

			}
		}

	}

	private void sendAnswer(String owner, DatagramPacket packet) throws IOException {
		int port=packet.getPort();
		InetAddress address = packet.getAddress();
		byte buffer[] = owner.getBytes();
		packet = new DatagramPacket(buffer, buffer.length, address, port);
		socket.send(packet);
	}

	private int register(String owner, String numberPlate) {

		if (database.get(numberPlate) == null)
			return -1;

		database.put(numberPlate, owner);
		return database.size();
	}

	private String lookup(String plate) {
		String to_return = database.get(plate);
		if (to_return != null)
			return to_return;

		return "NOT_FOUND";
	}

}

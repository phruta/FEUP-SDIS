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
	private ServerMulticastThread thread;

	public static void main(String[] args) {

		try {

			Server server = new Server();
			server.start();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Server() throws SocketException {
		this.database = new HashMap<String, String>();
		this.socket = new DatagramSocket(this.port);

		this.thread = new ServerMulticastThread();
		thread.start();
	}

	private void start() throws IOException {

		while (true) {
			DatagramPacket packet = new DatagramPacket(new byte[MaxSize], MaxSize);

			socket.receive(packet);

			String buffer = new String(packet.getData());
			String[] buffer_splited = buffer.split("\\s");

			System.out.println("COMMAND: " + buffer);

			if (buffer_splited[0].equals("REGISTER")) {
				int num = this.register(buffer_splited[2], buffer_splited[1]);

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
		int port = packet.getPort();
		InetAddress address = packet.getAddress();
		byte buffer[] = owner.getBytes();
		packet = new DatagramPacket(buffer, buffer.length, address, port);
		socket.send(packet);
	}

	private int register(String owner, String numberPlate) {

		if (database.get(numberPlate) != null)
			return -1;

		database.put(numberPlate, owner);
		return database.size();
	}

	private String lookup(String numberPlate) {
		String to_return = database.get(numberPlate);
		if (to_return != null)
			return to_return;

		return "NOT_FOUND";
	}

	private class ServerMulticastThread extends Thread {
		final static String MULTICAST_INET_ADDR = "224.0.0.3";
		final static int MULTICAST_PORT = 8888;

		public void run() {

			try {
				InetAddress addr = InetAddress.getByName(MULTICAST_INET_ADDR);
				@SuppressWarnings("resource")
				DatagramSocket MulticastserverSocket = new DatagramSocket();

				while (true) {

						String msg = InetAddress.getLocalHost().getHostName() +" " +Integer.toString(port)+" ";
						DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
						msg.getBytes().length, addr, MULTICAST_PORT);
						MulticastserverSocket.send(msgPacket);
						System.out.println("Server sent packet with msg: " + msg);
						Thread.sleep(2000);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}

/**
 * 
 */
package lab1;

import java.io.IOException;
import java.net.*;

/**
 * @author RTorres
 *
 */
public class Client {

	private static final int MaxSize = 256;
	private final int port = 4445;
	private DatagramPacket packet;
	private DatagramSocket socket;
	private InetAddress address;

	private String opr;
	private String[] to_send;

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		String hostname = "127.0.0.1";

		String opr = "REGISTER";
		String[] to_send = { "AA-BB-CC", "ruben" };

		try {

			Client client = new Client(hostname, opr, to_send);
			client.start();

		} catch (Exception e) {
			System.out.println("Error opening server.\n");
		}
	}

	public Client(String hostName, String opr, String[] to_send) throws SocketException, UnknownHostException {

		this.socket = new DatagramSocket();
		this.address = InetAddress.getByName(hostName);
		this.opr = opr;
		this.to_send = to_send;
	}

	private void start() throws IOException {

		String message;

		byte[] buffer;

		if (this.opr.equals("REGISTER")) {

			message = this.opr + " " + this.to_send[0] + " " + this.to_send[1];
			buffer = message.getBytes();

			this.packet = new DatagramPacket(buffer, buffer.length, this.address, this.port);
			this.socket.send(packet);
		} else if (this.opr.equals("LOOKUP")) {

			message = this.opr + " " + this.to_send[0];
			buffer = message.getBytes();
			packet = new DatagramPacket(buffer, buffer.length, this.address, this.port);
			this.socket.send(packet);
		}

		System.out.println("enviei alguma cena");
		try {
			packet = new DatagramPacket(new byte[MaxSize], MaxSize);
			socket.receive(packet);
			System.out.println("recebi alguma cena");
			String messageReceived = new String(packet.getData());
			System.out.println("Server message: " + messageReceived);
		} catch (Exception e) {
			System.out.println("No answer received.");
		}

	}

}

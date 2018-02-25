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
	final static String MULTICAST_INET_ADDR = "224.0.0.3";
	final static int MULTICAST_PORT = 8888;

	private DatagramPacket packet;
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	
	private String opr;
	private String[] to_send;

	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) throws IOException {

		InetAddress Multicast_address = InetAddress.getByName(MULTICAST_INET_ADDR);
		MulticastSocket clientSocket = new MulticastSocket(MULTICAST_PORT);
		clientSocket.joinGroup(Multicast_address);

		byte[] buf = new byte[MaxSize];
		DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
		clientSocket.receive(msgPacket);
		String msg = new String(buf, 0, buf.length);
		System.out.println("Socket 1 received msg: " + msg);
		clientSocket.close();
		
		String[] buffer_splited = msg.split("\\s");
		
		String opr = "LOOKUP";
		String[] to_send = { "AA-BB-CC", "ruben" };
		
		
		try {

			Client client = new Client(buffer_splited[0], opr, to_send, buffer_splited[1]);
			client.start();

		} catch (Exception e) {
			System.out.println("Error opening server.\n");
		}
	}

	public Client(String hostName, String opr, String[] to_send,String port) throws SocketException, UnknownHostException {

		this.socket = new DatagramSocket();
		this.address = InetAddress.getByName(hostName);
		this.opr = opr;
		this.to_send = to_send;
		this.port=Integer.parseInt(port);
	}

	private void start() throws IOException {

		String message;

		byte[] buffer;

		if (this.opr.equals("REGISTER")) {

			message = this.opr + " " + this.to_send[0] + " " + this.to_send[1] + " ";
			buffer = message.getBytes();

			this.packet = new DatagramPacket(buffer, buffer.length, this.address, this.port);
			this.socket.send(packet);
		} else if (this.opr.equals("LOOKUP")) {

			message = this.opr + " " + this.to_send[0] + " ";
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

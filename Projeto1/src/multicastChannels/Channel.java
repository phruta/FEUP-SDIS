package multicastChannels;

import java.io.IOException;
import java.net.*;

public abstract class Channel implements Runnable {
	public static final int MAX_MESSAGE_SIZE = 65000;

	public MulticastSocket socket;
	public InetAddress address;
	public int port;
	private volatile boolean stopWork;

	public Channel(InetAddress address, int port) {
		this.address = address;
		this.port = port;
		this.stopWork = false;
		try {
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		this.stopWork = true;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[MAX_MESSAGE_SIZE];
		while (!stopWork) {
			try {

				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				handler(packet);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void handler(DatagramPacket packet);

}

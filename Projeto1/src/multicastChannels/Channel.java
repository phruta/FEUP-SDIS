package multicastChannels;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import protocol.MulticastHandler;


public class Channel implements Runnable {
	public static final int MAX_MESSAGE_SIZE = 65000;

	public MulticastSocket socket;
	public InetAddress address;
	public int port;
	private volatile boolean stopWork;
	private ExecutorService threadpool;

	public Channel(InetAddress address, int port) {
		this.address = address;
		this.port = port;
		this.stopWork = false;
		threadpool=Executors.newFixedThreadPool(5);
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
		threadpool.shutdown();
		
	}

	@Override
	public void run() {
		byte[] buffer = new byte[MAX_MESSAGE_SIZE];
		while (!stopWork) {
			try {

				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				threadpool.execute(new MulticastHandler(packet));
			} catch (IOException e) {
				e.getMessage();
				e.printStackTrace();
			}
		}
	}
	
    public synchronized void send(byte[] message) {  
        try {
        	DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
			socket.send(packet);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
    }


}

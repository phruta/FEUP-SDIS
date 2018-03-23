package multicastChannels;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MDR extends Channel {

	public MDR(MulticastSocket socket, InetAddress address, int port) {
		super(socket, address, port);
		// TODO Auto-generated constructor stub
	}

	public void handler(DatagramPacket packet) {
		// TODO Auto-generated method stub
		
	}



}

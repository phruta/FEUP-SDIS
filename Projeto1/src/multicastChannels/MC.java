package multicastChannels;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MC extends Channel {

	public MC(MulticastSocket socket, InetAddress address, int port) {
		super(socket, address, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handler(DatagramPacket packet) {
		// TODO Auto-generated method stub
		
	}

}

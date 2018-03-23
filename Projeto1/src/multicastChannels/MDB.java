package multicastChannels;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MDB extends Channel {

	public MDB(MulticastSocket socket, InetAddress address, int port) {
		super(socket, address, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handler(DatagramPacket packet) {
		// TODO Auto-generated method stub
		
	}

}

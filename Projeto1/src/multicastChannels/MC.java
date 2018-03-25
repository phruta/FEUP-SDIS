package multicastChannels;

import java.net.DatagramPacket;
import java.net.InetAddress;


public class MC extends Channel {

	public MC(InetAddress address, int port) {
		super(address, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handler(DatagramPacket packet) {
		// TODO Auto-generated method stub
		
	}

}

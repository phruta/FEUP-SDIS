package multicastChannels;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.MembershipKey;
import java.nio.channels.NetworkChannel;
import java.util.Set;

public abstract class Channel implements java.nio.channels.MulticastChannel {

	@Override
	public NetworkChannel bind(SocketAddress arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SocketAddress getLocalAddress() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getOption(SocketOption<T> arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> NetworkChannel setOption(SocketOption<T> arg0, T arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public MembershipKey join(InetAddress arg0, NetworkInterface arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MembershipKey join(InetAddress arg0, NetworkInterface arg1, InetAddress arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}

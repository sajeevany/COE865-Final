package project865;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class App {

	private final int helloProtocolPort = 10090;
	private String myUniqueID = null;
	private String fileNames[];

	private Map<InetAddress, DatagramSocket> ipSocketMap = new HashMap<InetAddress, DatagramSocket>();

	public App(String[] IP, int[] sockets, String[] myFilesList, String uniqueID) {
		try {

			for (int i = 0; i < sockets.length; i++) {
				ipSocketMap.put(InetAddress.getByName(IP[i]), new DatagramSocket(sockets[i]));
			}

		} catch (SocketException e) {

			e.printStackTrace();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		this.myUniqueID = uniqueID;
		this.fileNames = myFilesList;

	}

	
	

	
	public static void main(String[] args) {

	}

}
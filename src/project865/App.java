package project865;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {

	private final int helloProtocolPort = 10090;
	private String myUniqueID = null;
	private String fileNames[];
	private HelloManager hManager;
	private DatagramSocket myHelloSocket;

	private Map<InetAddress, DatagramSocket> ipSocketMap = new HashMap<InetAddress, DatagramSocket>();

	public App(String[] IP, int[] sockets, String[] myFilesList, String uniqueID) throws SocketException {
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
		this.myHelloSocket = new DatagramSocket(helloProtocolPort);
		this.hManager = new HelloManager(new ArrayList<DatagramSocket>(){{add(myHelloSocket);}}, this.myUniqueID);
		this.hManager.runManager();
	}

	public void startHelloManager()
	{
		
	}
		
	public static void main(String[] args) throws SocketException {
		
		App client1 = new App(new String[]{"192.168.0.2", "192.168.1.2"}, new int[] {10901, 12345}, new String[] {"groupNames.txt", "myFile.txt"}, "R1");
	}

}
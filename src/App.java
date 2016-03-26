import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class App {

	private final int helloProtocolPort = 10090;
	private String myUniqueID;
	
	private Map<InetAddress, DatagramSocket> ipSocketMap = new HashMap<InetAddress, DatagramSocket>();
	
	
	public App(String[] IP, int[] sockets, String[] myFilesList, String uniqueID)
	{
		try {
			
			for (int i = 0; i < sockets.length; i++)
			{
				ipSocketMap.put(InetAddress.getByName(IP[i]) , new DatagramSocket(sockets[i]));
			}
			
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.myUniqueID = uniqueID;
		
	//file setups

	}
	
	public static void main(String[] args)
	{

	}
	
}
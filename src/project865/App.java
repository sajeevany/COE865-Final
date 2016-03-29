package project865;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class App {

	private String myUniqueID = null;
	private String resourceNames[];
	private HelloManager hManager;
	private Map<InetAddress, DatagramSocket> myIPSocketMap = new HashMap<InetAddress, DatagramSocket>();
	private ArrayList<InetAddress> targetMachineIPs = new ArrayList<InetAddress>();

	public App(String[] myIPs, int[] myQuerySockets, String[] myResourceList, String uniqueID, String configFileName) throws IOException {
            try {

                for (int i = 0; i < myQuerySockets.length; i++) {
                        myIPSocketMap.put(InetAddress.getByName(myIPs[i]), new DatagramSocket(myQuerySockets[i]));
                }

		} catch (SocketException e) {

			e.printStackTrace();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		this.myUniqueID = uniqueID;
		this.resourceNames = myResourceList;
		
		//determine network. Network mask assumption of /24
		ArrayList<String> myNetwork = new ArrayList<String>();
		List<String> myIPList =  Arrays.asList(myIPs);

		for (String i : myIPs)
		{
			myNetwork.add(getNetwork(i));
		}
		
		//Read file and find target machines/hosts within my networks
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(configFileName));
			
			String ipAddr = br.readLine();
			while(ipAddr != null)
			{
				String network = getNetwork(ipAddr);
				//If the IP is within my network and the IP is not my own
				if (myNetwork.contains(network) && !myIPList.contains(ipAddr))
				{
					targetMachineIPs.add(InetAddress.getByName(ipAddr));
				}
				ipAddr = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
                    e.printStackTrace();
		}finally
		{
                    br.close();
		}	
		
		this.hManager = new HelloManager(this.myUniqueID, targetMachineIPs, this.myIPSocketMap, myResourceList);
		this.hManager.runManager();
	}
		
	public static void main(String[] args) throws IOException {
		
		App client1 = new App(new String[]{"172.16.57.8", "10.1.1.3"}, new int[] {10901, 12345}, new String[] {"groupNames.txt", "myFile.txt"}, "R1", "config");
	}

	public static String getNetwork(String IP)
	{
		StringTokenizer sTok = new StringTokenizer(IP,".");
		StringBuilder sBuild = new StringBuilder(); 
		
		for (int j = 0; j < sTok.countTokens(); j++ )
		{
			sBuild.append(sTok.nextToken() + ".");
		}
		sBuild.append(sTok.nextToken() + ".0");
		
		return sBuild.toString();
	}
}
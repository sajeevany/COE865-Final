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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class App {

	// Unique ID of host
	private String myUniqueID = null;
	// Resources belonging to host,
	private String resourceNames[];
	//Hello manager to run Hello protocol threads
	private HelloManager hManager;
	//Hello manager to run Query protocol threads
	private QueryManager qManager;
	// Hosts Ip Addresses and associted port #'s
	private Map<String, Integer> myIPSocketMap = new HashMap<String, Integer>();
	// targetMachines IP addresses to be read from Config File
	private ArrayList<InetAddress> targetMachineIPs = new ArrayList<InetAddress>();

	public App(String[] myIPs, int[] myQuerySockets, String[] myResourceList, String uniqueID, String configFileName) throws IOException {
		for (int i = 0; i < myQuerySockets.length; i++) {
			myIPSocketMap.put(myIPs[i], myQuerySockets[i]);
		}

		//set hosts values from constructor parameters
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
		//HashMap<String,String> sourceTargetIPList = new HashMap<String, String>();
		ArrayList<SourceTargetIPPair> stIPPairList = new ArrayList<SourceTargetIPPair>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(configFileName));
			
			String ipAddr = br.readLine();
			int i = 0;
			while(ipAddr != null)
			{		
				StringTokenizer sTo = new StringTokenizer(ipAddr, ",");
				if (sTo.countTokens() > 2)
				{
					throw new IllegalArgumentException("Too many parameters in config file");
				}
				String sourceIP = sTo.nextToken();
				String targetIP = sTo.nextToken();
				
				for (String myIP : myIPs)
				{
					if (sourceIP.equals(myIP))
					{
						stIPPairList.add(new SourceTargetIPPair(sourceIP, targetIP));
						break;
					}	
				}
				
				ipAddr = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
                    e.printStackTrace();
		}finally
		{
                    br.close();
		}	
		
		//		//Set<SourceTargetIPPair> stSet = new LinkedHashSet<SourceTargetIPPair>();		//this.hManager = new HelloManager(this.myUniqueID, sourceTargetIPMap, this.myIPSocketMap, myResourceList);


		// Start Hello Manager and pass in UniqueID and directly connected ip addresses
		// 
		this.hManager = new HelloManager(this.myUniqueID, stIPPairList, this.myIPSocketMap, myResourceList);
		this.hManager.runManager();
        
        // Init Query Manager for diffreent machines                        
                //R1
				//this.qManager = new QueryManager("R1",new String[]{"10.1.1.13"}, new int[] {10901}, new String[] {"r1File.txt","r1File2.txt" });
                //R2
                //this.qManager = new QueryManager("R2",new String[]{"10.1.1.11", "10.1.2.10"}, new int[] {10902, 10903}, new String[] {"r2File.txt","r2File2.txt"});
                //R3
               //this.qManager = new QueryManager("R3",new String[]{"10.1.2.7"}, new int[] {10904}, new String[]{"r3File.txt","r3File2.txt","r3File3.txt" });
		
		/// run the hello manager
		this.qManager.runManager();
		

	}
		
	// Main function creates an instance of the App, Host IP and ports need to be specified
	// Target Machines need to be set in Config File 
	public static void main(String[] args) throws IOException {
	

                
               // App client1 = new App(new String[]{"10.1.1.13"}, new int[] {10901}, new String[] {"r1File.txt","r1File2.txt" }, "R1", "config");
                App client2 = new App(new String[]{"10.1.1.11", "10.1.2.10"}, new int[] {10902, 10903}, new String[] {"r2File.txt","r2File2.txt"}, "R2", "config");
                //App client3 = new App(new String[]{"10.1.2.7"}, new int[] {10904}, new String[] {"r3File.txt","r3File2.txt","r3File3.txt" }, "R3", "config");
                
	}

	// Returns the network of the IP, Assuming mask = 24 bits
	public static String getNetwork(String IP)
	{
		// Seperate IP by '.'
		StringTokenizer sTok = new StringTokenizer(IP,".");
		StringBuilder sBuild = new StringBuilder(); 
		
		// create network value by loading value and adding 0 as last byte
		for (int j = 0; j < sTok.countTokens(); j++ )
		{
			sBuild.append(sTok.nextToken() + ".");
		}
		sBuild.append(sTok.nextToken() + ".0");
		
		return sBuild.toString();
	}
}
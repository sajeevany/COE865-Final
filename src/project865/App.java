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

	private String myUniqueID = null;
	private String resourceNames[];
	private HelloManager hManager;
	private QueryManager qManager;
	private Map<String, Integer> myIPSocketMap = new HashMap<String, Integer>();
	private ArrayList<InetAddress> targetMachineIPs = new ArrayList<InetAddress>();

	public App(String[] myIPs, int[] myQuerySockets, String[] myResourceList, String uniqueID, String configFileName) throws IOException {
		for (int i = 0; i < myQuerySockets.length; i++) {
			myIPSocketMap.put(myIPs[i], myQuerySockets[i]);
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
		
		//Set<SourceTargetIPPair> stSet = new LinkedHashSet<SourceTargetIPPair>();
		//this.hManager = new HelloManager(this.myUniqueID, sourceTargetIPMap, this.myIPSocketMap, myResourceList);
//		this.hManager = new HelloManager(this.myUniqueID, stIPPairList, this.myIPSocketMap, myResourceList);
//		this.hManager.runManager();
		this.qManager = new QueryManager("R1",new String[]{"25.113.28.132"}, new int[] {8032}, new String[] {"Saj.txt"});
		this.qManager.runManager();
		

	}
		
	public static void main(String[] args) throws IOException {
	
                String whoami = "R1";
            
                HashMap<String, ArrayList<String>> hostResourceMap = new HashMap<String, ArrayList<String>>();
                hostResourceMap.put("R1", new ArrayList<String>(){{add("r1File.txt"); add("r1File2.txt");}});
                hostResourceMap.put("R2", new ArrayList<String>(){{add("r2File.txt"); add("r2File2.txt");}});
                hostResourceMap.put("R3", new ArrayList<String>(){{add("r3File.txt"); add("r3File2.txt"); add("r3File3.txt");}});
                        
                ArrayList<AttributeTrio> rte1Tr = new ArrayList<AttributeTrio>();
                AttributeTrio atr1 = new AttributeTrio("R1", hostResourceMap, new ArrayList<String>(){{add("R2");}});
                AttributeTrio atr2 = new AttributeTrio("R2", hostResourceMap, new ArrayList<String>(){{add("R1"); add("R2");}});
                AttributeTrio atr3 = new AttributeTrio("R3", hostResourceMap, new ArrayList<String>(){{add("R2");}});
                rte1Tr.add(atr1);
                rte1Tr.add(atr2);
                rte1Tr.add(atr3);
                
                //R1
                //{who data is from, port to sendIQueryRequestsTo, what their uniqueID is}
                RoutingTableEntry rte1 = new RoutingTableEntry("10.1.1.13", 10901, "R1", rte1Tr); //local data
                RoutingTableEntry rte2 = new RoutingTableEntry("10.1.1.11", 10902, "R1", rte1Tr);
                
                //R2
                RoutingTableEntry rte3 = new RoutingTableEntry("10.1.1.11", 10902, "R2", rte1Tr); //local data
                RoutingTableEntry rte4 = new RoutingTableEntry("10.1.1.13", 10901, "R1", rte1Tr);
                RoutingTableEntry rte5 = new RoutingTableEntry("10.1.1.10", 10903, "R2", rte1Tr); //local data
                RoutingTableEntry rte6 = new RoutingTableEntry("10.1.2.7" , 10901, "R2", rte1Tr);
                
                //R3
                RoutingTableEntry rte7 = new RoutingTableEntry("10.1.2.7" , 10904, "R3", rte1Tr); //local data
                RoutingTableEntry rte8 = new RoutingTableEntry("10.1.1.10", 10903, "R2", rte1Tr);
		
                if (whoami.toUpperCase().equals("R1"))
                {
                    RoutingTable.getRoutingTableInstance().addRoute(rte1);
                    RoutingTable.getRoutingTableInstance().addRoute(rte2);
                }
                else if (whoami.toUpperCase().equals("R2"))
                {
                    RoutingTable.getRoutingTableInstance().addRoute(rte3);
                    RoutingTable.getRoutingTableInstance().addRoute(rte4);
                    RoutingTable.getRoutingTableInstance().addRoute(rte5);
                    RoutingTable.getRoutingTableInstance().addRoute(rte6);
                }
                else
                {
                    RoutingTable.getRoutingTableInstance().addRoute(rte7);
                    RoutingTable.getRoutingTableInstance().addRoute(rte8);
                }
                
                App client1 = new App(new String[]{"10.1.1.13"}, new int[] {10901}, new String[] {"r1File.txt","r1File2.txt" }, "R1", "config");
                App client2 = new App(new String[]{"10.1.1.11", "10.1.2.10"}, new int[] {10902, 10903}, new String[] {"r2File.txt","r2File2.txt"}, "R2", "config");
                App client3 = new App(new String[]{"10.1.2.7"}, new int[] {10904}, new String[] {"r3File.txt","r3File2.txt","r3File3.txt" }, "R2", "config");
                
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
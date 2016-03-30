package project865;

public class QueryManager{
	
	private String myUniqueID = null;
	private String[] myIPs = null;
	private int[] mySockets = null;
	
	public QueryManager(String uniqueID, String[] ips, int[] sockets)
	{
		this.myUniqueID = uniqueID;
		this.myIPs = ips;
		this.mySockets = sockets;
	}
}
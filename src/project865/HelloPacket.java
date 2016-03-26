package project865;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class HelloPacket implements Serializable {

	private String myUniqueID = null;
	// uniqueID, resource
	private Map<String, String> uniqueIDResourceMap = new HashMap<String, String>();
	private InetAddress myAddresses[];
	private DatagramSocket mySockets[];

	public static final int sendInterval = 10;
	public static final int timeout = 3 * sendInterval;

	public HelloPacket(String uniqueID,Map<String, String> uniqueIDResourceMap, InetAddress myAddresses[], DatagramSocket mySockets[])
	{
		this.myUniqueID = uniqueID;
		this.uniqueIDResourceMap = uniqueIDResourceMap;
		this.myAddresses = myAddresses;
		this.mySockets = mySockets;
	}
}

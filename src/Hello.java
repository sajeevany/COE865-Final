import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hello {
	
	private String myUniqueID = null;
	private ArrayList<String> myNeighboursUniqueID = new ArrayList<String>();
	private Map<String, String> uniqueIDResourceMap = new HashMap<String, String>();
	
	private final int sendInterval = 100;
	private final int timeout = 3 * sendInterval;
	

	public Hello(String uniqueID,Map<String, String> uniqueIDResourceMap )
	{
		
	}
	
}

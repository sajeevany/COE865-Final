package project865;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class QueryPacket implements Serializable {

	private String myUniqueID = null;
	private String myDestinationID = null;
	// if false then results being returned 
	private boolean isRequest = false;
	private String requestedFileName = null;
	private boolean requestResult = false; //true - DestinationID has requestedFileName


	public QueryPacket(String uniqueID, String myDestinationID, boolean isRequest, String fileName, boolean requestedResponse)
	{	
		this.myUniqueID = uniqueID;
		this.myDestinationID = myDestinationID;
		this.isRequest = isRequest;
		this.requestedFileName = fileName;
		this.requestResult  = requestedResponse;		
	}


	public String getMyUniqueID() {
		return myUniqueID;
	}


	public String getMyDestinationID() {
		return myDestinationID;
	}


	public boolean isRequest() {
		return isRequest;
	}


	public String getRequestedFileName() {
		return requestedFileName;
	}


	public boolean isRequestResult() {
		return requestResult;
	}
	
	 @Override
		public String toString()
		{
			StringBuilder sB = new StringBuilder("myUniqueID: " + getMyUniqueID() + "\n");
			
			sB.append("Destination" + ": "  + getMyDestinationID() + "\n");
			sB.append("isRequest" + ": "  + isRequest() + "\n");
			sB.append("FileName" + ": "  + getRequestedFileName() + "\n");
			sB.append("isResultRequest" + ": "  + isRequestResult() + "\n");
			
					
			sB.append("\nResource List:\n");
			
			
			return sB.toString();
		}
	
	
	
    

}


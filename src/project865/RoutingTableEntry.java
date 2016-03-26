/*
 * Gumi is love.
 * Gumi is life.
 */
package project865;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Krishna Deoram
 */
public class RoutingTableEntry {

	private InetAddress nextHop;
	private Socket nextHopSocket;
	private String uniqueID;
	private HashMap<String, String> neighbourAndResource = new HashMap<String, String>();

	public RoutingTableEntry(InetAddress nextHop, Socket nextHopSocket, String uniqueID) {
		this.nextHop = nextHop;
		this.nextHopSocket = nextHopSocket;
		this.uniqueID = uniqueID;
	}

	public InetAddress getNextHop() {
		return nextHop;
	}

	public void setNextHop(InetAddress nextHop) {
		this.nextHop = nextHop;
	}

	public Socket getNextHopSocket() {
		return nextHopSocket;
	}

	public void setNextHopSocket(Socket nextHopSocket) {
		this.nextHopSocket = nextHopSocket;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public HashMap<String, String> getNeighbourAndResource() {
		return neighbourAndResource;
	}

	public void setNeighbourAndResource(String id, String resource) {
		this.neighbourAndResource.put(id, resource); // Order is crucial.
	}

	public HashMap<String, String> getNeibourAndResource() { // Just in case
		return this.neighbourAndResource;
	}

	public String hasResource(String resource) { // Returns the uniqueID of the
													// client with the
													// associated resource.
		String id = "";

		if (this.neighbourAndResource.containsValue(resource)) {
			id = this.neighbourAndResource.get(resource);
		}

		return id;
	}

	@Override
	public String toString() {
		return "RoutingTableEntry{" + "nextHop=" + nextHop + ", nextHopSocket=" + nextHopSocket + ", uniqueID="
				+ uniqueID + ", neighbourAndResource=" + neighbourAndResource.toString() + '}';
	}
}

package socket.Connections;

import java.net.SocketException;

import setup.ProjectProviderInstance;
import setup.Expressions.PositionExpression;
import socket.java.Server;

public class Connector {
	protected Server server;
	
	public Connector(int port) {
		server = new Server(port);
		server.initServer();
		server.waitForConnections();
	}
	
	public Server getServer() {
		return server;
	}
	
	public String readLine() throws SocketException {
		return server.read();
	}
	
	public void writeLayer(PositionExpression... peVrg) {
		//POSITION:DISTANCE
		for(PositionExpression pe : peVrg) {
			server.write(", '" + ProjectProviderInstance.POSITION + pe.getPositionNumber() + "': " + pe.getMeasuredDistance());
		}
	}
	
	public void writeLegDistance(int sensorNumber, int distance) {
		server.write("'pos':" + ProjectProviderInstance.POSITION);
		server.write(",'dist':" + distance);
	}
	
	public void write(String s) {
		server.write(s);
	}
}

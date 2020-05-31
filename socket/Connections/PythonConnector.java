package socket.Connections;
 
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import setup.ProjectProviderInstance;
import setup.Expressions.PositionExpression;
import socket.PCSocketStarter;

public class PythonConnector extends Connector {
	
	private Lock lock = new ReentrantLock(true);
	
	public PythonConnector(int port) {
		super(port);
		PCSocketStarter.hasConnectionToPC = true;
	}
	
	@Override
	public void writeLayer(PositionExpression... peVrg) {
		lock.lock();
		//new layer
		super.server.write(ProjectProviderInstance.START_LAYER);
		//timestamp
		super.server.write("{'" + ProjectProviderInstance.TIMESTAMP + "': " + peVrg[0].getTimeStamp());
		
		super.writeLayer(peVrg);
		
		//end layer
		super.server.write("}");
		super.server.write(ProjectProviderInstance.END_LAYER);
		
		try {
			super.server.getClientConnection().getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.unlock();
	}
	
	@Override
	public void writeLegDistance(int sensorNumber, int distance) {
		lock.lock();
		//new leg
		super.server.write(ProjectProviderInstance.START_LEG);
		super.server.write("{");
		super.writeLegDistance(sensorNumber, distance);
		super.server.write("}");
		super.server.write(ProjectProviderInstance.END_LEG);
		
		try {
			super.server.getClientConnection().getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.unlock();
	}
	
}
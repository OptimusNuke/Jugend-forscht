package threads.Organize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Caches.SensorDataCache;
import Caches.SensorDataLayerCachesPC;
import setup.Expressions.ArcExpression;
 
public class SensorDataLayerOrganizerDaemon extends Thread {
	
	//Collection to remove several ArcExpressions at once from the sensorDataCache.
	private List<ArcExpression> collection = new ArrayList<ArcExpression>();
	private Iterator<ArcExpression> iterator;
	private ArcExpression current;
	private int sensorNumber;
	
	/**
	 * Start the SensorDataLayerOrganizer-daemonThread.
	 */
	public SensorDataLayerOrganizerDaemon() {
		setDaemon(true);
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Initialize the iterator over the sensorDataCache.
			iterator = SensorDataCache.sensorDataCache.iterator();
			
			//Iterates over the sensorDataCache as long as there are elements available.
			while(iterator.hasNext()) {
				
//				Initialize an ArcExpression with the current ArcExpression
				current = iterator.next();
//				System.out.println("ARC: " + current.getKeyString());
//				Get the sensorNumber of this ArcExpression
				sensorNumber = current.getSensorNumber();
//				Adds this ArcExpression to the collection
				collection.add(current);
				
//				Adds the ArcExpression to the correct sensorDataLevelCache.
				if(sensorNumber == 1 || sensorNumber == 2 || sensorNumber == 3) {
					SensorDataLayerCachesPC.firstLayer.add(current);
				} else if(sensorNumber == 4 || sensorNumber == 5 || sensorNumber == 6) {
					SensorDataLayerCachesPC.secondLayer.add(current);
				} else if(sensorNumber == 7 || sensorNumber == 8) {
					SensorDataLayerCachesPC.thirdLayer.add(current);
				} else if(sensorNumber == 9 || sensorNumber == 10){
					SensorDataLayerCachesPC.fourthLayer.add(current);
				} else {
					System.err.println("ERROR: Couldn't add the ArcExpression " + current.getKeyString() + " with sensorNumber " + sensorNumber + " to a layer!");
				}
			}
			
//			Remove the assigned data from the sensorDataCache
			SensorDataCache.sensorDataCache.removeAll(collection);
			collection.clear();
		}
	}
}
package Caches;
 
/**
 * 
 * @author Tim_Wieland
 * @version 1.0.0
 *
 * This class:
 *  -   Provides the sensorDataCache - a CopyOnWriteArrayList of ArcExpression's working as a cache
 *  	- and the method addElement to add elements to the cache.
 *  
 */

import java.util.concurrent.CopyOnWriteArrayList;

import setup.Expressions.ArcExpression;


public class SensorDataCache {
	
	public static CopyOnWriteArrayList<ArcExpression> sensorDataCache = new CopyOnWriteArrayList<ArcExpression>();
	
	/**
	 * 
	 * @param sensorNumber sensor number
	 * @param measuredDistance distance returned from the sensor
	 * 
	 * This method adds elements to the sensorDataCache
	 */
	public static void addElement(byte sensorNumber, int timeStamp, int measuredDistance) {
		if(sensorNumber > 0 && timeStamp > 0 && measuredDistance > 0) {
			sensorDataCache.add(new ArcExpression(sensorNumber, timeStamp, measuredDistance));
		} else {
			throw new IllegalArgumentException("Can't add a new ArcExpression to the sensorDataCache: One of the following arguments is '0' or null.\n"
					+ "sensorNumber:\t" + sensorNumber + "\ttimeStamp:\t" + timeStamp + "\tmeasuredDistance:\t" + measuredDistance);
		}
	}
}

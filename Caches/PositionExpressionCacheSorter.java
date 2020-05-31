package Caches;
 
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import setup.ProjectProviderInstance;
import setup.Expressions.PositionExpression;
import setup.Expressions.PositionExpressionLayer;

public class PositionExpressionCacheSorter {
	  
	public static JsonObject jsonTimeStamps = new JsonObject();
	private static JsonObject jsonPosition = new JsonObject();
	private static JsonObject jsonPositionData = new JsonObject();
	
	//get first a timeStamp of the outerMap, then a position of the innerMap. This call will return the amount of information of one TimeStamp and Position. 
	
//	private static boolean isReady = false;   
	private static int timeStamp;
	
//	private static PropertyChangeListener propertyChangeListener = null;
	
	
	public PositionExpressionCacheSorter(boolean firstLayer, boolean secondLayer) {	
		if(firstLayer == true && secondLayer == true) {
			ProjectProviderInstance.getObservableTimeStamp().addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
	    			if("CurrentTimeStampChanged".equals(event.getPropertyName())) {
	    			
	    				for(int i = 0; i < 15; i++) {
							if(ProjectProviderInstance.getTimeStamp() > 3) {	//if(isReady) {
								
								JsonObject jsonTSObject = null;
								try {
									jsonTSObject = jsonTimeStamps.get("TimeStamp" + PositionExpressionCacheSorter.timeStamp).getAsJsonObject();
								} catch(NullPointerException e) {
									try {
										System.out.println("out");
										Thread.sleep(50);
										continue;
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									continue;
	//								e.printStackTrace();	//Not needed!
								}
								if(jsonTSObject.get("Position " + 1)  != null &&
										jsonTSObject.get("Position " + 3)  != null &&
											jsonTSObject.get("Position " + 5)  != null &&
												jsonTSObject.get("Position " + 21)  != null &&
													jsonTSObject.get("Position " + 23)  != null &&		//add new PositionExpressionLayer to the cache with data from Sensor Nr.
														jsonTSObject.get("Position " + 25)  != null) {	//1, 3, 5, 21, 23 and 25.
									try {
										/*PositionExpressionLayerCache.positionExpressionLayers.add(*/new PositionExpressionLayer(
												PositionExpression.getPositionExpressionByKeyString(jsonTSObject.get("Position " + 1).getAsJsonObject().get("KeyString").toString(), true),						
												PositionExpression.getPositionExpressionByKeyString(jsonTSObject.get("Position " + 3).getAsJsonObject().get("KeyString").toString(), true),
												PositionExpression.getPositionExpressionByKeyString(jsonTSObject.get("Position " + 5).getAsJsonObject().get("KeyString").toString(), true),
												PositionExpression.getPositionExpressionByKeyString(jsonTSObject.get("Position " + 21).getAsJsonObject().get("KeyString").toString(), true),
												PositionExpression.getPositionExpressionByKeyString(jsonTSObject.get("Position " + 23).getAsJsonObject().get("KeyString").toString(), true),
												PositionExpression.getPositionExpressionByKeyString(jsonTSObject.get("Position " + 25).getAsJsonObject().get("KeyString").toString(), true)
												)/*)*/;
										
										jsonTSObject = null;
										break;
									} catch (NullPointerException ex) {
										try {
											Thread.sleep(50);
											System.out.println("Wait " + i);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										continue;
									} catch (IOException ex) {
						// 			TODO Auto-generated catch block
										ex.printStackTrace();
										new PositionExpressionCacheSorter(firstLayer, secondLayer);
										
									}
								}
							} else {
								break;
							}
	    				}

						PositionExpressionCacheSorter.timeStamp = ProjectProviderInstance.getTimeStamp();
	    			}
	    		};
			});
		} else {
			try {
				throw new IOException("The layer, for which the PositionExpressionCache should run, is/are not given!");
			} catch (IOException e) {
		// 	TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	private static int timeStampSuffix = 25;
	private static int timeStampEnd = 25;
	
	private static void addTimeStampToJson(int timeStamp) {
		if(jsonTimeStamps.get("TimeStamp" + timeStamp) == null) {
			if(timeStamp % timeStampEnd == 0) {
				jsonTimeStamps = new JsonObject();
//				timeStampSuffix += timeStampEnd;
			}
			
			jsonPosition = new JsonObject();
			jsonTimeStamps.add("TimeStamp" + timeStamp, jsonPosition);
		}
	}
	
	private static void addPositionToTimeStamp(int timeStamp, int position) {
		if(jsonTimeStamps.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position) == null) {
			jsonPositionData = new JsonObject();
			jsonPosition.add("Position " + position, jsonPositionData);
		}
	}
	
	/*
	 * Now, only the first data gets added to the JSON-file. Means, when we have the same timeStamp and sensor number for 3 different times - this event
	 * occurs very often -, we only work with the first data and have no access to the other 3 different distances.
	 * Due to having not solved this problem, all data are getting taken and the average-measured-distance gets displayed.
	 */
	
	
	private static double average;
	private static double lastPercentage = 1;
	private static HashMap<Integer, HashMap<Integer, Double>> countMap = new HashMap<Integer, HashMap<Integer, Double>>();
	private static HashMap<Integer, HashMap<Integer, Double>> lastPercentageMap = new HashMap<Integer, HashMap<Integer, Double>>();

	
	private static void addDataToTimeStampPosition(int timeStamp, int position, String keyString, int measuredDistance, int sensorNumber) {
		addTimeStampToJson(timeStamp);
		addPositionToTimeStamp(timeStamp, position);
		
//		System.out.println("POSITION: " + position);
//		System.out.println("DISTANCE: " + measuredDistance);
		
		if(countMap.get(timeStamp) == null) {
			countMap.put(timeStamp, new HashMap<Integer, Double>());
		}
		try {
			if(!"{}".equals(jsonTimeStamps.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position).toString())) {
				countMap.get(timeStamp).replace(sensorNumber, countMap.get(timeStamp).get(sensorNumber) + 1.0);
				/*
				 * lastPercentage = (counts - 1) / counts
				 * Counts and lastPercentage gets set individually for every timeStamp and sensorNumber.
				 * Thus, counts and lastPercentage does not get influenced by any other timeStamp-sensorNumber-data.
				 * E.g.: counts and lastPercentage is individually for timeStamp-1 + sensorNumber-1 and timeStamp-1 + sensorNumber-23
				 * Consequently, the average of all data returned from the sensors is shown in the positionExpression.
				 */
				lastPercentage = ((countMap.get(timeStamp).get(sensorNumber) - 1d) / 
										countMap.get(timeStamp).get(sensorNumber));
				lastPercentageMap.get(timeStamp).replace(sensorNumber, lastPercentage);
				
	//			System.out.println("PERCENTAGE " + lastPercentage);
				
				average = ((measuredDistance) * (1d - lastPercentage)) +  
							(Integer.parseInt(jsonTimeStamps.get("TimeStamp" + timeStamp).getAsJsonObject()
								.get("Position " + position).getAsJsonObject().get("Distance").toString()) * lastPercentage);
				
				jsonTimeStamps.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position).getAsJsonObject().remove("KeyString");
				jsonTimeStamps.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position).getAsJsonObject().remove("Distance");
				
				//Create new PositionExpression, sensorNumber is not a PositionExpressionNumber!
				jsonTimeStamps.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position).getAsJsonObject().add("KeyString", 
								new JsonPrimitive(new PositionExpression(sensorNumber, timeStamp, (int) Math.round(average), false).getKeyString()));
				jsonTimeStamps.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position).getAsJsonObject().add("Distance", 
								new JsonPrimitive((int) Math.round(average)));
				
	//			System.out.println("AVERAGE: " + average);
			} else {
				countMap.get(timeStamp).put(sensorNumber, 1.0);
				lastPercentageMap.put(timeStamp, new HashMap<Integer, Double>());
				lastPercentageMap.get(timeStamp).put(sensorNumber, (double) 1.0);
				jsonPositionData.add("KeyString", new JsonPrimitive(keyString));
				jsonPositionData.add("Distance", new JsonPrimitive(measuredDistance));
			}
		} catch(NullPointerException ex) {
			//Do nothing. Somethimes, adding a timestamp is not possible. By catching this exception, the code doesn't interrupt.
			return;
		}
//		try {
//			Files.write(Paths.get("positionExpressionCache/" + /*directoryTime.toString() + "/*/"cacheTill" + timeStampSuffix + ".json"), new Gson().toJson(PositionExpressionCacheSorter.jsonTimeStamps).getBytes());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static void addPositionExpressionToJson(PositionExpression pe) {
		addDataToTimeStampPosition(pe.getTimeStamp(), pe.getPositionNumber(), pe.getKeyString(), pe.getMeasuredDistance(), pe.getSensorNumber());
	}
}
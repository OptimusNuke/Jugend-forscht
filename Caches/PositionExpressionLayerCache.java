package Caches;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import setup.Expressions.PositionExpression;

public class PositionExpressionLayerCache {
	
	private static int file_suffix = 25;
	private static int file_end = 25;

	public static JsonObject jsonPositionLayer = new JsonObject();
	private static JsonObject jsonPosition = new JsonObject();
	private static JsonObject jsonPositionData = new JsonObject();
	
	private static void addTimeStampToJson(int timeStamp) {
		if(jsonPositionLayer.get("TimeStamp" + timeStamp) == null) {
			if(timeStamp % file_end == 0) {
				jsonPositionLayer = new JsonObject();
				file_suffix += file_end;
			}
			
			jsonPosition = new JsonObject();
			jsonPositionLayer.add("TimeStamp" + timeStamp, jsonPosition);
		}
	}
	
	private static void addPositionToTimeStamp(int timeStamp, int position) {
		if(jsonPositionLayer.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position) == null) {
			jsonPositionData = new JsonObject();
			jsonPosition.add("Position " + position, jsonPositionData);
		}
	}
	
	private static void addDataToTimeStampPosition(int timeStamp, int position, String keyString, int measuredDistance) {
		addTimeStampToJson(timeStamp);
		addPositionToTimeStamp(timeStamp, position);
		if("{}".equals(jsonPositionLayer.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + position).toString())) {
			jsonPositionData.add("KeyString", new JsonPrimitive(keyString));
			jsonPositionData.add("Distance", new JsonPrimitive(measuredDistance));
		}
		
		try {
			Files.write(Paths.get("positionExpressionLayerCache/layerTill" + file_suffix + ".json"), new Gson().toJson(PositionExpressionLayerCache.jsonPositionLayer).getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPositionExpressionToJson(PositionExpression pe) {
		if(pe != null) {
			addDataToTimeStampPosition(pe.getTimeStamp(), pe.getPositionNumber(), pe.getKeyString(), pe.getMeasuredDistance());
		}
	}
	
}

package setup.Expressions;
 
import java.io.IOException;

public class ArcExpression {
	
	private byte sensorNumber;
	private int timeStamp;
	private int measuredDistance;

	private int layerNumber = 0;
	
//	private int angleAtPerson = 0;
	 
//	private JFPoint topLeftCorner = new JFPoint(); 
//	private JFPoint  bottomRightCorner = new JFPoint(); 
//	private double height;
	private String keyString = "";
//	private boolean isUsedForCalculation = false;
//	private boolean isDrawn = false;
	
	public ArcExpression(byte sensorNumber, int timeStamp, int measuredDistance) {
		setMeasuredDistance(measuredDistance);
		setTimeStamp(timeStamp);
		initialize(sensorNumber);
		keyString = createString();
	}
	
	public String getKeyString() {
		return keyString;
	}
	
	public byte getSensorNumber() {
		return sensorNumber;
	}

	public int getMeasuredDistance() {
		return measuredDistance;
	}
	
	public void setMeasuredDistance(int measuredDistance) {
		this.measuredDistance = measuredDistance;
	}

//	public JFPoint getTopLeftCorner() {
//		return topLeftCorner;
//	}
//
//	public JFPoint getBottomRightCorner() {
//		return bottomRightCorner;
//	}
//
//	public double getHeight() {
//		return height;
//	}
	
	private void initialize(byte sensorNumber) {
		this.sensorNumber = sensorNumber;
//		if((sensorNumber == 1) || (sensorNumber == 4)) {
//			setAngleAtPerson(30);
////			System.err.println(sensorNumber + " is connected with " + getAngleAtPerson());
//		} else if ((sensorNumber == 3) || (sensorNumber == 6)) {
//			setAngleAtPerson(-30);
////			System.err.println(sensorNumber + " is connected with " + getAngleAtPerson());
//		} else {
//			setAngleAtPerson(0);
////			System.err.println(sensorNumber + " is connected with " + getAngleAtPerson());
//		}
		
		if((sensorNumber >= 1) && (sensorNumber <= 3)) {
			setLayerNumber(1);
//			System.err.println(sensorNumber + " is connected with " + getAngleAtPerson());
		} else if ((sensorNumber >= 4) && (sensorNumber <= 6)) {
			setLayerNumber(2);
//			System.err.println(sensorNumber + " is connected with " + getAngleAtPerson());
		} else if ((sensorNumber >= 7) && (sensorNumber <= 8)) {
			setLayerNumber(3);
//			System.err.println(sensorNumber + " is connected with " + getAngleAtPerson());
		} else if ((sensorNumber >= 9) && (sensorNumber <= 10)) {
			setLayerNumber(4);
		}
	}
	
//	private void setAngleAtPerson(int angle) {
//		angleAtPerson = angle;
//	}
//	
//	public int getAngleAtPerson() {
//	return angleAtPerson;
//}
	
	private void setLayerNumber(int layerNumber) {
		this.layerNumber = layerNumber;
	}
	
	public int getLayerNumber() {
		return layerNumber;
	}
	
	public int getTimeStamp() {
		return timeStamp;
	}

	private void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	
//	public boolean isUsedForCalculation() {
//		return isUsedForCalculation;
//	}
//
//	public void setUsedForCalculation(boolean isUsedForCalculation) {
//		this.isUsedForCalculation = isUsedForCalculation;
//	}
//	
//	public boolean isDrawn() {
//		return isDrawn;
//	}
//
//	public void setDrawn(boolean isDrawn) {
//		this.isDrawn = isDrawn;
//	}
	
	private String createString() {
		/*
		 * Example for a keyString:
		 * "S=3;ts=20;dt=3540"
		 */
		byte sensorNumber;
		int timeStamp;
		sensorNumber = getSensorNumber();
		timeStamp = getTimeStamp();
		
		return ("S=" + sensorNumber					//"S=" means Sensor
				+ ";ts=" + timeStamp				//"ts=" means timeStamp
				+ ";dt=" + getMeasuredDistance());	//"dt=" means distance
	}

	
	
	public static ArcExpression arcExpressionByString(String expressionString) throws IOException {
		if(expressionString == null || expressionString.isEmpty()) {
			return null;
		}
		
		String subString = "";
		byte sensor = 0;
		int time = 0;
		int distance = 0;
		
		/*
		 * Example for a keyString:
		 * "S=3;ts=20;dt=3540"
		 */
		
		if(!(expressionString.startsWith("S=") && 
				expressionString.indexOf("S=") < expressionString.indexOf(";ts=") 
					&& expressionString.indexOf(";ts=") < expressionString.indexOf(";dt="))) {
			System.err.println("Input string " + expressionString + " is not a valid arcExpressionKeyString!");
			return null;
		}
		
		subString = expressionString.substring(expressionString.indexOf("S=") + 2,
					expressionString.indexOf(";ts="));
		
		try {
			sensor = Byte.parseByte(subString);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			System.err.println("Problems with generating a PositionExpression from a keyString!\n\tSensor: " + sensor);
			return null;
		}
		
		
		subString = expressionString.substring(expressionString.indexOf("ts=") + 3,
					expressionString.indexOf(";dt="));
		
		try {
			time = Integer.parseInt(subString);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			System.err.println("Problems with generating a PositionExpression from a keyString!\n\tSensor: " + sensor + "; time: " + time);
			return null;
		}
		
		subString = expressionString.substring(expressionString.indexOf("dt=") + 3,
					expressionString.length());
		
		try {
			distance = Integer.parseInt(subString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.err.println("Problems with generating a ArcExpression from a keyString!\n\tSensor: " + sensor + "; time: " + time + "; distance: " + distance);
			return null;
		}
		
		subString = "";
		
		if(sensor > 0 && time > 0 && distance > 0) {
			return new ArcExpression(sensor, time, distance);
		} else {
			System.err.println("Problems with generating a ArcExpression from a keyString!\n\tSensor: " + sensor + "; time: " + time + "; distance: " + distance);
			return null;
		}
	}
}

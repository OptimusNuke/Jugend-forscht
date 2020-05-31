package setup.Expressions;
 
import java.io.IOException;

public class PositionExpression {
	
	private int positionNumber;
	
	private int sensorNumber;
	
	private int measuredDistance; 

	private int timeStamp;
	
	private String keyString = "";
	
	private boolean isDrawn = false;
	
	public PositionExpression(ArcExpression arc) {
		setSensorNumber(arc.getSensorNumber());
		setPositionNumberBySensorNumber(arc.getSensorNumber());
		setTimeStamp(arc.getTimeStamp());
		setMeasuredDistance(arc.getMeasuredDistance());
		setKeyString();
	}
	
	public PositionExpression(int positionNumber, int timeStamp, int distance, boolean byPositionExpression) {
		if(byPositionExpression == true) {
			setPositionNumberByPositionNumber(positionNumber);
		} else {
			setPositionNumberBySensorNumber(positionNumber);
		}
		
		setTimeStamp(timeStamp);
		setMeasuredDistance(distance);
		setKeyString();
	}
	
	public static PositionExpression getPositionExpressionByArcExpression(ArcExpression arc) {
		return new PositionExpression(arc);
	}

	public static PositionExpression getPositionExpressionByKeyString(String expressionString, boolean byPositionExpressionString) throws IOException {
		if(expressionString == null) {
			throw new IOException("Problems with generating a PositionExpression from a keyString! The keyString is null!");
		}
		
		String subString = "";
		byte sensorNumber = 0;
		int time = 0;
		int distance = 0;
		
		/*
		 * Example for a keyString:
		 * "PE-S:12;ts:100;dt:3000"
		 */
		
		subString = expressionString.substring(expressionString.indexOf("S:") + 2,
					expressionString.indexOf(";ts:"));
		
		try {
			sensorNumber = Byte.parseByte(subString);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			throw new IOException("Problems with generating a PositionExpression from a keyString!\n\tSensor: " + sensorNumber);
		}
		
		
		subString = expressionString.substring(expressionString.indexOf("ts:") + 3,
					expressionString.indexOf(";dt:"));
		
		try {
			time = Integer.parseInt(subString);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			throw new IOException("Problems with generating a PositionExpression from a keyString!\n\tSensor: " + sensorNumber + "; time: " + time);
		}
		
		subString = expressionString.substring(expressionString.indexOf("dt:") + 3,
					expressionString.length() - 1);
		
		try {
			distance = Integer.parseInt(subString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new IOException("Problems with generating a ArcExpression from a keyString!\n\tSensor: " + sensorNumber + "; time: " + time + "; distance: " + distance);
		}
		
		subString = "";
		
		if(sensorNumber > 0 && time > 0 && distance > 0) {
			return new PositionExpression(sensorNumber, time, distance, byPositionExpressionString);
		} else {
			throw new IOException("Problems with generating a PositionExpression from a keyString!\n\tSensor: " + sensorNumber + "; time: " + time + "; distance: " + distance);
		}
	}
	
	public int getPositionNumber() {
		return positionNumber;
	}

	public void setPositionNumber(int positionNumber) {
		this.positionNumber = positionNumber;
	}
	
	public void setPositionNumberBySensorNumber(int sensorNumber) {
		if(sensorNumber == 1) {
			positionNumber = 1;
		} else if(sensorNumber == 2) {
			positionNumber = 3;
		} else if(sensorNumber == 3) {
			positionNumber = 5;
		} else if(sensorNumber == 7) {
			positionNumber = 13;
		} else if(sensorNumber == 4) {
			positionNumber = 21;
		} else if(sensorNumber == 5) {
			positionNumber = 23;
		} else if(sensorNumber == 6) {
			positionNumber = 25;
		} else {
			throw new IllegalArgumentException("The sensorNumber " + sensorNumber + " is invalid!");
		}
	}
	
	public void setPositionNumberByPositionNumber(int positionNumber) {
		if(positionNumber >= 1 && positionNumber <= 25) {
			this.positionNumber = positionNumber;
		} else {
			throw new IllegalArgumentException("The sensorNumber " + positionNumber + " is invalid!");
		}
	}
	
	public int getSensorNumber() {
		return sensorNumber;
	}
	
	private void setSensorNumber(int sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

	public int getMeasuredDistance() {
		return measuredDistance;
	}

	public void setMeasuredDistance(int distance) {
		this.measuredDistance = distance;
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getKeyString() {
		return keyString;
	}

	private void setKeyString() {
		/*
		 * Example for a keyString:
		 * "PE-S:12;ts:100;dt:3000"
		 */
		
		if(getPositionNumber() > 0 && getTimeStamp() > 0 && getMeasuredDistance() > 0) {
			keyString = "PE-S:" + getPositionNumber() + ";ts:" + getTimeStamp() + ";dt:" + getMeasuredDistance();  
		} else {
			keyString = "";
		}
	}

	public boolean isDrawn() {
		return isDrawn;
	}

	public void setDrawn(boolean isDrawn) {
		this.isDrawn = isDrawn;
	}
}
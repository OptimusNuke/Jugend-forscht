package threads.Communication;
 
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import setup.ProjectProviderInstance;
import setup.Expressions.PositionExpression;
import socket.ArduinoSocketStarter;

public class VibrationMotorToArduino {
	
	public VibrationMotorToArduino() {
		
	}
	
	public void writeDataToArduino(PositionExpression... peVrg) {
		int[] vibration_data = new int[25];
		
		int position = 0;
		
		for(PositionExpression pe : peVrg) {
			if(pe == null) {
				vibration_data[position] = 0;
				continue;
			}
			
			vibration_data[position] = convertDistanceToIntensityOfVibration(pe.getMeasuredDistance());
			
			position++;
		}
		
		ArduinoSocketStarter.arduinoConnection.write(ProjectProviderInstance.STARTBYTE_ARDUINO + "[");
//		System.out.println(ProjectProviderInstance.STARTBYTE_ARDUINO + "[");
		
		for(int b : vibration_data) {
			ArduinoSocketStarter.arduinoConnection.write(String.valueOf(b) + ", ");
			//System.out.print(b + ", ");
		}
		
		ArduinoSocketStarter.arduinoConnection.write("]" + ProjectProviderInstance.ENDBYTE_ARDUINO);
//		System.out.println("]" + ProjectProviderInstance.ENDBYTE_ARDUINO);
		
		
		try {
			ArduinoSocketStarter.arduinoConnection.getServer().getClientConnection().getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int convertDistanceToIntensityOfVibration(int distance) {
		if(distance >= 0 && distance <= 500) {
//			System.out.println(distance + "\t" + 255);
			return 255;
		} else if(distance >= 501 && distance <= 2000) {
//			For formula look in the google doc!
//			System.out.println(distance + "\t" + (int) ( 180d + 74d * (1d - ((distance - 501d) / 1499d))));
			return (int) Math.round((180d + 74d * (1d - ((distance - 501d) / 1499d))));
		} else if(distance >= 2001 && distance <= 3000) {
//			System.out.println(distance + "\t" + 140);
			return 140;
		} else if (distance >= 3001 && distance <= 5000) {
//			System.out.println(distance + "\t" + 0);
			return 1;
		} else {
			System.err.println("Unable to calculate a vibration data for distance " + distance + "!");
			return 1;
		}
	}
	
	private static ReentrantLock lock = new ReentrantLock(true);
	
	//Method which gets called by the SensorDataReaderClass
	//Sends single data; 4 threads can access on this method by the same time
	public static void writeSingleVibrationData(byte sensorNumber, int distance) {
		lock.lock();
		
		try {
//			Example-String for leg-vibration-data: 
			ArduinoSocketStarter.arduinoConnection.write(ProjectProviderInstance.STARTBYTE_ARDUINO + "V");
			//System.out.print(ProjectProviderInstance.STARTBYTE_ARDUINO + "V");	
			
			ArduinoSocketStarter.arduinoConnection.write(String.valueOf(sensorNumber) + ":");
			//System.out.print(sensorNumber + ":");
			
			ArduinoSocketStarter.arduinoConnection.write(String.valueOf(convertDistanceToIntensityOfVibration(distance)));
			//System.out.print(String.valueOf(convertDistanceToIntensityOfVibration(distance)) + "\t" + distance + "\n");
			
			ArduinoSocketStarter.arduinoConnection.write(ProjectProviderInstance.ENDBYTE_ARDUINO);
			//System.out.println(ProjectProviderInstance.ENDBYTE_ARDUINO);
			
			ArduinoSocketStarter.arduinoConnection.getServer().getClientConnection().getOutputStream().flush();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
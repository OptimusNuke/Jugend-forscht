package threads.Communication;
 
import java.io.IOException;
import java.util.ArrayList;

//import Begin.ProjectRunnerPI;
import setup.ProjectProviderInstance;
import socket.ArduinoSocketStarter;


public class ConnectionFromArduinoThread extends Thread {
	
	public ConnectionFromArduinoThread() throws IOException {		
		System.out.println("Der ConnectionFromArduinoThread ist da!");
	}
	
	@Override
	public void run() {
		try {
			new ArrayList<Byte>();
			new ArrayList<Byte>();
			String string = "";
			String transmitOrNot = "";
			int sensorNumber;
			String readLine = "";
			
			while(true) {
				try {
					readLine = ArduinoSocketStarter.arduinoConnection.readLine();
				} catch (java.net.SocketException ex) {
					System.err.println("### Connection reset!");
//					new ArduinoSocketStarter(ProjectRunnerPI.ARDUINO_PORT);
				}
				
				//readLine must contain the char '@' and the char '#' to be able to identify the received data is suitable.
				if(readLine.isEmpty() 
						|| !readLine.contains(ProjectProviderInstance.STARTBYTE_ARDUINO)
						|| !readLine.contains(ProjectProviderInstance.ENDBYTE_ARDUINO)) {
					Thread.sleep(50);
					continue;
				}
				
				//Gets only the 
				string = readLine.substring(readLine.indexOf(ProjectProviderInstance.STARTBYTE_ARDUINO), 
						readLine.indexOf(ProjectProviderInstance.ENDBYTE_ARDUINO));
					
				//System.out.println("FROM ARDUINO: " + string);
					
				sensorNumber = 0;
				
				try {
					sensorNumber = Integer.parseInt(string.substring(string.indexOf("S-") + 2, string.indexOf(":")));
					
				} catch (NumberFormatException e) {
					System.err.println("Couldn't parse the sensorNumber from a Arduino-String");
					e.printStackTrace();
				}
				
				transmitOrNot = string.substring(string.indexOf(":") + 1, string.length());
				
//				System.out.println("TRANSMITORNOT: " + transmitOrNot);
//				System.out.println("NUMBER: " + sensorNumber);
				
				
				//Changing the sensor number from 7 to 8 and 8 to 7: The Accelerometers are this way attached to the I2C-Multiplexer
				//numbers do not suit: Usage of multiplexer was only possible with connecting the MPU6050 to random pins...
				if(sensorNumber == 7) {
					sensorNumber = 8;
					//System.out.println("Changed 7 to 8!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else if(sensorNumber == 8) {
					sensorNumber = 7;
					//System.out.println("Changed 8 to 7!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				}
				
				if(sensorNumber >= 7 && sensorNumber <= 10) {
					if("p".equals(transmitOrNot)) {
						//System.out.println("Sensor " + sensorNumber + " is not transmitting waves!");
						transmitOrNot = "";
						ProjectProviderInstance.sensors.getSensorByNumber((byte) sensorNumber).stopTransmittingWaves();
					} else if("f".equals(transmitOrNot)) {
						//System.out.println("Sensor " + sensorNumber + " is transmitting waves!");
						transmitOrNot = "";
						ProjectProviderInstance.sensors.getSensorByNumber((byte) sensorNumber).transmitWaveDirect();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		//} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
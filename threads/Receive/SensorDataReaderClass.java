package threads.Receive;
 
/**
 * @author Tim_Wieland
 * @version 0.2
 *
 * This thread:
 *  -   Is a thread-daemon.
 *  -	Must be initialized with a RS232-cable COM-Port
 *  -	Will read data returned from sensors and put them into the SensorDataCache.
 *  
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import Caches.SensorDataCache;
import setup.ProjectProviderInstance;
import socket.ArduinoSocketStarter;
import threads.Communication.VibrationMotorToArduino;

public class SensorDataReaderClass {
	
	//Maximal change of values between to measured distances 
	//	-when the old value is 500, the next value can become maximal 1750 to stabilize the data from abrupt changes
	private static final int RATE_LIMIT = 500;
	
	//Variables which are needed intern to provide a smooth running thread.
	private SerialPort serialPort;
	private byte readByte;
	//Stores the last distance which is not 5000 - 5000 =^ no object found. 
	private int $lastDataRead = 5000;
	private int lastTimeStamp;
	
	/**
	 * 
	 * @param port Port from the connected RS232-cable
	 * @param sensorNumber Number from the sensor
	 * 
	 * This method initializes the variable sensorNumber,
	 * checks, if the port is usable and then initializes and opens the port.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public SensorDataReaderClass(String serialPortName, byte sensorNumber, SerialPort s) throws InterruptedException, IOException {
		lastTimeStamp = ProjectProviderInstance.getTimeStamp();
		this.serialPort = s;
		
//		Endless loop waiting till every sensor is connected and has its own listener.	
		while(!ProjectProviderInstance.isEverythingConnected()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		Class to check, if this port can be used.
//		SensorChecklist.checkIfUsable(this.serialPort.getSystemPortName());	
		
		System.out.println("### A SensorDataReader for sensor " + sensorNumber + " got activated: " + 
		this.serialPort.addDataListener(new SerialPortDataListener() {
			List<Byte> bytesRead = new ArrayList<Byte>();
			String measuredDistance = "";
			int falseCounter = 42;
			
			@Override
			public int getListeningEvents() { 
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; 
			}
			   
			@Override
			public void serialEvent(SerialPortEvent event) {
				if(event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
					return;
				}
				
//				Listener fires, if the sensor has returned any bytes.
//				If true, the byte-List bytesRead is getting fulfilled with the readByte.
//				If the readByte is between 0 and 9, the following task is to store all measured distances 
//				into the SensorDataCache by calling the addElement-method from the SensorDataCache.
					
				try {
						
//					Checks, if any data is in the inputStream
					if (serialPort.getInputStream().available() > 0) {
							
//						Calls the method nextByteIsAvailable()
//						Puts the readByte into the byte-List bytesRead if not sorted out.
						while(nextByteIsAvailable()) {
							bytesRead.add(readByte);
						}
							
//						Iterates through the byte-List bytesRead
						for(byte currentByte : bytesRead) {
								
//							The sensor returns a R followed by 4 ASCII-Chars describing the measured
//							distance in millimeter. The 4 ASCII-Chars are at this point in the List
//							bytes Read. We put now all 4 chars into the String measuredDistance.
							measuredDistance += (char) currentByte;
						}
							
//						The data stored in measuredDistance is checked that it can be considered as real
//						data. If the distance is "7354", an error occurred - this data can be found when
//						the sensor restarts and writes his script. The "7354" is the sensor model.
//						The next 41 bytes are useless, this is why we skip them - we shouldn't ignore them,
//						because part of them are numbers that can look like measured distances.
							
						if(measuredDistance.equals("7354")) {
							falseCounter = 1;
							System.err.println("Error at sensor Nr. " + sensorNumber + ". This sensor restarted.");
						} else if(falseCounter != 42) {
							falseCounter++;
						}
						
//						Add the data to the SensorDataCache.
						if(measuredDistance.length() == 4 && falseCounter == 42 && ProjectProviderInstance.getTimeStamp() != 0) {
//							Actual Measured distance gets modified and maximal increased by the RATE_LIMIT  
							measuredDistance = modifyWithRateLimit(measuredDistance);
							
							if((sensorNumber >= 7 && sensorNumber <= 10) && ArduinoSocketStarter.hasConnectionToArduino 
									&& (lastTimeStamp/* + 1*/) < ProjectProviderInstance.getTimeStamp()) {
								lastTimeStamp = ProjectProviderInstance.getTimeStamp();
								VibrationMotorToArduino.writeSingleVibrationData(sensorNumber, Integer.parseInt(measuredDistance));
							} else {
								SensorDataCache.addElement(sensorNumber, ProjectProviderInstance.getTimeStamp(), 
									Integer.parseInt(measuredDistance));
							}
						}
							
//						The data in "bytesRead" and "measuredDistance" is not needed anymore.
						bytesRead.clear();
						measuredDistance = "";
						//Send a null byte to clear the buffer of the RS232
						serialPort.getOutputStream().write('\0');
						Thread.sleep(25);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}));
	}
	
	/**
	 * 
	 * Initializes the variable 'readByte' with the next byte.
	 * This byte can't be -1, 0 or 13 - but if true, this method returns false.
	 * If this byte is 'R', the next byte will be read.
	 * 
	 * @return if the next byte returned from the sensor is between 0 and 9
	 * @throws IOException
	 * 
	 */
	private boolean nextByteIsAvailable() throws IOException {
		readByte = (byte) serialPort.getInputStream().read();
		
//		Returns false, if the readByte is -1, 0 or 13 - if it is (byte) R, the next byte will be read. 
		
		if(readByte == 13 || readByte == 0) {
			return false;
		} else if(readByte == 'R') {
			readByte = (byte) serialPort.getInputStream().read();
			
			if(readByte == 13 || readByte == 0) {
				return false;
			} else if(readByte == -1) {
				return false;
			}
		} else if(readByte == -1) {
			return false;
		}
		
		return (readByte >= (byte) '0' && readByte <= (byte) '9');
	}
	
	private String modifyWithRateLimit(String m) {
		int dist = Integer.parseInt(m);
		int dif = dist - $lastDataRead;
		
		//difference is smaller than the rate limit (change is allowed)
		if(Math.abs(dif) <= RATE_LIMIT) {
			$lastDataRead = dist;
			return m;
		}
		
		//difference is greater than the rate limit - change only to the rate limit
		//Distance needs to get increased by RATE_LIMIT: Difference is positive 
		if(dif > 0) {
			$lastDataRead += RATE_LIMIT;
			return Integer.toString($lastDataRead);
		}
		
		//Distance needs to get decreased by RATE_LIMIT: Difference is negative
		if(dif < 0) {
			$lastDataRead -= RATE_LIMIT;
			return Integer.toString($lastDataRead);
		}
		System.err.printf("\n### Problems with modifying values in %s with a old value of %d and a new value of %d", "threads.Receive.SensorDataReaderClass", $lastDataRead, dist);
		return m;	 
	}
}
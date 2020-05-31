package Tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;

public class SensorTesting {
	
	private static double DISTANCE_TO_OBJECT = 1; 
	private final static Integer DATA_LIMIT = 1000;
	private final static Integer WAVE_SPEED = 341;
	
	private static ArrayList<Integer> sData = new ArrayList<>();
	
	private static byte readByte;

	private static SerialPort serialPort = null;
	
	public static void main(String[] args) {
		
		DISTANCE_TO_OBJECT = Double.parseDouble(JOptionPane.showInputDialog("Geb die Distanz ein!")) / 100;
		
		serialPort = SerialPort.getCommPort("COM4");
		serialPort.openPort();
		
		List<Byte> bytesRead = new ArrayList<Byte>();
		String measuredDistance = "";
		int falseCounter = 42;
		int run = 0;
			

		while(true) {
			try {
	//					Checks, if any data is in the inputStream
				if (serialPort.getInputStream().available() > 0) {
						
	//		
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
						System.err.println("Error: A sensor restarted.");
					} else if(falseCounter != 42) {
						falseCounter++;
					}
					
	//						Add the data to the SensorDataCache.
					if(measuredDistance.length() == 4 && falseCounter == 42) {
						System.out.println(run + "\t" + measuredDistance);
						sData.add(Integer.parseInt(measuredDistance));
						run++;
						if(run >= DATA_LIMIT) {
							calculateTable();
							return;
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
	private static boolean nextByteIsAvailable() throws IOException {
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
	
//	DISTANCE_TO_OBJECT
//	sData
//	WAVE_SPEED
	
	private static void calculateTable() {
		double theo_flight_time = (DISTANCE_TO_OBJECT / WAVE_SPEED) * 1000_000;
		
		Double sum = 0d;
		for(Integer i : sData) {
			sum += i;
		}
		
		double average_distance = sum / DATA_LIMIT;
		
		double average_flight_time = ((average_distance / 1000)  / WAVE_SPEED) * 1000_000;
		
		double absolute_error = Math.abs(DISTANCE_TO_OBJECT - average_distance / 1000) * 1000;
		
		double relative_error = (absolute_error / (DISTANCE_TO_OBJECT * 1000)) * 100;
		
		System.out.printf("Distance to Object: %s\n"
				+ "Theoretical time of flight: %s\n"
				+ "Average measured distance: %s\n"
				+ "Flight time: %s\n"
				+ "Absolute error: %s\n"
				+ "Relative error: %s", 
				DISTANCE_TO_OBJECT, theo_flight_time, average_distance, average_flight_time, absolute_error, relative_error);
	}
}
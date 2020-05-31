package setup.Start;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import com.fazecast.jSerialComm.SerialPort;

import setup.ProjectProviderInstance;
import threads.InternUses.SerialPortListener; 

public class SetSerialPortsWithListeners {
	
//	ArrayList with all serialPortNames
	private static ArrayList<String> portList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean remove(Object o) {
//			System.out.println("REMOVED: " + o);
			return super.remove(o);
		}
	};
	
	private static byte currentSensorNumber;
	private static boolean isBlocking = false;
	private static byte doubleSensorNumber = 0;
	private static boolean isConArduino = false;
	private static boolean isConPC = false;
	private static boolean isReadyAgain = true;
	
	/**
	 * Start for every port a thread, then start with turning pin 4 up from different sensors.
	 * If a thread receives data, this thread will be closed and a new SensorDataReaderThread will be started with the
	 * sensorNumber (known from pin 4) and the port (known from the portList).
	 * If the char '$' is received (this char will send the arduino), the port from the arduino is known.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	
	public static void setSerialPorts() throws IOException, InterruptedException {
		for(SerialPort port : SerialPort.getCommPorts()) {
			if(!("ttyS0".equals(port.getSystemPortName()) || "ttyAMA0".equals(port.getSystemPortName()))) {
				getPortList().add(port.getSystemPortName());
			}
		}
		
		launchThreads();
				
		for(byte sensorNumber = 1; getPortList().size() > 0; sensorNumber++) {
			Thread.sleep(500);
//			System.out.println("PORTLISTSIZE: " + getPortList().size());
			
			setCurrentSensorNumber(sensorNumber);
			setIsBlocking(true);
			ProjectProviderInstance.sensors.getSensorByNumber(currentSensorNumber).transmitWavesForPortSetter(3000);			
			if(getIsBlocking() == true) {
				System.err.println("--> Couldn't intialize sensor " + currentSensorNumber + " with a port.");
				setReadyAgain(true);
			}
		}
		ProjectProviderInstance.setIsEverythingConnected(true);
		return;
	}

	static Stack<SerialPortListener> stack = new Stack<SerialPortListener>();
	
	private static void launchThreads() {
		for(int i = 0; i <= getPortList().size() - 1; i++) {
			new SerialPortListener(getPortList().get(i));
//			System.out.println("\t\tThreadlaunch: " + (i + 1) + "\t" + portList.get(i));
		}
	}
	
	public static synchronized byte getCurrentSensorNumber() {
		return currentSensorNumber;
	}
	
	public static void setCurrentSensorNumber(byte number) {
		currentSensorNumber = number;
	}
	
	public static synchronized void setIsBlocking(boolean bol) {
		isBlocking = bol;
	}
	
	public static boolean getIsBlocking() {
		return isBlocking;
	}
	
	public static synchronized ArrayList<String> getPortList() {
		return portList;
	}
	
	public static boolean isConArduino() {
		return isConArduino;
	}

	public static void setConArduino(boolean isConArduino) {
		SetSerialPortsWithListeners.isConArduino = isConArduino;
	}

	public static boolean isConPC() {
		return isConPC;
	}

	public static void setConPC(boolean isConPC) {
		SetSerialPortsWithListeners.isConPC = isConPC;
	}

	public static boolean isReadyAgain() {
		return isReadyAgain;
	}

	public static void setReadyAgain(boolean isReadyAgain) {
		SetSerialPortsWithListeners.isReadyAgain = isReadyAgain;
	}

	public static synchronized void checkForNoDouble(byte sensorNumber) throws IOException {
		if(doubleSensorNumber == sensorNumber || sensorNumber == 0) {
			System.err.println("--> A sensor is writing continously data! Please check the sensor connections!");
			return;
		}
		doubleSensorNumber = sensorNumber;
	}

	public void setPortList(ArrayList<String> portList) {
		SetSerialPortsWithListeners.portList = portList;
	}
}
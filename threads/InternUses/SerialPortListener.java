package threads.InternUses;
 
import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import setup.ProjectProviderInstance;
import setup.Start.SetSerialPortsWithListeners;
import threads.Receive.SensorDataReaderClass;

public class SerialPortListener {
	private static byte newData;
	
	private SerialPort port;
	
	public SerialPortListener(String portName) {
		
		port = SerialPort.getCommPort(portName);
		if(!port.openPort()) {
			SetSerialPortsWithListeners.getPortList().remove(portName);
			SetSerialPortsWithListeners.setIsBlocking(false);
			System.err.println("Problems with opening port " + portName);
		} else {
			System.out.println("### Thread successfully launched for " + portName);
		}
		
		try {
			
			port.getOutputStream().write(0);
			Thread.sleep(10);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Problem: I could only add one listener to the serial port. Means I had to use this while-loop and add afterwards a listener.
//		Removing the listener / closing the port was possible, but then I had to deal with an closed port which can't be opened anymore; this was minimizing the possibilities.
//		Attaching another listener after closing the port was not possible.
		new Thread() {
			@Override 
			public void run() {
				while(true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(port.bytesAvailable() > 0) {
						try {
							newData = (byte) port.getInputStream().read();
//							System.out.println("NEW " + newData);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}	
						if(newData >= 70 && newData <= 200 && SetSerialPortsWithListeners.getCurrentSensorNumber() != 0) {
							try {
								SetSerialPortsWithListeners.checkForNoDouble(SetSerialPortsWithListeners.getCurrentSensorNumber());
								
								ProjectProviderInstance.sensors.addSensorsToUse(ProjectProviderInstance.sensors.getSensorByNumber(SetSerialPortsWithListeners.getCurrentSensorNumber()));

								System.out.println("A SensorDataReader for Sensor " + 
										SetSerialPortsWithListeners.getCurrentSensorNumber() + " can be activated!\t-\t" + portName);

								SetSerialPortsWithListeners.setIsBlocking(false);
								SetSerialPortsWithListeners.getPortList().remove(portName);
								
								new SensorDataReaderClass(portName, SetSerialPortsWithListeners.getCurrentSensorNumber(), port);
								return;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				      }
				  }
			}
		}.start();
	}
}
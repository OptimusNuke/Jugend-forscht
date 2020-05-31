package Tests;

import com.fazecast.jSerialComm.SerialPort;

public class ConnectedUSB {
	public static void main(String[] args) {
		SerialPort[] sp = SerialPort.getCommPorts();
		for(SerialPort port : sp) {
			System.out.println(port.getSystemPortName());
		}
//		System.out.println("".isEmpty());
//		System.out.println(sp[0].getSystemPortName());
	}
}
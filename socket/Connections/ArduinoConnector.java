package socket.Connections;

import java.io.IOException;

import socket.ArduinoSocketStarter;
import threads.Communication.ConnectionFromArduinoThread;

public class ArduinoConnector extends Connector {
	
	public ArduinoConnector(int port) {
		super(port);
		ArduinoSocketStarter.hasConnectionToArduino = true;
		try {
			new ConnectionFromArduinoThread().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

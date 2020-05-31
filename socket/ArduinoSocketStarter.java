package socket;

import socket.Connections.ArduinoConnector;

public class ArduinoSocketStarter {
	
	public static ArduinoConnector arduinoConnection;
	public static boolean hasConnectionToArduino = false; 
	
	public ArduinoSocketStarter(int port) {
		//Starting a socket means waiting for a client to connect.
		//And the server.accept-Method in Server.java blocks the current thread till a connection is built.
		Thread t = new Thread() {
			@Override
			public void run() {
				arduinoConnection = new ArduinoConnector(port);
				//Start a instance which evaluates incoming messages from the python-instance
//						new PythonConnectionEvaluator(pyConnection).start();
			}
		};
		t.start();
	}
}

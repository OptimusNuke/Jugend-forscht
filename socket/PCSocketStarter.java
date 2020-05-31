package socket;
 
import socket.Connections.PythonConnector;

public class PCSocketStarter {
	public static PythonConnector pyConnection;
	public static boolean hasConnectionToPC = false; 
	
	
	public PCSocketStarter(int port) { 
		
		//Starting a socket means waiting for a client to connect.
		//And the server.accept-Method in Server.java blocks the current thread till a connection is built.
		Thread t = new Thread() {
			@Override
			public void run() {
				pyConnection = new PythonConnector(port);
				//Start a instance which evaluates incoming messages from the python-instance
//				new PythonConnectionEvaluator(pyConnection).start();
			}
		};
		t.start();
	}
}

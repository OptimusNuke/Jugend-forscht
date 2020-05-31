package socket.Connections;
 
import java.net.SocketException;

public class PythonConnectionEvaluator extends Thread {

	private PythonConnector connection;
	
	public PythonConnectionEvaluator(PythonConnector connection) {
		this.connection = connection;
		System.out.println("Initialized a new PythonConnectionEvaluator");
		setDaemon(true);
	}
	
	@Override
	public void run() {
		String readString = "";
		
//		while(true) {
			try {
			readString = connection.readLine();
			System.out.println("Read " + readString);
			
			} catch(SocketException ex) {
				ex.printStackTrace();
				connection.getServer().closeClientConnection();
				connection.getServer().closeServer();
//				break;
			}
//		}
	}
}
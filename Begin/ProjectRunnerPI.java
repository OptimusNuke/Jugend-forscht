package Begin;
 
import setup.ProjectProviderInstance;
import socket.ArduinoSocketStarter;
import socket.PCSocketStarter;
import threads.Organize.ArcLayerToPositionLayerThread;

public class ProjectRunnerPI {
	public static final int PC_PORT = 8080;
	public static final int ARDUINO_PORT = 300;
	
	public static void main(String[] args) { 
		
		//Start a new Socket on the PI and wait for the computer/Arduino connect.
		//The connecting PC/Arduino must be in the ? created by the PI.
		//This socket will be (statically) used by the PositionExpressionLayer to send data if a connection is present.
		
		new PCSocketStarter(PC_PORT);
		new ArduinoSocketStarter(ARDUINO_PORT);
		
		//Run the ProjectProviderInstance
		new ProjectProviderInstance().run();
		
		//This thread starts all the sensors and also all SensorDataReader.
		new ArcLayerToPositionLayerThread(true, true).start();
	}
}
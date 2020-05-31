package setup;

 
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author Tim_Wieland
 * @version 0.2
 *
 * This class is the project provider instance, which provides the GpioController for pin changes.
 * Several subclasses will be able to communicate via this class.
 * Providing several threads, most processes are going to be set by this class.
 *  
 */

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinProvider;
import com.pi4j.util.Console;

import Helper.CurrentTimeStamp;
import setup.Expressions.ArcExpression;
import setup.Sensors.SensorConnectionsPin4;
import setup.Start.SetSerialPortsWithListeners;
import threads.Organize.SensorDataLayerOrganizerDaemon;
import threads.Transmission.TransmittingThread;

public class ProjectProviderInstance implements Runnable {

	public static final String STARTBYTE_PC = "*";
	public static final String ENDBYTE_PC = "~";
	
	public static final String STARTBYTE_ARDUINO = "@";
	public static final String ENDBYTE_ARDUINO = "#";
	
	//Used in PythonConnector to write data to a PC
	public static final String START_LAYER = "{LAYER}";
	public static final String END_LAYER = "{LAYEREND}";
	public static final String TIMESTAMP = "TIMESTAMP";
	public static final String POSITION = "pos";
	
	public static final String START_LEG = "{LEG}";
	public static final String END_LEG = "{LEGEND}";
	
	final Console console = new Console();
	
    public ProjectProviderInstance() {
    	console.title("Jugend forscht", "Martin Achtner sowie Tim Wieland", "Version 1.0.0", "Theodor Heuss Gymnasium Aalen");
    	console.promptForExit();
    }
    
    @Override
	public void run() {
		sensors = new SensorConnectionsPin4();
		sensors.stopAllSensorsWithTransmitting();
		
		try {
			System.out.println("Started with connection ports");
			SetSerialPortsWithListeners.setSerialPorts();
			System.out.println("The Organizer thread started");
			new SensorDataLayerOrganizerDaemon().start();
			System.out.println("The transmitting thread started");
			new TransmittingThread().start();
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
    

    
    public static SensorConnectionsPin4 sensors;
	
	private static List<ArcExpression> expressionsToDraw = null;
	
	public static List<ArcExpression> getExpressionsToDraw() {
		return expressionsToDraw;
	}
		
	public static void addExpressionToDraw(ArcExpression expressionToAdd) {
		expressionsToDraw.add(expressionToAdd);
	}
    
	/**
	 * 
	 * Creates package-private static final GpioController 'gpio' 
	 * instance provided for all subclasses
	 * 
	 */
    public static final GpioController gpio = GpioFactory.getInstance();

    /**
     * Creates new private static HashMap 'pins'
     * Key = String of the name of the Pin; Value = Pins
     * This HashMap will be used as a comparison map for 'checkForEligibleAccess(Pin pin)' 
     * to ensure that a pin is never used twice
     * 
     */
    private static HashMap<String, Pin> pins = new HashMap<String, Pin>(PinProvider.allPins().length);
    
    /**
     * 
     * @param pin GPIO from the Raspberry Pi
     * @return the name from this GPIO
     * 
     * Private methode to get the correct Key for the meant pin of gpio
     * 
     */
    
    private static String getKey(Pin pin) {
        return pin.getName();
    }

    
    /**
     * 
     * @param pin GPIO from the Raspberry Pi
     * 
     * Method to check if the wanted pin is already used and the access is eligible
     * 
     */
    
    public static void checkForEligibleAccess(Pin pin) {
        if(pins.containsKey(getKey(pin))) {
            //This pin is not allowed to use - throws IllegalArgumentException
            throw new IllegalArgumentException("The pin " + pin + " is already used! Change pin number!");
        } else {
            //Add the not-used pin to the pins-HashMap with the Key getKey(pin)
            pins.put(getKey(pin), pin);
        }
    }

    private static boolean connectionToPC = false;
    private static SerialPort portToPC;
    
	public static void setConnectionToPC(boolean b) {
		connectionToPC = b;
	}

	public static boolean isConnectedToPC() {
		return connectionToPC;
	}

	public static SerialPort getPortToPC() {
		if(isConnectedToPC() && !portToPC.isOpen()) {
			if(!portToPC.openPort()) {
				System.err.println("ERROR: Lost connection to PC!");
				setConnectionToPC(false);
				return null;
			}
		}
		return portToPC;
	}

	public static void setPortToPC(SerialPort portToPC) {
		ProjectProviderInstance.portToPC = portToPC;
		if(!ProjectProviderInstance.portToPC.isOpen()) {
			if(!ProjectProviderInstance.portToPC.openPort()) {
				System.err.println("ERROR: Couldn't open the connection to the PC!");
				setConnectionToPC(false);
			}
		}
	}
	
	private static boolean connectionToArduino = false;
    private static SerialPort portToArduino;
    
	public static void setConnectionToArduino(boolean b) {
		connectionToArduino = b;
	}

	public static boolean isConnectedToArduino() {
		return connectionToArduino;
	}

	public static SerialPort getPortToArduino() {
		if(isConnectedToArduino() && !portToArduino.isOpen()) {
			if(!portToArduino.openPort()) {
				System.err.println("ERROR: Lost connection to Arduino!");
				setConnectionToPC(false);
				return null;
			}
		}
		return portToPC;
	}

	public static void setPortToArduino(SerialPort portToPC) {
		ProjectProviderInstance.portToArduino = portToPC;
		if(!ProjectProviderInstance.portToArduino.isOpen()) {
			if(!ProjectProviderInstance.portToArduino.openPort()) {
				System.err.println("ERROR: Couldn't open the connection to the Arduino!");
				setConnectionToPC(false);
			}
		}
	}
	
	
	private static CurrentTimeStamp timeStamp = new CurrentTimeStamp(0);
	
	public static int getTimeStamp() {
		return timeStamp.getNewestTimeStamp();
	}
	
	public static CurrentTimeStamp getObservableTimeStamp() {
		return timeStamp;
	}

	public static void setTimeStamp(int timeStamp) {
		if(timeStamp != 0) {
			ProjectProviderInstance.timeStamp.setCurrentTimeStamp(timeStamp);
			
		}
	}

	private static boolean isEverythingConnected = false;
	
	public static void setIsEverythingConnected(boolean b) {
		isEverythingConnected = b;
	}
	
	public static boolean isEverythingConnected() {
		// TODO Auto-generated method stub
		return isEverythingConnected;
	}
}
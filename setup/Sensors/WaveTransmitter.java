package setup.Sensors;
 
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import setup.ProjectProviderInstance;
import setup.Start.SetSerialPortsWithListeners;

/**
 * @author Tim_Wieland
 * @version 1.0.0
 *
 * This class:
 *  -   Provides methods to transmit waves by the GpioController.
 *  -	Each sensor is connected with a ground, a power supply and a RS232 cable.
 *  -	The fourth pin connected from the sensor is with a GPIO from the Raspberry Pi, which,
 *  	if pulled high, let the sensor transmit waves, and if pulled low, stop transmitting waves.
 *  
 *   
 */

public class WaveTransmitter {

    //Creates a not-declared variable - will be declared after check in the constructor
    private GpioPinDigitalOutput waveTransmitter;
    //Ability to transmit waves
    private boolean isAbleToTransmitWaves = false;

	private static long startingTime;
    
    private String sensorName;
    /**
     * 
     * This constructor shouldn't be used - if used, the method setWaveTransmitter(Pin pin)
     * must be used!
     */
    //Default constructor
    WaveTransmitter() {
    }
    
    /**
     * 
     * @param pin
     * 
     * Constructor of ProjectV0_1.WaveTransmitter1 with the needed argument pin
     * - checks and, if possible, makes the pin to a waveTransmitter which is pulled high.
     */
    
    public WaveTransmitter(Pin pin) {
        //Checks, if the pin is not already occupied
        ProjectProviderInstance.checkForEligibleAccess(pin);
        //Occupies the Pin pin as a waveTransmitter - a digital output pin
        //Declare the variable waveTransmitter
        waveTransmitter = ProjectProviderInstance.gpio.provisionDigitalOutputPin(pin, "Wave transmitter", PinState.LOW);
        
        if(pin == RaspiPin.GPIO_29) {
        	setSensorName("Sensor 1");
        } else if(pin == RaspiPin.GPIO_28) {
        	setSensorName("Sensor 2");
        } else if(pin == RaspiPin.GPIO_27) {
        	setSensorName("Sensor 3");
        } else if(pin == RaspiPin.GPIO_25) {
        	setSensorName("Sensor 4");
        } else if(pin == RaspiPin.GPIO_24) {
        	setSensorName("Sensor 5");
        } else if(pin == RaspiPin.GPIO_23) {
        	setSensorName("Sensor 6");
        } else if(pin == RaspiPin.GPIO_22) {
        	setSensorName("Sensor 7");
        } else if(pin == RaspiPin.GPIO_21) {
        	setSensorName("Sensor 8");
        } else if(pin == RaspiPin.GPIO_30) {
        	setSensorName("Sensor 9");
        } else if(pin == RaspiPin.GPIO_31) {
        	setSensorName("Sensor 10");
        }
   }

    
    /**
     * 
     * @param pin
     * 
     * This method initializes the waveTransmitter afterwards with a pin.
     * 
     */
    
    public void setWaveTransmitter(Pin pin) {
        //Checks, if the pin is not already occupied
        ProjectProviderInstance.checkForEligibleAccess(pin);
        //Occupies the Pin pin as a waveTransmitter - a digital output pin
        //Declare the variable waveTransmitter
        waveTransmitter = ProjectProviderInstance.gpio.provisionDigitalOutputPin(pin, "Wave transmitter", PinState.LOW);
    }

    
    /**
     * 
     * @param isAbleToTransmit true, if able to transmit waves
     * 
     * Set ability to transmit waves
     */
    
    private void setAbilityToTransmitWaves(boolean isAbleToTransmit) { 
    	isAbleToTransmitWaves = isAbleToTransmit; 
    }

    /**
     * 
     * @return which value the variable isAbleToTransmitWaves has
     * 
     */
    
    public boolean getAbilityToTransmitWaves() {
        return isAbleToTransmitWaves;
    }

    /**
     * 
     * Changes ability to transmit waves of the WaveTransmitter instance
     * 
     */
    
    public void changeAbilityToTransmitWaves() {
        if(isAbleToTransmitWaves) setAbilityToTransmitWaves(false);
        else setAbilityToTransmitWaves(true);
    }

    //
    
    /**
     * 
     * Method to transmit waves direct.
     * Pulls the initialized pin high.
     * 
     */
    
    public void transmitWaveDirect() {
        //Set pin to low
        waveTransmitter.high();
    }
    
    /**
     * 
     * Method to stop transmitting waves.
     * Pulls the initialized pin low.
     * 
     */
    
    public void stopTransmittingWaves() {
    	waveTransmitter.low();
    }

    /**
     * 
     * @param time how long the pin should be pulled high
     * 
     * Method to transmit waves for x milliseconds
     */
    public void transmitWavesFor(long time) {
        waveTransmitter.high();
        try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        waveTransmitter.low();
    }

	public void transmitWavesForPortSetter(long time) {
		waveTransmitter.high();
		setStartingTime(System.currentTimeMillis());
		while((System.currentTimeMillis() <= (getStartingTime() + time)) && SetSerialPortsWithListeners.getIsBlocking()) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		waveTransmitter.low();
	}

	public static long getStartingTime() {
		return startingTime;
	}

	public static void setStartingTime(long startingTime) {
		WaveTransmitter.startingTime = startingTime;
	}
	
	private void setSensorName(String name) {
		sensorName = name;
	}
	
	public String getSensorName() {
		return sensorName;
	}
}
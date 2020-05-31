package setup.Sensors;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.pi4j.io.gpio.RaspiPin;


public class SensorConnectionsPin4 {
	
	private ArrayList<WaveTransmitter> sensorsToUse = new ArrayList<>();
	
	final WaveTransmitter sensorOne;
	final WaveTransmitter sensorTwo;
	final WaveTransmitter sensorThree;

	final WaveTransmitter sensorFour;
	final WaveTransmitter sensorFive;
	final WaveTransmitter sensorSix;

	final WaveTransmitter sensorSeven;
	final WaveTransmitter sensorEight;

	final WaveTransmitter sensorNine;
	final WaveTransmitter sensorTen;
	
	public SensorConnectionsPin4() {
		
		sensorOne = new WaveTransmitter(RaspiPin.GPIO_29);
		sensorTwo = new WaveTransmitter(RaspiPin.GPIO_28);
		sensorThree = new WaveTransmitter(RaspiPin.GPIO_27);
		
		sensorFour = new WaveTransmitter(RaspiPin.GPIO_25);
		sensorFive = new WaveTransmitter(RaspiPin.GPIO_24);
		sensorSix = new WaveTransmitter(RaspiPin.GPIO_23);
		
		sensorSeven = new WaveTransmitter(RaspiPin.GPIO_22);
		sensorEight = new WaveTransmitter(RaspiPin.GPIO_21);

		sensorNine = new WaveTransmitter(RaspiPin.GPIO_30);
		sensorTen = new WaveTransmitter(RaspiPin.GPIO_31);
	}
	
	public WaveTransmitter getSensorByNumber(byte sensorNumber) throws IOException {
		if(sensorNumber < 1 || sensorNumber > 10) {
			System.err.println("Error at getSensorNumber for Sensor number " + sensorNumber);
			return sensorTen;
		}
		
		switch(sensorNumber) {
			case 1:
				return sensorOne;
			case 2:
				return sensorTwo;
			case 3: 
				return sensorThree;
			case 4:
				return sensorFour;
			case 5:
				return sensorFive;
			case 6:
				return sensorSix;
			case 7:
				return sensorSeven;
			case 8:
				return sensorEight;
			case 9:
				return sensorNine;
			case 10:
				return sensorTen;
			}
		throw new IOException();
	}
	
	public void stopAllSensorsWithTransmitting() {
		sensorOne.stopTransmittingWaves();
		sensorTwo.stopTransmittingWaves();
		sensorThree.stopTransmittingWaves();
		sensorFour.stopTransmittingWaves();
		sensorFive.stopTransmittingWaves();
		sensorSix.stopTransmittingWaves();
		sensorSeven.stopTransmittingWaves();
		sensorEight.stopTransmittingWaves();
		sensorNine.stopTransmittingWaves();
		sensorTen.stopTransmittingWaves();
	}

	public ArrayList<WaveTransmitter> getSensorsToUse() {
		return sensorsToUse;
	}
	
	private ArrayList<WaveTransmitter> stepOneTransmitter = new ArrayList<WaveTransmitter>();
	private ArrayList<WaveTransmitter> stepTwoTransmitter = new ArrayList<WaveTransmitter>();
	private Lock lock = new ReentrantLock();
	private WaveTransmitter currentSensor;
	
	public void addSensorsToUse(WaveTransmitter sensorToUse) {
		lock.lock();
		sensorsToUse.add(sensorToUse);
		currentSensor = sensorToUse;
		
//		System.out.println("ADD SENSOR TO USE: " + sensorToUse.getSensorName());
		
		if((sensorOne == currentSensor) 
				|| (sensorThree == currentSensor) 
				|| (sensorFive == currentSensor)) {
			stepOneTransmitter.add(sensorToUse);
			
		} else if((sensorTwo == currentSensor)
				|| (sensorFour == currentSensor)
				|| (sensorSix == currentSensor)) {
			
//			System.out.print(sensorToUse.getSensorName());
			stepTwoTransmitter.add(sensorToUse);
		}
		
		lock.unlock();
	}
	
	public ArrayList<WaveTransmitter> getSensorsToTransmitInStepOne() {
		return stepOneTransmitter;		
	}
	
	public ArrayList<WaveTransmitter> getSensorsToTransmitInStepTwo() {
		return stepTwoTransmitter;		
	}
}

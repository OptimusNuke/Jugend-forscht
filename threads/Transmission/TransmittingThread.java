package threads.Transmission;
 
import java.util.ArrayList;
import java.util.List;

import setup.ProjectProviderInstance;
import setup.Sensors.WaveTransmitter;

/**
 * 
 * @author Tim_Wieland
 * @version 1.0.0
 *
 *	This thread is pulling pin 4 low for a specific given time and then again high of the upper-body-sensors.
 *	Having a timeStamp, the duration should be low enough to let every sensor transmit for a specific time and not have a
 *		significant difference of data in a timeStamp.
 *
 *	We try to let pin 1 , pin 3 and pin 5 transmit a the same time as well as
 *				  pin 2, pin 4 and pin 6.
 *	
 *	Due to this, we can get up to 3 distances from 3 sensors during one transmission-run. 
 */


public class TransmittingThread extends Thread {
	
	private static List<String> sensorTransmittingList = new ArrayList<String>();
	private long MILLIS_TO_TRANSMIT = 350;
	private long WAITING_TIME = 25;
	
	
	public TransmittingThread() {
	}

	@Override
	public void run() {
		
		//Adding all sensors which should transmit waves after each timeStamp to the sensorTransmittingList
		for(WaveTransmitter transmitter : ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne()) {
			if(!sensorTransmittingList.contains(transmitter.getSensorName())) {
				sensorTransmittingList.add(transmitter.getSensorName());
			}
		}
		
		for(WaveTransmitter transmitter : ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo()) {
			if(!sensorTransmittingList.contains(transmitter.getSensorName())) {
				sensorTransmittingList.add(transmitter.getSensorName());
			}
		}
		
		for(int timeStamp = 1; true; timeStamp++) {
			
			ProjectProviderInstance.setTimeStamp(timeStamp);
			
			try {
				
				if(ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne() != null && ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo() != null) {
					//Make all sensors send data
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne().forEach((wt) -> wt.transmitWaveDirect());
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo().forEach((wt) -> wt.transmitWaveDirect());
					
					//Reset all MILLIS_TO_TRANSMIT to prevent having an initialized average filter at the sensor with a rate limit 
					Thread.sleep(MILLIS_TO_TRANSMIT);
					
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne().forEach((wt) -> wt.stopTransmittingWaves());
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo().forEach((wt) -> wt.stopTransmittingWaves());
					
					Thread.sleep(WAITING_TIME);
				}
				
				/*
				//Make all sensors of step one to transmit waves for MILLIS_TO_TRANSMIT and then stop them
				if(ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne() != null) {
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne().forEach((wt) -> wt.transmitWaveDirect());
//					for(WaveTransmitter transmitterStepOne : ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne()) {
//						transmitterStepOne.transmitWaveDirect();
//					}
					
					Thread.sleep(MILLIS_TO_TRANSMIT);
					
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne().forEach((wt) -> wt.stopTransmittingWaves());
//					for(WaveTransmitter transmitterStepOne : ProjectProviderInstance.sensors.getSensorsToTransmitInStepOne()) {
//						transmitterStepOne.stopTransmittingWaves();
//					}
					
					//Thread.sleep(WAITING_TIME);
				}
					
				//Make all sensors of step two to transmit waves for MILLIS_TO_TRANSMIT and then stop them
				if(ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo() != null) {
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo().forEach((wt) -> wt.transmitWaveDirect());
//					for(WaveTransmitter transmitterStepTwo : ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo()) {
//						transmitterStepTwo.transmitWaveDirect();
//					}
					
					Thread.sleep(MILLIS_TO_TRANSMIT);
					
					ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo().forEach((wt) -> wt.stopTransmittingWaves());
//					for(WaveTransmitter transmitterStepTwo : ProjectProviderInstance.sensors.getSensorsToTransmitInStepTwo()) {
//						transmitterStepTwo.stopTransmittingWaves();
//					}
					
					//Thread.sleep(WAITING_TIME);
				}
				*/
				
			} catch (InterruptedException e) {
				e.printStackTrace();	
			}
			System.out.println("For timeStamp " + timeStamp + ":\tThe Sensors " + sensorTransmittingList + " should have transmitted!");
		}
	}
}
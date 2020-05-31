package threads.Communication;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;

import Caches.SensorDataLayerCachesPC;
import Helper.CurrentTimeStamp;
import setup.Expressions.ArcExpression;

@Deprecated
public class PCToPiThread extends Thread {

//	DO NOT USE THE CONSTANTES FROM ProjectProviderInstance4! This will initialize the static variables which need pi4j what
//	cannot run on the PC and will therefore not execute the programm.	
	public static final byte STARTBYTE_PC = (byte) '*';
	public static final byte ENDBYTE_PC = (byte) '~';
	
	private static SerialPort serialPort;
	private static String serialPortName;
	private static String arcKeyString = "";


	private static boolean setUpForPI = true;
	
	public static boolean hasConnectionToPC = false;
	
	private static ArcExpression arcExpression;
	
	public static CurrentTimeStamp currentTimeStamp;
	
	public PCToPiThread() throws IOException {
		
		
		SerialPort[] sp = SerialPort.getCommPorts();
//		for(SerialPort port : sp) {
//			System.out.println(port.getSystemPortName());
//		}
		if(sp.length == 1) {
			setSerialPort(sp[0]);
			setSerialPortName(sp[0].getSystemPortName());
			System.out.println("Use SerialPort: " + getSerialPortName());
		} else {
			setSerialPortName("COM28");
		}
		
		setSerialPort(SerialPort.getCommPort(getSerialPortName()));
		
		if(!getSerialPort().openPort()) {
			System.err.println("ERROR: Problems with opening PCToPiConnection " + getSerialPortName());
		}
		
		currentTimeStamp = new CurrentTimeStamp(1);
		
		/*startingByte = -1;
		byteArrayList = new ArrayList<Byte>();
		readByte = -1;
		currentChar = ' ';
		sensorNumberString = null;
		sensorNumber = 0;
		
		serialPort.addDataListener(new SerialPortDataListener() {
		   @Override
		   public int getListeningEvents() { 
			   return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; 
		   }
		   
		   @Override
		   public void serialEvent(SerialPortEvent event) {
			   if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
				   return;
			   } 
			   while(serialPort.bytesAvailable() > 0) {
				   try {
					   if(setUpForPI) {
						   while(getSerialPort().getInputStream().read() != STARTBYTE_PC) {
								Thread.sleep(20);
							}
							setUpForPI = false;
					   }
					   
					   startingByte = (byte) serialPort.getInputStream().read();
					   
					   if(startingByte != -1 && startingByte == STARTBYTE_PC) {	//Starting byte
//							serialPort.getOutputStream().write((byte) '@');
							readByte = (byte) serialPort.getInputStream().read();
							if(readByte != ENDBYTE_PC) {
								do {
									Thread.sleep(3);
									if(readByte != -1 && readByte >= 30 && readByte <= 200 && 
											readByte != STARTBYTE_PC && readByte != ENDBYTE_PC) {
										System.out.print((char) readByte);
										byteArrayList.add(readByte);
									}
									readByte = (byte) serialPort.getInputStream().read();
								} while(readByte != ENDBYTE_PC);
							}
							
							it = byteArrayList.iterator();
							
							while(it.hasNext()) {		//Maybe change this part. Could work without because an ArcExpression can be generated just by the keyString.
								sensorNumberString += currentChar;
							}
							
							sensorNumberString = sensorNumberString.substring(sensorNumberString.indexOf("S=") + 2, sensorNumberString.indexOf(";ts="));
							System.out.println(sensorNumberString);
							if(sensorNumberString != "" && sensorNumberString != "0") {
								try {
									System.out.println(sensorNumber);
									sensorNumber = Byte.parseByte(sensorNumberString);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							
								it = byteArrayList.iterator();
							
								while(it.hasNext()) {
									currentChar = (char) it.next().byteValue();
									System.out.print(currentChar);
									arcKeyString += currentChar;
								}						
								System.out.println(arcKeyString);
								arcExpression = ArcExpression.arcExpressionByString(arcKeyString);
							
								System.out.println("Arc: " + arcExpression.getKeyString());
							
								if(arcExpression != null) { 
									if(sensorNumber > 0) {
										if(sensorNumber == 1 || sensorNumber == 2 || sensorNumber == 3) {
											SensorDataLayerCachesPC.firstLayer.add(arcExpression);
//											System.out.println("Added data to the first layer!\t" + arcExpression.getKeyString());
										} else if(sensorNumber == 4 || sensorNumber == 5 || sensorNumber == 6) {
											SensorDataLayerCachesPC.secondLayer.add(arcExpression);
										} else if(sensorNumber == 7 || sensorNumber == 8) {
											SensorDataLayerCachesPC.thirdLayer.add(arcExpression);
										} else if(sensorNumber == 9 || sensorNumber == 10){
											SensorDataLayerCachesPC.fourthLayer.add(arcExpression);
										}
									}
								}
							}
							
							it = null;
							sensorNumber = 0;
							sensorNumberString = "";
							arcExpression = null;
							arcKeyString = "";
							byteArrayList.clear();
							currentChar = 0;
//						serialPort.closePort();
						}
				   } catch (InterruptedException e) {
					   e.printStackTrace();
				   } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }
		   }
		   }
		});*/
	}
	
	@Override
	public void run() {
		try {
			byte startingByte = -1;
			List<Byte> byteArrayList = new ArrayList<Byte>();
			byte readByte;
			char currentChar;
			String sensorNumberString = null;
			Iterator<Byte> it;
			byte sensorNumber = 0;
			
			while(true) {
//				Thread.sleep(100);
				
				if(setUpForPI) {	//One time loop, works together with the other PC-PI-ConnectionThread
					while(getSerialPort().getInputStream().read() != STARTBYTE_PC) {
//						Char to write to PI - the PI will check for this char
//						System.out.println(getSerialPort().isOpen());
						getSerialPort().getOutputStream().write(STARTBYTE_PC);
						System.out.println("Write " + STARTBYTE_PC);
						Thread.sleep(200);
					}
					setUpForPI = false;
				}
			
				
				if(serialPort.bytesAvailable() > 0) {
					startingByte = (byte) serialPort.getInputStream().read();
					if(startingByte != -1 && startingByte == STARTBYTE_PC) {	//Starting byte
//						serialPort.getOutputStream().write((byte) '@');
						readByte = (byte) serialPort.getInputStream().read();
						
						if(readByte != ENDBYTE_PC) {
							do {
								Thread.sleep(3);
								if(readByte != -1 && readByte >= 30 && readByte <= 200 && 
										readByte != STARTBYTE_PC && readByte != ENDBYTE_PC) {
									byteArrayList.add(readByte);
								}
								readByte = (byte) serialPort.getInputStream().read();
							} while(readByte != ENDBYTE_PC);
						}
						it = byteArrayList.iterator();
						
						while(it.hasNext()) {
							currentChar = (char) it.next().byteValue();
//							System.out.print(currentChar);
							arcKeyString += currentChar;
						}
						
						
						if(arcKeyString != "" && arcKeyString != null
								&& arcKeyString.contains("S=") 
								&& arcKeyString.contains(";ts=") 
								&& arcKeyString.contains(";dt=")) {
							
							sensorNumberString = arcKeyString.substring(arcKeyString.indexOf("S=") + 2, arcKeyString.indexOf(";ts="));
							
							try {
								sensorNumber = Byte.parseByte(sensorNumberString);
//								System.out.println(sensorNumber);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}						
							
							arcExpression = ArcExpression.arcExpressionByString(arcKeyString);
							
							if(arcExpression != null) { 
//								System.out.println("Arc: " + arcExpression.getKeyString());
								if(sensorNumber > 0) {
									if(sensorNumber == 1 || sensorNumber == 2 || sensorNumber == 3) {
										SensorDataLayerCachesPC.firstLayer.add(arcExpression);
//										System.out.println("Added data to the first layer!\t" + arcExpression.getKeyString());
									} else if(sensorNumber == 4 || sensorNumber == 5 || sensorNumber == 6) {
										SensorDataLayerCachesPC.secondLayer.add(arcExpression);
									} else if(sensorNumber == 7 || sensorNumber == 8) {
										SensorDataLayerCachesPC.thirdLayer.add(arcExpression);
									} else if(sensorNumber == 9 || sensorNumber == 10){
										SensorDataLayerCachesPC.fourthLayer.add(arcExpression);
									}
								}
								currentTimeStamp.setCurrentTimeStamp(arcExpression.getTimeStamp());
							} //else {
//								Thread.sleep(250);
//							}
						}
						
						it = null;
						sensorNumber = 0;
						sensorNumberString = "";
						arcExpression = null;
						arcKeyString = "";
						byteArrayList.clear();
						currentChar = 0;
//					serialPort.closePort();
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static SerialPort getSerialPort() {
		return serialPort;
	}

	public static void setSerialPort(SerialPort serialPort) {
		PCToPiThread.serialPort = serialPort;
	}

	public static String getSerialPortName() {
		return serialPortName;
	}

	public static void setSerialPortName(String serialPortName) {
		PCToPiThread.serialPortName = serialPortName;
	}
}


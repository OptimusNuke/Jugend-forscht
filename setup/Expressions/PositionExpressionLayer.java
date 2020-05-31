package setup.Expressions;
 
import Caches.PositionExpressionLayerCache;
import socket.ArduinoSocketStarter;
import socket.PCSocketStarter;
import threads.Communication.VibrationMotorToArduino;

/**
 * 
 * @author Tim_Wieland
 * @version 1.0.0
 * 
 * 
 * 	A PositionExpressionLayer describes a amount of PositionExpression's together.
 * For Example: If we got PositionExpression's / ArcExpression's by Sensor 1, 2, 3, 4, 5 and 6,
 * a PositionExpressionLayer will look like the following:
 * 
 * PositionExpressionLayer - full:
 * 
 * 		(PE1,	PE2,	PE3,	PE4,	PE5)
 * 		(PE6,	PE7,	PE8,	PE9,	PE10)
 * 	{	(PE11,	PE12,	PE13,	PE14,	PE15)	}
 * 		(PE16,	PE17,	PE18,	PE19,	PE20)
 * 		(PE21,	PE22,	PE23,	PE24,	PE25)
 * 
 * PositionExpressionLayer	-	only 5 Sensors:
 * 
 * {	(PE1,	PE2,	PE3,	PE4,	PE5)	}
 * 
 * The expressions which are not given - like 2 and 4 - will be calculated.
 * 
 */

public class PositionExpressionLayer {	
	
	private static PositionExpression expression1 = null;
	private static PositionExpression expression2 = null;
	private static PositionExpression expression3 = null;
	private static PositionExpression expression4 = null;
	private static PositionExpression expression5 = null;
	
	private static PositionExpression expression6 = null;
	private static PositionExpression expression7 = null;
	private static PositionExpression expression8 = null;
	private static PositionExpression expression9 = null;
	private static PositionExpression expression10 = null;
	
	private static PositionExpression expression11 = null;
	private static PositionExpression expression12 = null;
	private static PositionExpression expression13 = null;
	private static PositionExpression expression14 = null;
	private static PositionExpression expression15 = null;
	
	private static PositionExpression expression16 = null;
	private static PositionExpression expression17 = null;
	private static PositionExpression expression18 = null;
	private static PositionExpression expression19 = null;
	private static PositionExpression expression20 = null;
	
	private static PositionExpression expression21 = null;
	private static PositionExpression expression22 = null;
	private static PositionExpression expression23 = null;
	private static PositionExpression expression24 = null;
	private static PositionExpression expression25 = null;
	
	private static VibrationMotorToArduino arduino = new VibrationMotorToArduino();
	
	private int timeStamp;
	
	
	//DO NOT USE THIS METHOD: ONLY USE ALL 2 LAYERS!
	@Deprecated
	public PositionExpressionLayer(PositionExpression pe1, PositionExpression pe3, PositionExpression pe5) {
		if((pe1.getTimeStamp() == pe3.getTimeStamp()) && (pe3.getTimeStamp() == pe5.getTimeStamp())) {
			setTimeStamp(pe1.getTimeStamp());
		} else {
			throw new IllegalArgumentException("Can't generate a PositionExpressionLayer for PositionExpressions without the same timeStamp!");
		}
		
		expression1 = pe1;
		expression3 = pe3;
		expression5 = pe5;
		
		expression2 = PositionExpressionCalculator.interpolatePositions(2, pe1, pe3, getTimeStamp());
		expression4 = PositionExpressionCalculator.interpolatePositions(4, pe3, pe5, getTimeStamp());
		
		processData();
	}
	
	public PositionExpressionLayer(PositionExpression pe1, 	PositionExpression pe3, 	PositionExpression pe5,
								   PositionExpression pe21, PositionExpression pe23, 	PositionExpression pe25) {
		if((pe1.getTimeStamp() == pe3.getTimeStamp()) && (pe3.getTimeStamp() == pe5.getTimeStamp()) &&
			(pe5.getTimeStamp() == pe21.getTimeStamp()) && (pe21.getTimeStamp() == pe23.getTimeStamp()) &&
			(pe23.getTimeStamp() == pe25.getTimeStamp())) {
			setTimeStamp(pe1.getTimeStamp());
		} else {
			throw new IllegalArgumentException("Can't generate a PositionExpressionLayer for PositionExpressions without the same timeStamp!");
		}
		
		expression1 = pe1;
		expression3 = pe3;
		expression5 = pe5;
		expression21 = pe21;
		expression23 = pe23;
		expression25 = pe25;
		/*
		 expression13 = PositionExpressionCalculator.calculateExpression13(pe3, pe23, getTimeStamp());
		
		expression2 = PositionExpressionCalculator.calculateExpression2(pe1, pe3, getTimeStamp());
		expression4 = PositionExpressionCalculator.calculateExpression4(pe3, pe5, getTimeStamp());
		expression22 = PositionExpressionCalculator.calculateExpression22(pe21, pe23, getTimeStamp());
		expression24 = PositionExpressionCalculator.calculateExpression24(pe23, pe25, getTimeStamp());
		
		expression11 = PositionExpressionCalculator.calculateExpression11(pe1, pe21, getTimeStamp());
		expression15 = PositionExpressionCalculator.calculateExpression15(pe5, pe25, getTimeStamp());
		
		expression6 = PositionExpressionCalculator.calculateExpression6(pe1, expression11, getTimeStamp());
		expression16 = PositionExpressionCalculator.calculateExpression16(expression11, pe21, getTimeStamp());
		expression10 = PositionExpressionCalculator.calculateExpression10(pe5, expression15, getTimeStamp());
		expression20 = PositionExpressionCalculator.calculateExpression20(expression15, pe25, getTimeStamp());
		
		expression8 = PositionExpressionCalculator.calculateExpression8(pe3, expression13, getTimeStamp());
		expression18 = PositionExpressionCalculator.calculateExpression18(expression13, pe23, getTimeStamp());
		expression12 = PositionExpressionCalculator.calculateExpression12(expression11, expression13, getTimeStamp());
		expression14 = PositionExpressionCalculator.calculateExpression14(expression13, expression15, getTimeStamp());
		
		expression7 = new PositionExpression(7, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression7By2and12(expression2, expression12, getTimeStamp()).getMeasuredDistance() + 
											  	PositionExpressionCalculator.calculateExpression7By6and8(expression6, expression8, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression9 = new PositionExpression(9, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression9By4and14(expression4, expression14, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.calculateExpression9By8and10(expression8, expression10, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression17 = new PositionExpression(17, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression17By12and22(expression12, expression22, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.calculateExpression17By16and18(expression16, expression18, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression19 = new PositionExpression(19, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression19By14and24(expression14, expression24, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.calculateExpression19By18and20(expression18, expression20, getTimeStamp()).getMeasuredDistance()) / 2, true);
		
		 */
		
		expression13 = PositionExpressionCalculator.calculateAverageExpression(13, pe3, pe23, getTimeStamp());
		
		expression2 = PositionExpressionCalculator.interpolatePositions(2, pe1, pe3, getTimeStamp());
		expression4 = PositionExpressionCalculator.interpolatePositions(4, pe3, pe5, getTimeStamp());
		expression22 = PositionExpressionCalculator.interpolatePositions(22, pe21, pe23, getTimeStamp());
		expression24 = PositionExpressionCalculator.interpolatePositions(24, pe23, pe25, getTimeStamp());
		
		expression11 = PositionExpressionCalculator.calculateAverageExpression(11, pe1, pe21, getTimeStamp());
		expression15 = PositionExpressionCalculator.calculateAverageExpression(15, pe5, pe25, getTimeStamp());
		
		expression6 = PositionExpressionCalculator.calculateAverageExpression(6, pe1, expression11, getTimeStamp());
		expression16 = PositionExpressionCalculator.calculateAverageExpression(16, expression11, pe21, getTimeStamp());
		expression10 = PositionExpressionCalculator.calculateAverageExpression(10, pe5, expression15, getTimeStamp());
		expression20 = PositionExpressionCalculator.calculateAverageExpression(20, expression15, pe25, getTimeStamp());
		
		expression8 = PositionExpressionCalculator.calculateAverageExpression(8, pe3, expression13, getTimeStamp());
		expression18 = PositionExpressionCalculator.calculateAverageExpression(18, expression13, pe23, getTimeStamp());
		expression12 = PositionExpressionCalculator.interpolatePositions(12, expression11, expression13, getTimeStamp());
		expression14 = PositionExpressionCalculator.interpolatePositions(14, expression13, expression15, getTimeStamp());
		
		expression7 = new PositionExpression(7, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(7, expression2, expression12, getTimeStamp()).getMeasuredDistance() + 
											  	PositionExpressionCalculator.interpolatePositions(7, expression6, expression8, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression9 = new PositionExpression(9, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(9, expression4, expression14, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.interpolatePositions(9, expression8, expression10, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression17 = new PositionExpression(17, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(17, expression12, expression22, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.interpolatePositions(17, expression16, expression18, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression19 = new PositionExpression(19, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(19, expression14, expression24, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.interpolatePositions(19, expression18, expression20, getTimeStamp()).getMeasuredDistance()) / 2, true);
		
		processData();
	}
	
	//DO NOT USE THIS METHOD: POSITIONEXPRESSION13 WOULD BE AN UNIMPLEMENTED SENSOR!
	@Deprecated
	public PositionExpressionLayer(PositionExpression pe1, 	PositionExpression pe3, 	PositionExpression pe5,
								   						   	PositionExpression pe13,
								   PositionExpression pe21, PositionExpression pe23, 	PositionExpression pe25) {
		
		if((pe1.getTimeStamp() == pe3.getTimeStamp()) && (pe3.getTimeStamp() == pe5.getTimeStamp()) &&
			(pe5.getTimeStamp() == pe13.getTimeStamp()) && (pe13.getTimeStamp() == pe21.getTimeStamp()) &&
			(pe21.getTimeStamp() == pe23.getTimeStamp()) && (pe23.getTimeStamp() == pe25.getTimeStamp())) {
			setTimeStamp(pe1.getTimeStamp());
		} else {
			throw new IllegalArgumentException("Can't generate a PositionExpressionLayer for PositionExpressions without the same timeStamp!");
		}
		
		expression1 = pe1;
		expression3 = pe3;
		expression5 = pe5;
		expression13 = pe13;
		expression21 = pe21;
		expression23 = pe23;
		expression25 = pe25;
		
		expression2 = PositionExpressionCalculator.interpolatePositions(2, pe1, pe3, getTimeStamp());
		expression4 = PositionExpressionCalculator.interpolatePositions(4, pe3, pe5, getTimeStamp());
		expression22 = PositionExpressionCalculator.interpolatePositions(22, pe21, pe23, getTimeStamp());
		expression24 = PositionExpressionCalculator.interpolatePositions(24, pe23, pe25, getTimeStamp());
		
		expression11 = PositionExpressionCalculator.calculateAverageExpression(11, pe1, pe21, getTimeStamp());
		expression15 = PositionExpressionCalculator.calculateAverageExpression(15, pe5, pe25, getTimeStamp());
		
		expression6 = PositionExpressionCalculator.calculateAverageExpression(6, pe1, expression11, getTimeStamp());
		expression16 = PositionExpressionCalculator.calculateAverageExpression(16, expression11, pe21, getTimeStamp());
		expression10 = PositionExpressionCalculator.calculateAverageExpression(10, pe5, expression15, getTimeStamp());
		expression20 = PositionExpressionCalculator.calculateAverageExpression(20, expression15, pe25, getTimeStamp());
		
		expression8 = PositionExpressionCalculator.calculateAverageExpression(8, pe3, expression13, getTimeStamp());
		expression18 = PositionExpressionCalculator.calculateAverageExpression(18, expression13, pe23, getTimeStamp());
		expression12 = PositionExpressionCalculator.interpolatePositions(12, expression11, expression13, getTimeStamp());
		expression14 = PositionExpressionCalculator.interpolatePositions(14, expression13, expression15, getTimeStamp());
		
		expression7 = new PositionExpression(7, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(7, expression2, expression12, getTimeStamp()).getMeasuredDistance() + 
											  	PositionExpressionCalculator.interpolatePositions(7, expression6, expression8, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression9 = new PositionExpression(9, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(9, expression4, expression14, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.interpolatePositions(9, expression8, expression10, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression17 = new PositionExpression(17, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(17, expression12, expression22, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.interpolatePositions(17, expression16, expression18, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression19 = new PositionExpression(19, getTimeStamp(),
												(PositionExpressionCalculator.calculateAverageExpression(19, expression14, expression24, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.interpolatePositions(19, expression18, expression20, getTimeStamp()).getMeasuredDistance()) / 2, true);
		
		/*
		expression2 = PositionExpressionCalculator.calculateExpression2(pe1, pe3, getTimeStamp());
		expression4 = PositionExpressionCalculator.calculateExpression4(pe3, pe5, getTimeStamp());
		expression22 = PositionExpressionCalculator.calculateExpression22(pe21, pe23, getTimeStamp());
		expression24 = PositionExpressionCalculator.calculateExpression24(pe23, pe25, getTimeStamp());
		
		expression11 = PositionExpressionCalculator.calculateExpression11(pe1, pe21, getTimeStamp());
		expression15 = PositionExpressionCalculator.calculateExpression15(pe5, pe25, getTimeStamp());
		
		expression6 = PositionExpressionCalculator.calculateExpression6(pe1, expression11, getTimeStamp());
		expression16 = PositionExpressionCalculator.calculateExpression16(expression11, pe21, getTimeStamp());
		expression10 = PositionExpressionCalculator.calculateExpression10(pe5, expression15, getTimeStamp());
		expression20 = PositionExpressionCalculator.calculateExpression20(expression15, pe25, getTimeStamp());
		
		expression8 = PositionExpressionCalculator.calculateExpression8(pe3, pe13, getTimeStamp());
		expression18 = PositionExpressionCalculator.calculateExpression18(pe13, pe23, getTimeStamp());
		expression12 = PositionExpressionCalculator.calculateExpression12(expression11, pe13, getTimeStamp());
		expression14 = PositionExpressionCalculator.calculateExpression14(pe13, expression15, getTimeStamp());
		
		expression7 = new PositionExpression(7, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression7By2and12(expression2, expression12, getTimeStamp()).getMeasuredDistance() + 
											  	PositionExpressionCalculator.calculateExpression7By6and8(expression6, expression8, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression9 = new PositionExpression(9, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression9By4and14(expression4, expression14, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.calculateExpression9By8and10(expression8, expression10, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression17 = new PositionExpression(17, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression17By12and22(expression12, expression22, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.calculateExpression17By16and18(expression16, expression18, getTimeStamp()).getMeasuredDistance()) / 2, true);
		expression19 = new PositionExpression(19, getTimeStamp(),
												(PositionExpressionCalculator.calculateExpression19By14and24(expression14, expression24, getTimeStamp()).getMeasuredDistance() + 
				  								PositionExpressionCalculator.calculateExpression19By18and20(expression18, expression20, getTimeStamp()).getMeasuredDistance()) / 2, true);
		*/
		processData();
	}
	
	public PositionExpression getPositionExpressionByNumber(int positionExpressionNumber) {
		if(positionExpressionNumber < 1 && positionExpressionNumber > 25) {
			throw new IllegalArgumentException("There's no PositionExpression for PositionExpressionNumber " + positionExpressionNumber);
		} else {
			switch(positionExpressionNumber) {
				case 1:	return expression1;
				case 2: return expression2;
				case 3: return expression3;
				case 4: return expression4;
				case 5: return expression5;
				case 6: return expression6;
				case 7: return expression7;
				case 8: return expression8;
				case 9: return expression9;
				case 10: return expression10;
				case 11: return expression11;
				case 12: return expression12;
				case 13: return expression13;
				case 14: return expression14;
				case 15: return expression15;
				case 16: return expression16;
				case 17: return expression17;
				case 18: return expression18;
				case 19: return expression19;
				case 20: return expression20;
				case 21: return expression21;
				case 22: return expression22;
				case 23: return expression23;
				case 24: return expression24;
				case 25: return expression25;
				default: return null;
			}
		}
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	private void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@SuppressWarnings("unused")
	private static void printOutAllExpressions() {
		System.out.println("Expression 1: " + expression1.getKeyString() + "\n" + 
				"Expression 2: " + expression2.getKeyString() + "\n" + 
				"Expression 3: " + expression3.getKeyString() + "\n" + 
				"Expression 4: " + expression4.getKeyString() + "\n" + 
				"Expression 5: " + expression5.getKeyString() + "\n" + 
				"Expression 6: " + expression6.getKeyString() + "\n" + 
				"Expression 7: " + expression7.getKeyString() + "\n" + 
				"Expression 8: " + expression8.getKeyString() + "\n" + 
				"Expression 9: " + expression9.getKeyString() + "\n" + 
				"Expression 10: " + expression10.getKeyString() + "\n" + 
				"Expression 11: " + expression11.getKeyString() + "\n" + 
				"Expression 12: " + expression12.getKeyString() + "\n" + 
				"Expression 13: " + expression13.getKeyString() + "\n" + 
				"Expression 14: " + expression14.getKeyString() + "\n" + 
				"Expression 15: " + expression15.getKeyString() + "\n" + 
				"Expression 16: " + expression16.getKeyString() + "\n" + 
				"Expression 17: " + expression17.getKeyString() + "\n" + 
				"Expression 18: " + expression18.getKeyString() + "\n" + 
				"Expression 19: " + expression19.getKeyString() + "\n" + 
				"Expression 20: " + expression20.getKeyString() + "\n" + 
				"Expression 21: " + expression21.getKeyString() + "\n" + 
				"Expression 22: " + expression22.getKeyString() + "\n" + 
				"Expression 23: " + expression23.getKeyString() + "\n" + 
				"Expression 24: " + expression24.getKeyString() + "\n" + 
				"Expression 25: " + expression25.getKeyString());
	}
	
	@SuppressWarnings("unused")
	private static void addAllExpressiosToExpressionLayerCache() {
		PositionExpressionLayerCache.addPositionExpressionToJson(expression1);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression2);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression3);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression4);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression5);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression6);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression7);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression8);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression9);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression10);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression11);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression12);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression13);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression14);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression15);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression16);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression17);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression18);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression19);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression20);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression21);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression22);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression23);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression24);
		PositionExpressionLayerCache.addPositionExpressionToJson(expression25);
	}
	
	private static void processData() {
//		printOutAllExpressions();
//		addAllExpressiosToExpressionLayerCache();
		sendLayerDataToArduino();
		sendLayerDataToPC();
	}
	
	private static void sendLayerDataToArduino() {
		if(!ArduinoSocketStarter.hasConnectionToArduino) {
			return;
		}
		arduino.writeDataToArduino(expression25, expression20, expression15, expression10, expression5,
				expression24, expression19, expression14, expression9, expression4,
				expression23, expression18, expression13, expression8, expression3,
				expression22, expression17, expression12, expression7, expression2,
				expression21, expression16, expression11, expression6, expression1);
	}
	
	private static void sendLayerDataToPC() {
		if(!PCSocketStarter.hasConnectionToPC) {
			return;
		}
		
		PCSocketStarter.pyConnection.writeLayer(expression1, expression2, expression3, expression4, expression5,
				expression6, expression7, expression8, expression9, expression10,
				expression11, expression12, expression13, expression14, expression15,
				expression16, expression17, expression18, expression19, expression20,
				expression21, expression22, expression23, expression24, expression25);
	}
}
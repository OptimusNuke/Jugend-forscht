package setup.Expressions;

public class PositionExpressionCalculator {
	
	//	FORMULA FROM US CAN BE FOUND HERE (GERMAN)
	//	https://docs.google.com/document/d/1_zOBgnmsX0CTXX-fHYeSgxPfDoKpDBI422jJV1j1_90/edit#
	
	private final static double CALC_USS_DIFFERENCE = 7.5; //6; //Distance between the two sensors in our made device
	
	private final static double CALC_ALPHA = Math.toRadians(15d);	//Angle between two sensors in our made device
	
	private final static double CALC_C = CALC_USS_DIFFERENCE / Math.tan(CALC_ALPHA);
	
	private final static double CALC_E = Math.tan(CALC_ALPHA * 0.5) * CALC_C;

	private final static double CALC_G = Math.sqrt(CALC_C * CALC_C + CALC_E * CALC_E);
	
//	private final static double CALC_F = CALC_USS_DIFFERENCE - CALC_E;
	
	private final static double CALC_D = Math.sqrt(CALC_C * CALC_C + CALC_USS_DIFFERENCE * CALC_USS_DIFFERENCE);
	
	private static int averageDistance = 0;
	
	public static PositionExpression interpolatePositions(int newPosition, PositionExpression pe1, PositionExpression pe2, int timeStamp) {
		if(pe1 != null && pe2 != null) {
			averageDistance = INTERPOLATE_POINTS(pe1.getMeasuredDistance(), pe2.getMeasuredDistance());
			return new PositionExpression(newPosition, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateAverageExpression(int newPosition, PositionExpression pe1, PositionExpression pe2, int timeStamp) {
		if(pe1 != null && pe2 != null) {
			averageDistance = (pe1.getMeasuredDistance() + pe2.getMeasuredDistance()) / 2;
			return new PositionExpression(newPosition, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	
	/*
	public static PositionExpression calculateExpression2(PositionExpression pe1, PositionExpression pe3, int timeStamp) {
		if(pe1 != null && pe3 != null) {
			averageDistance = INTERPOLATE_POINTS(pe1.getMeasuredDistance(), pe3.getMeasuredDistance());
			return new PositionExpression(2, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression4(PositionExpression pe3, PositionExpression pe5, int timeStamp) {
		if(pe3 != null && pe5 != null) {
			averageDistance = INTERPOLATE_POINTS(pe5.getMeasuredDistance(), pe3.getMeasuredDistance());
			return new PositionExpression(4, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression6(PositionExpression pe1, PositionExpression pe11, int timeStamp) {
		if(pe1 != null && pe11 != null) {
			averageDistance = (pe1.getMeasuredDistance() + pe11.getMeasuredDistance()) / 2;
			return new PositionExpression(6, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression7By6and8(PositionExpression pe6, PositionExpression pe8, int timeStamp) {
		if(pe6 != null && pe8 != null) {
			averageDistance = INTERPOLATE_POINTS(pe6.getMeasuredDistance(), pe8.getMeasuredDistance());
			return new PositionExpression(7, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression7By2and12(PositionExpression pe2, PositionExpression pe12, int timeStamp) {
		if(pe2 != null && pe12 != null) {
			averageDistance = (pe2.getMeasuredDistance() + pe12.getMeasuredDistance()) / 2;
			return new PositionExpression(7, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression8(PositionExpression pe3, PositionExpression pe13, int timeStamp) {
		if(pe3 != null && pe13 != null) {
			averageDistance = (pe3.getMeasuredDistance() + pe13.getMeasuredDistance()) / 2;
			return new PositionExpression(8, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression9By8and10(PositionExpression pe8, PositionExpression pe10, int timeStamp) {
		if(pe8 != null && pe10 != null) {
			averageDistance = INTERPOLATE_POINTS(pe10.getMeasuredDistance(), pe8.getMeasuredDistance());
			return new PositionExpression(9, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression9By4and14(PositionExpression pe4, PositionExpression pe14, int timeStamp) {
		if(pe4 != null && pe14 != null) {
			averageDistance = (pe4.getMeasuredDistance() + pe14.getMeasuredDistance()) / 2;
			return new PositionExpression(9, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression10(PositionExpression pe5, PositionExpression pe15, int timeStamp) {
		if(pe5 != null && pe15 != null) {
			averageDistance = (pe5.getMeasuredDistance() + pe15.getMeasuredDistance()) / 2;
			return new PositionExpression(10, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression11(PositionExpression pe1, PositionExpression pe21, int timeStamp) {
		if(pe1 != null && pe21 != null) {
			averageDistance = (pe1.getMeasuredDistance() + pe21.getMeasuredDistance()) / 2;
			return new PositionExpression(11, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression12(PositionExpression pe11, PositionExpression pe13, int timeStamp) {
		if(pe11 != null && pe13 != null) {
			averageDistance = INTERPOLATE_POINTS(pe11.getMeasuredDistance(), pe13.getMeasuredDistance());
			return new PositionExpression(12, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression13(PositionExpression pe3, PositionExpression pe23, int timeStamp) {
		if(pe3 != null && pe23 != null) {
			averageDistance = (pe3.getMeasuredDistance() + pe23.getMeasuredDistance()) / 2;
			return new PositionExpression(13, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression14(PositionExpression pe13, PositionExpression pe15, int timeStamp) {
		if(pe13 != null && pe15 != null) {
			averageDistance = INTERPOLATE_POINTS(pe15.getMeasuredDistance(), pe13.getMeasuredDistance());
			return new PositionExpression(14, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression15(PositionExpression pe5, PositionExpression pe25, int timeStamp) {
		if(pe5 != null && pe25 != null) {
			averageDistance = (pe5.getMeasuredDistance() + pe25.getMeasuredDistance()) / 2;
			return new PositionExpression(15, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression16(PositionExpression pe11, PositionExpression pe21, int timeStamp) {
		if(pe11 != null && pe21 != null) {
			averageDistance = (pe11.getMeasuredDistance() + pe21.getMeasuredDistance()) / 2;
			return new PositionExpression(16, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression17By12and22(PositionExpression pe12, PositionExpression pe22, int timeStamp) {
		if(pe12 != null && pe22 != null) {
			averageDistance = (pe12.getMeasuredDistance() + pe22.getMeasuredDistance()) / 2;
			return new PositionExpression(17, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression17By16and18(PositionExpression pe16, PositionExpression pe18, int timeStamp) {
		if(pe16 != null && pe18 != null) {
			averageDistance = INTERPOLATE_POINTS(pe16.getMeasuredDistance(), pe18.getMeasuredDistance());
			return new PositionExpression(17, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression18(PositionExpression pe13, PositionExpression pe23, int timeStamp) {
		if(pe13 != null && pe23 != null) {
			averageDistance = (pe13.getMeasuredDistance() + pe23.getMeasuredDistance()) / 2;
			return new PositionExpression(18, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression19By14and24(PositionExpression pe14, PositionExpression pe24, int timeStamp) {
		if(pe14 != null && pe24 != null) {
			averageDistance = (pe14.getMeasuredDistance() + pe24.getMeasuredDistance()) / 2;
			return new PositionExpression(19, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression19By18and20(PositionExpression pe18, PositionExpression pe20, int timeStamp) {
		if(pe18 != null && pe20 != null) {
			averageDistance = INTERPOLATE_POINTS(pe20.getMeasuredDistance(), pe18.getMeasuredDistance());
			return new PositionExpression(19, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression20(PositionExpression pe15, PositionExpression pe25, int timeStamp) {
		if(pe15 != null && pe25 != null) {
			averageDistance = (pe15.getMeasuredDistance() + pe25.getMeasuredDistance()) / 2;
			return new PositionExpression(20, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression22(PositionExpression pe21, PositionExpression pe23, int timeStamp) {
		if(pe21 != null && pe23 != null) {
			averageDistance = INTERPOLATE_POINTS(pe21.getMeasuredDistance(), pe23.getMeasuredDistance());
			return new PositionExpression(22, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	
	public static PositionExpression calculateExpression24(PositionExpression pe23, PositionExpression pe25, int timeStamp) {
		if(pe23 != null && pe25 != null) {
			averageDistance = INTERPOLATE_POINTS(pe25.getMeasuredDistance(), pe23.getMeasuredDistance());
			return new PositionExpression(24, timeStamp, averageDistance, true);
		} else {
			return null;
		}
	}
	*/
	
	
	public static int INTERPOLATE_POINTS(Integer dist_angle, Integer dist_straight) {
		if(dist_angle == null) {
			return 0;
		}
		
		if(dist_straight == null) {
			return 0;
		}
		
		double distance_h_i = Math.sqrt(SQUARE(CALC_D, dist_angle) + SQUARE(CALC_C, dist_straight) - (2 * (dist_angle + CALC_D) * (dist_straight + CALC_C) * Math.cos(CALC_ALPHA)));

		double cos_delta = (SQUARE(dist_angle, CALC_D) - distance_h_i * distance_h_i - SQUARE(dist_straight, CALC_C)) / ((-2) * distance_h_i * (dist_straight + CALC_C));
		
		double delta = Math.acos(cos_delta);
		
		double beta = Math.PI - CALC_ALPHA - delta;
		
		double gamma = Math.PI - beta - CALC_ALPHA / 2;
		
		double distance_b_g = (Math.sin(beta) / Math.sin(gamma)) * (dist_angle + CALC_D);
		
		double interpolated_distance = distance_b_g - CALC_G;
		
		return (int) Math.round(interpolated_distance);
	}
	
	private static double SQUARE(double a1, double a2) {
		return (a1 + a2) * (a1 + a2);
	}
}
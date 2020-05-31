package Caches;
 
import java.util.concurrent.CopyOnWriteArrayList;

import setup.Expressions.ArcExpression;

public class SensorDataLayerCachesPC {
	
	private static boolean firstLayerChanged = false;
	private static boolean secondLayerChanged = false;
	private static boolean thirdLayerChanged = false;
	private static boolean fourthLayerChanged = false;
	
	private static boolean addingArcsFirstLayer = true;
	private static boolean addingArcsSecondLayer = true;
	private static boolean addingArcsThirdLayer = true;
	private static boolean addingArcsFourthLayer = true;
	
//	Use CopyOnWriteArrayList to have a efficient and synchronized (thread-safe) list
//	Only add ArcExpressions to these lists, else the lists will throw an error.
	public static CopyOnWriteArrayList<Object> firstLayer = new CopyOnWriteArrayList<Object>() { 
		/**
		 * Only use ArcExpression"
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(Object arcExpression) {
			try  {
				checkData(arcExpression, "firstLayer");
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(isAddingArcsFirstLayer() == true) {
				return super.add(arcExpression);
			} else {
				return false;
			}
		}
	};
	
	public static CopyOnWriteArrayList<Object> secondLayer = new CopyOnWriteArrayList<Object>(){ 
		/**
		 * Only use ArcExpression"
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(Object arcExpression) {
			try  {
				checkData(arcExpression, "secondLayer");
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(isAddingArcsSecondLayer() == true) {
				return super.add(arcExpression);
			} else {
				return false;
			}
		}
	};
	
	public static CopyOnWriteArrayList<Object> thirdLayer = new CopyOnWriteArrayList<Object>() { 
		/**
		 * Only use ArcExpression"
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(Object arcExpression) {
			try  {
				checkData(arcExpression, "thirdLayer");
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(isAddingArcsThirdLayer() == true) {
				return super.add(arcExpression);
			} else {
				return false;
			}
		}
	};
	
	public static CopyOnWriteArrayList<Object> fourthLayer = new CopyOnWriteArrayList<Object>() { 
		/**
		 * Only use ArcExpression"
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(Object arcExpression) {
			try  {
				checkData(arcExpression, "fourthLayer");
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(isAddingArcsFourthLayer() == true) {
				return super.add(arcExpression);
			} else {
				return false;
			}
		}
	};
	
	private static void checkData(Object o, String listName) throws IllegalArgumentException {
		if(o == null) {
			throw new IllegalArgumentException("\nEin Objekt, welches zum " + listName + " hinzugefügt werden sollte, ist null!");
		} else if (o instanceof ArcExpression) {
			setLayerChange(true, ((ArcExpression) o).getLayerNumber());
			return;
		} else { 
			throw new IllegalArgumentException("\nEin Objekt, welches zum " + listName + " hinzugefügt werden sollte, ist keine ArcExpression!");
		}
	}
	
	public static int getLayerNumberByName(CopyOnWriteArrayList<Object> layer) {
		if(layer == null) {
			throw new NullPointerException("Der Parameter list ist null!");
		} else if(layer.hashCode() == firstLayer.hashCode()) {
			return 1;
		} else if(layer.hashCode() == secondLayer.hashCode()) {
			return 2;
		} else if(layer.hashCode() == thirdLayer.hashCode()) {
			return 3;
		} else if(layer.hashCode() == fourthLayer.hashCode()) {
			return 4;
		}
		throw new IllegalArgumentException("Der Parameter list übergibt nicht eine der vier SensorDataLayerCaches!");
	}
	
	public static String getLayerNameByNumber(int layerNumber) {
		if(layerNumber == 1) {
			return "First layer";
		} else if(layerNumber == 2) {
			return "Second layer";
		} else if(layerNumber == 3) {
			return "Third layer";
		} else if(layerNumber == 4) {
			return "Fourth layer";
		}
		throw new IllegalArgumentException("Der Parameter layerNumber übergibt nicht eine der vier SensorDataLayerCaches-Nummern!");
	}
	
	public static void setLayerChange(boolean bol, int layerNumber) {
		if(layerNumber == 1) {
			firstLayerChanged = bol;
		} else if (layerNumber == 2) {
			secondLayerChanged = bol;
		} else if (layerNumber == 3) {
			thirdLayerChanged = bol;
		} else if (layerNumber == 4) {
			fourthLayerChanged = bol;
		}
	}
	
	public static boolean hasLayerChanged(int layerNumber) {
		if(layerNumber == 1) {
			return firstLayerChanged;
		} else if (layerNumber == 2) {
			return secondLayerChanged;
		} else if (layerNumber == 3) {
			return thirdLayerChanged;
		} else if (layerNumber == 4) {
			return fourthLayerChanged;
		}
		return false;
	}
	
	public static CopyOnWriteArrayList<Object> getLayerByNumber(int layerNumber) {
		if(layerNumber == 1) {
			return firstLayer;
		} else if (layerNumber == 2) {
			return secondLayer;
		} else if (layerNumber == 3) {
			return thirdLayer;
		} else if (layerNumber == 4) {
			return thirdLayer;
		}
		throw new IllegalArgumentException("This layerNumber - " + layerNumber + " - is not allowed!");
	}

	public static void setIsAddingArcsByLayerNumber(int layerNumber, boolean bol) {
		if(layerNumber == 1) {
			setAddingArcsFirstLayer(bol);
			return;
		} else if (layerNumber == 2) {
			setAddingArcsSecondLayer(bol);
			return;
		} else if (layerNumber == 3) {
			setAddingArcsThirdLayer(bol);
			return;
		} else if (layerNumber == 4) {
			setAddingArcsFourthLayer(bol);
			return;
		}
		throw new IllegalArgumentException("This layerNumber - " + layerNumber + " - is not allowed!");
	}
	
	public static boolean isAddingArcsByLayerNumber(int layerNumber) {
		if(layerNumber == 1) {
			return isAddingArcsFirstLayer();
		} else if (layerNumber == 2) {
			return isAddingArcsSecondLayer();
		} else if (layerNumber == 3) {
			return isAddingArcsThirdLayer();
		} else if (layerNumber == 4) {
			return isAddingArcsFourthLayer();
		}
		throw new IllegalArgumentException("This layerNumber - " + layerNumber + " - is not allowed!");
	}
	
	public static boolean isAddingArcsFirstLayer() {
		return addingArcsFirstLayer;
	}

	public static void setAddingArcsFirstLayer(boolean addingArcsFirstLayer) {
		SensorDataLayerCachesPC.addingArcsFirstLayer = addingArcsFirstLayer;
	}

	public static boolean isAddingArcsSecondLayer() {
		return addingArcsSecondLayer;
	}

	public static void setAddingArcsSecondLayer(boolean addingArcsSecondLayer) {
		SensorDataLayerCachesPC.addingArcsSecondLayer = addingArcsSecondLayer;
	}

	public static boolean isAddingArcsThirdLayer() {
		return addingArcsThirdLayer;
	}

	public static void setAddingArcsThirdLayer(boolean addingArcsthirdLayer) {
		SensorDataLayerCachesPC.addingArcsThirdLayer = addingArcsthirdLayer;
	}

	public static boolean isAddingArcsFourthLayer() {
		return addingArcsFourthLayer;
	}

	public static void setAddingArcsFourthLayer(boolean addingArcsFourthLayer) {
		SensorDataLayerCachesPC.addingArcsFourthLayer = addingArcsFourthLayer;
	}
}

package threads.Organize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Caches.PositionExpressionCacheSorter;
import Caches.SensorDataLayerCachesPC;
import setup.Expressions.ArcExpression;
import setup.Expressions.PositionExpression;
 

public class ArcLayerToPositionLayerThread extends Thread {
	
	private boolean firstLayer = false;
	private boolean secondLayer = false;
	
	public ArcLayerToPositionLayerThread(boolean forFirstLayer, boolean forSecondLayer) {
		if(forFirstLayer) {
			setFirstLayer(true);
		}
		if(forSecondLayer) {
			setSecondLayer(true);
		}
	} 
	
	@Override
	public void run() {
		List<ArcExpression> collection = new ArrayList<ArcExpression>();
		new PositionExpressionCacheSorter(hasFirstLayer(), hasSecondLayer());
		 
		if(hasFirstLayer() && hasSecondLayer()) {
			Iterator<Object> firstLayerIterator;
			Iterator<Object> secondLayerIterator;
			ArcExpression currentArc;
			PositionExpression positionExpression;
			
			while(true) {
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				firstLayerIterator = SensorDataLayerCachesPC.firstLayer.iterator(); 
				secondLayerIterator = SensorDataLayerCachesPC.secondLayer.iterator();
				while(firstLayerIterator.hasNext()) {
					currentArc = (ArcExpression) firstLayerIterator.next();
					collection.add(currentArc);
					if(currentArc != null) {
						positionExpression = new PositionExpression(currentArc);
						if(positionExpression != null) { 
							PositionExpressionCacheSorter.addPositionExpressionToJson(positionExpression); 
						}
						currentArc = null;
					}
				}
				
				SensorDataLayerCachesPC.firstLayer.removeAll(collection);
				collection.clear();
				
				while(secondLayerIterator.hasNext()) {
					currentArc = (ArcExpression) secondLayerIterator.next();
					collection.add(currentArc);
					if(currentArc != null) {
						positionExpression = new PositionExpression(currentArc);
						if(positionExpression != null) { 
							PositionExpressionCacheSorter.addPositionExpressionToJson(positionExpression);
						}
						currentArc = null;
					}
				}
				
				SensorDataLayerCachesPC.secondLayer.removeAll(collection);
				collection.clear();
			}
			
		} /*else if(hasFirstLayer()) {
			Iterator<Object> firstLayerIterator;
			ArcExpression currentArc;
			PositionExpression positionExpression;
			
			while(true) {
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				firstLayerIterator = SensorDataLayerCachesPC.firstLayer.iterator();
				
				while(firstLayerIterator.hasNext()) {
					currentArc = (ArcExpression) firstLayerIterator.next();
					collection.add(currentArc);
					if(currentArc != null) {
						positionExpression = new PositionExpression(currentArc);
						if(positionExpression != null) {
							PositionExpressionCacheSorter.addPositionExpressionToJson(positionExpression);
						}
						currentArc = null;
					}
				}
				
				SensorDataLayerCachesPC.firstLayer.removeAll(collection);
				collection.clear();
			}
			
		} else if(hasSecondLayer()) {
			Iterator<Object> secondLayerIterator;
			ArcExpression currentArc;
			PositionExpression positionExpression;
			
			while(true) {
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				secondLayerIterator = SensorDataLayerCachesPC.secondLayer.iterator();
				
				while(secondLayerIterator.hasNext()) {
					currentArc = (ArcExpression) secondLayerIterator.next();
					collection.add(currentArc);
					if(currentArc != null) {
						positionExpression = new PositionExpression(currentArc);
						if(positionExpression != null) {
							PositionExpressionCacheSorter.addPositionExpressionToJson(positionExpression);
						}
						currentArc = null;
					}
				}
				
				SensorDataLayerCachesPC.secondLayer.removeAll(collection);
				collection.clear();
			}
		}*/
	}

	private boolean hasFirstLayer() {
		return firstLayer;
	}

	private void setFirstLayer(boolean firstLayer) {
		this.firstLayer = firstLayer;
	}

	private boolean hasSecondLayer() {
		return secondLayer;
	}

	private void setSecondLayer(boolean secondLayer) {
		this.secondLayer = secondLayer;
	}
}

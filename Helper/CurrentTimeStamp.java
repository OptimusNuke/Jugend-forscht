package Helper;
 
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CurrentTimeStamp {
	protected PropertyChangeSupport propertyChangeSupport;
	private Integer currentTimeStamp = null;
	private Integer cache1TimeStamp = null;
	private Integer cache2TimeStamp = null;
	
	public CurrentTimeStamp(Integer currentTimeStamp) {
		propertyChangeSupport = new PropertyChangeSupport(this);
		this.currentTimeStamp = currentTimeStamp;
	}

	public void setCurrentTimeStamp(Integer currentTimeStamp) {
		if(cache1TimeStamp == null) {
			this.cache1TimeStamp = currentTimeStamp;
			return;
		}
		
		if(cache2TimeStamp == null) {
			this.cache2TimeStamp = cache1TimeStamp;
			return;
		}
		
		if(currentTimeStamp.equals(this.currentTimeStamp)) {
			propertyChangeSupport.firePropertyChange("NextPositionForTimeStamp", cache2TimeStamp, cache1TimeStamp);
			return;
		}
		
		cache2TimeStamp = cache1TimeStamp;
		
		cache1TimeStamp = this.currentTimeStamp;
		
		this.currentTimeStamp = currentTimeStamp;
		
		//Fires only if a the timeStamp reached the end of the procedure and the PC is receiving a new timeStamp
		propertyChangeSupport.firePropertyChange("CurrentTimeStampChanged", cache2TimeStamp, cache1TimeStamp);
	}
	
	public Integer getTimeStamp() {	//To use after the listener detected an change.
		return cache1TimeStamp;
	}
	
	public Integer getNewestTimeStamp() {	//To use for PositionExpressionCache
		return currentTimeStamp;
	}
	
	 public void addPropertyChangeListener(PropertyChangeListener listener) {
		 propertyChangeSupport.addPropertyChangeListener(listener);
	 }
	 
	 public void removePropertyChangeListener(PropertyChangeListener listener) {
         propertyChangeSupport.removePropertyChangeListener(listener);
	 }
}
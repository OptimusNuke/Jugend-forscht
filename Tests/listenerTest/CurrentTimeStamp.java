package Tests.listenerTest;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CurrentTimeStamp {
	protected PropertyChangeSupport propertyChangeSupport;
	private Integer currentTimeStamp;
	
	public CurrentTimeStamp(Integer currentTimeStamp) {
		propertyChangeSupport = new PropertyChangeSupport(this);
		this.currentTimeStamp = currentTimeStamp;
	}
	
	public void setCurrentTimeStamp(Integer currentTimeStamp) {
		Integer oldTimeStamp = this.currentTimeStamp;
		this.currentTimeStamp = currentTimeStamp;
		if(currentTimeStamp != oldTimeStamp) 
			propertyChangeSupport.firePropertyChange("CurrentTimeStampChanged", oldTimeStamp, this.currentTimeStamp);
	}
	
	 public void addPropertyChangeListener(PropertyChangeListener listener) {
		 propertyChangeSupport.addPropertyChangeListener(listener);
	 }
}

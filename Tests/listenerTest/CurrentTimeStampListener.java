package Tests.listenerTest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CurrentTimeStampListener implements PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if("CurrentTimeStampChanged".equals(event.getPropertyName())) {
			System.out.println("CHANGE! " + event.getNewValue().toString());
		}
		
	}
}
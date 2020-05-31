package Tests.listenerTest;

public class CurrentTimeStampListenerTest {
	public static void main(String[] args) {
		CurrentTimeStamp timestamp = new CurrentTimeStamp(1);
		CurrentTimeStampListener listener = new CurrentTimeStampListener();
		timestamp.addPropertyChangeListener(listener);
		timestamp.setCurrentTimeStamp(1);
		timestamp.setCurrentTimeStamp(2);
		timestamp.setCurrentTimeStamp(3);
		timestamp.setCurrentTimeStamp(3);
		timestamp.setCurrentTimeStamp(4);
	}
}

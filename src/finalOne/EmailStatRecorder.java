package finalOne;

public class EmailStatRecorder extends EmailStatObserver {
	
	public void update(String notification) {
		System.out.println(notification);
	}
	
}

package finalOne;

public abstract class MyThread {
	private boolean isRunning;
	
	public MyThread() {isRunning = false;}

	public void start() {
		isRunning = true;
	}

	public void stop() {
		isRunning = false;
	}
	
	public boolean isAlive() {
		return isRunning;
	}

}

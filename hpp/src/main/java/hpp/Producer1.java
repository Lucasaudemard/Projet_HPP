package hpp;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class Producer1 implements Runnable {
	
	private BlockingQueue<Event> bq;
	private String directory;
	
	
	public Producer1(BlockingQueue<Event> bq, String directory) {
		super();
		this.bq = bq;
		this.directory = directory;
	}

	
	/**
	 * Opens the file specified in the directory and reads the file. The line read is then processed to create an Event object that is put in the BlockingQueue
	 * When the blocking queue is full waits a certain time to continue filling it
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// TODO add a wait when the blocking queue is full 
	}

}

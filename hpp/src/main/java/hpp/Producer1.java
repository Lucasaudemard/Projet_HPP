package hpp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

//Producing comments 
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
		try {
			FileInputStream fstream = new FileInputStream(directory);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
			String line;
			try {
				while ((line = br.readLine()) != null) {
					//TODO implement a system of exceptions
					
					String [] lineList = line.split("[|]");
					LocalDateTime dateTime = LocalDateTime.parse(line.split("[+]")[0]);
					Comment c = new Comment(dateTime,lineList[1],lineList[2],lineList[lineList.length-1]);
					bq.add(c);
					System.out.println(dateTime + "  "+  lineList[1] +"  " +lineList[2]+"  "+ lineList[lineList.length-1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}

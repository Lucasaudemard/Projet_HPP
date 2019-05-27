package hpp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

/**
 * Producer of the Comment BlockingQueue
 * @author Florian
 *
 */
public class Producer1 implements Runnable {
	
	private BlockingQueue<Event> bq;
	private String directory;
	
	/**
	 * 
	 * @param bq blocking queue where to store the Comments
	 * @param directory where the source file is located
	 */
	public Producer1(BlockingQueue<Event> bq, String directory) {
		super();
		this.bq = bq;
		this.directory = directory;
	}
	
	
	/**
	 * Opens the file specified in the directory and reads the file. The line read is then processed to create an Event object that is put in the BlockingQueue
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			FileInputStream fstream = new FileInputStream(directory);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
			String line;
			try {
				while ((line = br.readLine()) != null) {
	
					String [] lineList = line.split("[|]");
					LocalDateTime dateTime = LocalDateTime.parse(line.split("[+]")[0]);
					Comment c = new Comment(dateTime,lineList[1],lineList[2],lineList[lineList.length-1]);
					try {
						bq.put(c);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						
					}
					//System.out.println(dateTime + "  "+  lineList[1] +"  " +lineList[2]+"  "+ lineList[lineList.length-1]);
				}
				Comment c = new Comment(LocalDateTime.parse("0000-01-01T00:00:00.000"),"PoisonPill","PoisonPill","PoisonPill");
				try {
					bq.put(c);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		System.out.println("Prod 1 : finished");
	}

}

package hpp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class Main {
	//Tests on full dataset 
	public static void main(String[] args) {
		String commentsPath = "C:\\Users\\Florian\\Desktop\\HPP project\\Data HPP project\\comments.dat";
		String postsPath = "C:\\Users\\Florian\\Desktop\\HPP project\\Data HPP project\\posts.dat";
		String outputPath = "C:\\Users\\Florian\\Desktop\\HPP project\\Data HPP project\\output.dat";
		BlockingQueue<Event> bqComments = new ArrayBlockingQueue<Event>(1000000);
		BlockingQueue<Event> bqPosts = new ArrayBlockingQueue<Event>(1000000);
		Producer1 commentProducer = new Producer1(bqComments, commentsPath);
		Producer2 postProducer = new Producer2(bqPosts, postsPath);
		Ordonnanceur ord = new Ordonnanceur(bqComments, bqPosts, outputPath);
		ExecutorService pool = Executors.newFixedThreadPool(3);
		pool.execute(commentProducer);
		pool.execute(postProducer);
		pool.execute(ord);
		
		// Finish
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}



}


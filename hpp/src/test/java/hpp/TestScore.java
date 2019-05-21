package hpp;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

public class TestScore {

	@Test
	public void testDecrementScore() throws InterruptedException {
		
		String str = "";
		
		//création de la blockingQueue
		BlockingQueue<Event> bq1 = new ArrayBlockingQueue<Event>(400);
		Producer1 p1 = new Producer1(bq1,"/Users/joulin/Documents/Cours TSE FISE2/Projet HPP/test_comments.dat");
		p1.run();
		

		BlockingQueue<Event> bq2 = new ArrayBlockingQueue<Event>(400);
		Producer2 p2 = new Producer2(bq2,"/Users/joulin/Documents/Cours TSE FISE2/Projet HPP/test_posts2.dat");
		p2.run();
		
		Ordonnanceur ord = new Ordonnanceur(bq1, bq2, str);
		
		while (!bq1.isEmpty() && !bq2.isEmpty()) {

			ord.chooseToQueue();
			
			ord.hashId();

			ord.decrementScores();
			
		}	
	}

}

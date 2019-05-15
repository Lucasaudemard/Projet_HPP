package hpp;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

public class TestDay {

	@Test
	public void test() {
		LocalDateTime currentTime = LocalDateTime.parse("2010-02-22T09:20:32.064");
		BlockingQueue<Event> bq = new ArrayBlockingQueue<Event>(20);
		Producer2 p2 = new Producer2(bq,"/Users/joulin/Documents/Cours TSE FISE2/Projet HPP/test_posts.dat");
		p2.run();

		List<Event> listEvents = new LinkedList<Event>();
		for (int i=0;i<15;i++) {
			listEvents.add(bq.remove());
		}

		System.out.println(listEvents);
		Day day1 = new Day(currentTime, 24, listEvents);
		assertEquals(4, day1.compareTS().size());
		
	}

}

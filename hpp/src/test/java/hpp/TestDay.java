package hpp;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

public class TestDay {

	
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
	
	
	public void testAdd() {
		LocalDateTime currentTime = LocalDateTime.parse("2010-02-22T09:20:32.064");
		BlockingQueue<Event> bq = new ArrayBlockingQueue<Event>(20);
		Producer2 p2 = new Producer2(bq,"/Users/joulin/Documents/Cours TSE FISE2/Projet HPP/test_posts.dat");
		p2.run();

		List<Event> listEvents = new LinkedList<Event>();
		for (int i=0;i<15;i++) {
			listEvents.add(bq.remove());
		}

		List<Event> newEvent = new LinkedList<Event>();
		newEvent.add(bq.remove());
		
		System.out.println(listEvents);
		Day day1 = new Day(currentTime, 24, listEvents);
		day1.addEvents(newEvent);
		assertEquals(16, day1.listEvent.size());
	}
	
	@Test
	public void testRemove() {
		LocalDateTime currentTime = LocalDateTime.parse("2010-02-22T09:20:32.064");
		BlockingQueue<Event> bq = new ArrayBlockingQueue<Event>(20);
		Producer2 p2 = new Producer2(bq,"/Users/joulin/Documents/Cours TSE FISE2/Projet HPP/test_posts.dat");
		p2.run();

		List<Event> listEvents = new LinkedList<Event>();
		for (int i=0;i<16;i++) {
			listEvents.add(bq.remove());
		}

		List<Event> badEvent = new LinkedList<Event>();
		badEvent.add(listEvents.get(15));
		
		
		Day day1 = new Day(currentTime, 24, listEvents);
		day1.removeEvents(badEvent);

		assertEquals(15, day1.listEvent.size());
	}

}

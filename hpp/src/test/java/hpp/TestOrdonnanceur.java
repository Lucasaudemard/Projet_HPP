package hpp;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

public class TestOrdonnanceur {
	
	BlockingQueue<Event> p1 = new ArrayBlockingQueue<Event>(20);
	BlockingQueue<Event> p2 = new ArrayBlockingQueue<Event>(20);
	
	LocalDateTime now1 = LocalDateTime.now();
	LocalDateTime now2 = now1.minusHours(3);
	
	Event post1 = new Post(now1, "100", null);
	Event com2 = new Comment(now2, "200", null, "100");
	Event com3 = new Comment(null, null, null, null);
	Event com4 = new Comment(null, null, null, null);
	
	
	
	Ordonnanceur t3 = new Ordonnanceur(p1, p2);

	@Test
	public void bonObj() throws InterruptedException {
		
		p1.add(post1);
		p2.add(com2);
		//p2.add(com3);
		//p2.add(com4);
		
		t3.chooseToQueue();
		assertEquals(com2.getClass().getName(), t3.getCurrentObj().getClass().getName());
		assertEquals("200", com2.getId());
		
			
	}
	
	@Test
	public void idCommentToPost() throws InterruptedException{
		
		//t3.hashId();
		//t3.getPostToPostObj().put(post1.getId(), post1);
				
		//assertEquals(1, t3.getPostToPostObj().size());
		
		assertEquals("100", ((Comment) com2).getPostRepliedId());
		
		
	}
	
	@Test
	public void ajoutHashMap() throws InterruptedException {
		
		t3.getPostToPostObj().put("100", post1);
		
		
		p1.add(post1);
		p2.add(com2);
		
		
		
		t3.chooseToQueue();
		t3.hashId();
		
		assertEquals(1, t3.getComToPost().size());
		
		
	}
	
	@Test
	public void contientElement() throws InterruptedException {
		
		this.ajoutHashMap();
		
		assertEquals(post1.getId(), t3.getComToPost().get(t3.getCurrentObj().getId()));
	}

}

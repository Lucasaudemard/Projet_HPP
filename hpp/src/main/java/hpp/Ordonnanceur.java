package hpp;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Ordonnanceur implements Runnable {
	
	
	private HashMap<String, String> comToPost;
	private HashMap<String, Event> postToObjPost;
	
	private BlockingQueue<Event> produce1;
	private BlockingQueue<Event> produce2;
	
	public LocalDateTime ts;
	
	private Event currentObj;
	
	
	public Ordonnanceur(BlockingQueue<Event> p1, BlockingQueue<Event> p2 ) {
		
		
		this.comToPost = new HashMap<>();
		this.postToObjPost = new HashMap<>();
		
		this.produce1 = p1;
		this.produce2 = p2;
		
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		// TODO add a wait when the blocking queue is full 
	}
	
	public void chooseToQueue(BlockingQueue<Event> p1, BlockingQueue<Event> p2) throws InterruptedException {
		
		Event obj1 = p1.peek();
		Event obj2 = p2.peek();
		
		
		if(obj1.getTs().isBefore(obj2.getTs())) {
			
			this.currentObj = p1.take();
			
		}
		else {
			
			this.currentObj = p2.take();
		}
		
	}
	
	
	
	public void hashId() {
		
		
		String currentId = this.currentObj.getId();
		
		if(this.currentObj.getClass().getName() == "hpp.Post") {
			
			this.postToObjPost.put(currentId, this.currentObj);
			
		}
		else {
			
			if(this.currentObj.getPostReplied().getClass().getName() == "hpp.Post") {
				
				this.comToPost.put(currentId, this.currentObj.getPostReplied());
					
			}
			else {
					
				this.comToPost.put(currentId, comToPost.get(this.currentObj.getPostReplied()));
					
			}
		}		
	}
		
		

}

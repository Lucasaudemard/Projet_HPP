package hpp;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Ordonnanceur implements Runnable {
	
	private List<String> listCom;
	private HashMap<String, List<String>> comToPost;
	private HashMap<String, Post> postToObjPost;
	
	private BlockingQueue<Event> produce1;
	private BlockingQueue<Event> produce2;
	
	public LocalDateTime ts;
	
	private Event currentObj;
	
	
	public Ordonnanceur(BlockingQueue<Event> p1, BlockingQueue<Event> p2 ) {
		
		this.listCom = new ArrayList<String>();
		this.comToPost = new HashMap<>();
		this.postToObjPost = new HashMap<>();
		
		this.produce1 = p1;
		this.produce2 = p2;
		
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		// TODO add a wait when the blocking queue is full 
	}
	
	public void chooseToQueue(BlockingQueue<Event> p1, BlockingQueue<Event> p2) {
		
		Event obj1 = p1.peek();
		Event obj2 = p2.peek();
		
		
		if(obj1.getTs().isBefore(obj2.getTs())) {
			
			this.currentObj = obj1;
			
		}
		else {
			
			this.currentObj = obj2;
		}
		
	}
	
	
	
	public void hashId() {
		
		/*String comId = com.getId();
		this.listCom.add(comId);
		String postId = post.getId();
		
		this.comToPost.put(postId, this.listCom);*/
		
		if(this.currentObj.getClass().getName() == "hpp.Post") {
			
			this.comToPost.put(this.currentObj.getId(), null);
			
		}
		else {
			
			this.listCom.add(this.currentObj.getId());
			
			
		}
		
			
	}
	
	
	public void hashObj(Post post) {
		
		String postId = post.getId();
		
		this.postToObjPost.put(postId, post);
	}

}

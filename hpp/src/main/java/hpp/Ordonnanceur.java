package hpp;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayList;
import java.util.HashMap;

public class Ordonnanceur implements Runnable {
	
	private List<String> listCom;
	private HashMap<String, List<String>> comToPost;
	private HashMap<String, Post> postToObjPost;
	
	private BlockingQueue<Event> produce1;
	private BlockingQueue<Event> produce2;
	
	
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
	
	
	
	public void hashId(Event com, Event post) {
		
		/*String comId = com.getId();
		this.listCom.add(comId);
		String postId = post.getId();
		
		this.comToPost.put(postId, this.listCom);*/
		
		if(com.getClass().getName() == "hpp.Post") {
			
			
			
		}
		
			
	}
	
	
	public void hashObj(Post post) {
		
		String postId = post.getId();
		
		this.postToObjPost.put(postId, post);
	}

}

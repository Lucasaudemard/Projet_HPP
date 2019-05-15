package hpp;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Ordonnanceur implements Runnable {
	
	private List<String> listCom;
	private HashMap<String, List<String>> comToPost;
	private HashMap<String, Post> postToObjPost;
	
	
	
	public Ordonnanceur() {
		
		listCom = new ArrayList<String>();
		comToPost = new HashMap<>();
		postToObjPost = new HashMap<>();
		
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		// TODO add a wait when the blocking queue is full 
	}
	
	public void hashId(Event com, Event post) {
		
		String comId = com.getId();
		this.listCom.add(comId);
		String postId = post.getId();
		
		this.comToPost.put(postId, this.listCom);
		
			
	}
	
	
	public void hashObj(Post post) {
		
		String postId = post.getId();
		
		this.postToObjPost.put(postId, post);
	}

}

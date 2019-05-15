package hpp;

import java.util.HashMap;

public class Ordonnanceur implements Runnable {
	
	
	private HashMap<Integer, Integer> comToPost;
	private HashMap<Integer, Post> postToObjPost;
	
	
	
	public Ordonnanceur() {
		
		comToPost = new HashMap<>();
		postToObjPost = new HashMap<>();
		
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		// TODO add a wait when the blocking queue is full 
	}
	
	public void hashId(Comment com, Post post) {
		
		//int comId = com.getId();
		
		
	}

}

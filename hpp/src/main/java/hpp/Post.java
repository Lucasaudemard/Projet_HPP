package hpp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;

public class Post implements Event {
	private LocalDateTime timeStamp;
	private String postId;
	private String userName;
	private int internScore;
	private int externScore;
	private int commenter;
	LinkedHashSet<String> commentHashSet = new LinkedHashSet<String>();
	
	
	
	public Post(LocalDateTime timeStamp, String postId, String userName) {
		super();
		this.timeStamp = timeStamp;
		this.postId = postId;
		this.userName = userName;
		this.internScore = 10;
		this.externScore = 0;
		this.commenter = 0;
	}


	@Override
	public String getId() {
		return this.postId;
	}
	
	@Override
	public LocalDateTime getTs() {
		return this.timeStamp;
	}

	public void decreaseInternScore() {
		this.internScore--;
	}
	
	
	public void addNewComment(String userId) {
		//TODO functionnality to add the commenter_id to the hashmap
		this.externScore+=10;
		if (this.commentHashSet.add(userId)) this.commenter++;
	}

	public void decreaseExternScore() {
		this.externScore--;
		
	}
	
	public int getScore() {
		return this.internScore+this.externScore;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public int getCommenters() {
		return this.commenter;
	}

}

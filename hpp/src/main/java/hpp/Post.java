package hpp;

import java.time.LocalDateTime;

public class Post implements Event {
	private LocalDateTime timeStamp;
	private String postId;
	private String userName;
	private int internScore;
	private int externScore;
	private int commenter;
	
	
	
	
	
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
		// TODO Auto-generated method stub
		return this.postId;
	}

	@Override
	public void changeId(String newId) {
		// TODO Auto-generated method stub
		this.postId = newId;
	}

	@Override
	public LocalDateTime getTs() {
		// TODO Auto-generated method stub
		return this.timeStamp;
	}


	@Override
	public void decreaseInternScore() {
		// TODO Auto-generated method stub
		this.internScore--;
	}


	@Override
	public void addNewComment() {
		// TODO Auto-generated method stub
		this.externScore+=10;
	}


	@Override
	public void decreaseExternScore() {
		// TODO Auto-generated method stub
		this.externScore--;
		
	}
	
	public int getScore() {
		return this.internScore+this.externScore;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
}

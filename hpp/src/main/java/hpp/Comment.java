package hpp;

import java.time.LocalDateTime;

public class Comment implements Event {
	private LocalDateTime timeStamp;
	private String commentId;
	private String userName;
	private String postReplied;
	private int internScore;
	
	
	
	
	public Comment(LocalDateTime timeStamp, String commentId, String userName, String postReplied) {
		super();
		this.timeStamp = timeStamp;
		this.commentId = commentId;
		this.userName = userName;
		this.postReplied = postReplied;
		this.internScore = 10;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.commentId;
	}

	
	@Override
	public void changeId(String newId) {
		// TODO Auto-generated method stub
		this.postReplied = newId;
	}
	
	//Return the Id directed linked to this comment (It can be a post_id or comment_id)
	@Override
	public String getPostReplied() {
		return this.postReplied;
	}

	@Override
	public LocalDateTime getTs() {
		// TODO Auto-generated method stub
		return this.timeStamp;
	}

	@Override
	public void decreaseInternScore() {
		// TODO Auto-generated method stub
		this.internScore --;
	}
	
	@Override
	public void addNewComment() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void decreaseExternScore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return this.internScore;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	

}

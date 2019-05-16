package hpp;

import java.time.LocalDateTime;

public class Comment implements Event {
	private LocalDateTime timeStamp;
	private String commentId;
	private String userId;
	private String postReplied;
	
	
	public Comment(LocalDateTime timeStamp, String commentId, String userId, String postReplied) {
		super();
		this.timeStamp = timeStamp;
		this.commentId = commentId;
		this.userId = userId;
		this.postReplied = postReplied;
	}
	
	
	@Override
	public String getId() {
		return this.commentId;
	}
	
	@Override
	public LocalDateTime getTs() {
		return this.timeStamp;
	}

	public void changeId(String newId) {
		this.postReplied = newId;
	}
	
	//Return the Id directed linked to this comment (It can be a post_id or comment_id)
	public String getPostRepliedId() {
		return this.postReplied;
	}

	public String getUserId() {
		return this.userId;
	}



}

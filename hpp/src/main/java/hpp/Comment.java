package hpp;

import java.time.LocalDateTime;

/**
 * 
 * @author Florian
 *
 */
public class Comment implements Event {
	private LocalDateTime timeStamp;
	private String commentId;
	private String userId;
	private String postReplied;
	
	/**
	 * 
	 * @param timeStamp timestamp of the comment 
	 * @param commentId unique ID of the comment 
	 * @param userId unique ID of the user that posted the comment 
	 * @param postReplied ID of the related comment if it's a response to a comment or of the related post if it's a comment
	 */
	public Comment(LocalDateTime timeStamp, String commentId, String userId, String postReplied) {
		super();
		this.timeStamp = timeStamp;
		this.commentId = commentId;
		this.userId = userId;
		this.postReplied = postReplied;
	}
	
	/**
	 * @return the ID of the Comment
	 */
	@Override
	public String getId() {
		return this.commentId;
	}
	/**
	 * @return the timestamp of the Comment 
	 */
	@Override
	public LocalDateTime getTs() {
		return this.timeStamp;
	}
	/**
	 * Useful for a response when we want to change the ID of the comment replied by the ID of the post
	 * @param newId : the ID that we want to replace the actual ID with
	 * 
	 */
	public void changeId(String newId) {
		this.postReplied = newId;
	}
	
	/**
	 * 
	 * @return the Id directly linked to this comment (It can be a post_id or comment_id depending on if it is a comment or if it is a response to a comment )
	 */
	public String getPostRepliedId() {
		return this.postReplied;
	}
	
	/**
	 * 
	 * @return the ID of the user who posted this comment 
	 */
	public String getUserId() {
		return this.userId;
	}



}

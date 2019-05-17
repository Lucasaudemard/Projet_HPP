package hpp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;

/**
 * 
 * @author Florian
 *
 */
public class Post implements Event {
	private LocalDateTime timeStamp;
	private String postId;
	private String userName;
	private int internScore;
	private int externScore;
	private int commenter;
	LinkedHashSet<String> commentHashSet = new LinkedHashSet<String>();
	private LocalDateTime timeStampLastCommentary;
	
	
	/**
	 * 
	 * @param timeStamp timestamp of the post
	 * @param commentId unique ID of the post
	 * @param userName name of the owner of the Post
	 */
	public Post(LocalDateTime timeStamp, String postId, String userName) {
		super();
		this.timeStamp = timeStamp;
		this.postId = postId;
		this.userName = userName;
		this.internScore = 10;
		this.externScore = 0;
		this.commenter = 0;
	}

	/**
	 * @return the unique ID of the post 
	 */
	@Override
	public String getId() {
		return this.postId;
	}
	
	/**
	 * @return the timestamp of the post
	 */
	@Override
	public LocalDateTime getTs() {
		return this.timeStamp;
	}

	/**
	 * decreases by one the internScore of the Post 
	 */
	public void decreaseInternScore() {
		this.internScore--;
	}
	
	/**
	 * Adds 10 to the extern score of the Post and increases by one the commenter number if the commenter hasn't already commented
	 * @param userId the Id of the user that posted the comment
	 */
	public void addNewComment(String userId,LocalDateTime timeStampComment) {
		//TODO functionnality to add the commenter_id to the hashmap
		this.externScore+=10;
		if (this.commentHashSet.add(userId)) this.commenter++;
		this.timeStampLastCommentary= timeStampComment;
	}
	/**
	 * decreases by 1 the extern score of the post 
	 */
	public void decreaseExternScore() {
		this.externScore--;
		
	}
	/**
	 * 
	 * @return the total (intern+extern) score of the post
	 */
	public int getScore() {
		return this.internScore+this.externScore;
	}
	/**
	 * 
	 * @return the name of the user that posted this comment 
	 */
	public String getUserName() {
		return this.userName;
	}
	/**
	 * 
	 * @return the number of distinct peoples that have commented the post
	 */
	public int getCommenters() {
		return this.commenter;
	}
	
	public LocalDateTime getLastCommentTimeStamp() {
		return this.timeStampLastCommentary;
	}

}

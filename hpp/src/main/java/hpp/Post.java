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
	}

	@Override
	public void getId() {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeId() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTs() {
		// TODO Auto-generated method stub

	}

}

package hpp;

import java.time.LocalDateTime;

public class Comment implements Event {
	private LocalDateTime timeStamp;
	private String commentId;
	private String userName;
	private String commentReplied;
	private int internScore;
	
	
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeId(String newId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocalDateTime getTs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decreaseInternScore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewComment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decreaseExternScore() {
		// TODO Auto-generated method stub
		
	}

	

}

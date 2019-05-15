package hpp;

import java.time.LocalDateTime;

public interface Event {
	String getId();
	void changeId(String newId);
	LocalDateTime getTs();
	void decreaseInternScore();
	void addNewComment();
	void decreaseExternScore();
	int getScore();
	String getUserName();
	
	//TODO Add other usefull methods
}

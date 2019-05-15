package hpp;

import java.time.LocalDateTime;

public interface Event {
<<<<<<< Upstream, based on origin/test_flo
	String getId();
	void changeId(String newId);
	LocalDateTime getTs();
	void decreaseInternScore();
	void addNewComment();
	void decreaseExternScore();
=======
	void getId();
	void changeId();
	void getTs();
>>>>>>> d1f793e Ordonnanceur
	
	//TODO Add other usefull methods
}

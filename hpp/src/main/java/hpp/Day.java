package hpp;

import java.time.LocalDateTime;

public class Day {
	
	public LocalDateTime minTS; //ts minimum des posts et comments dans le Day
	public LocalDateTime currentTime;
	//public List<event> //liste des events correspondant aux posts et aux comments
	
	public Day(LocalDateTime currentTime, int nbHours) {
		//this.minTS = ;//le plus petit ts de la liste des events
		this.currentTime = currentTime;
		
	}
	
	public void compareTS() {
		
	}
	

}

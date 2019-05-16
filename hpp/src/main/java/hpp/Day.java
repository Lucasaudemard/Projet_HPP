package hpp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Day {
	
	public LocalDateTime currentTime;
	public List<Event> listEvent; //liste des events correspondant aux posts et aux comments
	private int nbHour; //
	
	public Day(LocalDateTime currenttime, int nbHours, List<Event> listEvents) {
		this.currentTime = currenttime;
		this.listEvent = listEvents;
		this.nbHour = nbHours;
	}
	
	public List<Event> compareTS() {
		List<Event> modifiedEvents = new LinkedList<Event>(); 
		int i = 0;
		LocalDateTime minTS = this.listEvent.get(i).getTs();; //le plus petit ts de la liste d'Events (soit le premier de la liste)
		long diffHour = Duration.between(minTS, this.currentTime).toHours(); //calcul la différence en heure entre le currentTime et le minTS 

		while(diffHour>this.nbHour && i<listEvent.size()) {
			modifiedEvents.add(this.listEvent.get(i));
			i++;
			minTS = this.listEvent.get(i).getTs(); 
			diffHour = Duration.between(minTS, this.currentTime).toHours();
		}
		return modifiedEvents;
	}
	
	public void addEvent(Event e){
		this.listEvent.add(e);
	}

}

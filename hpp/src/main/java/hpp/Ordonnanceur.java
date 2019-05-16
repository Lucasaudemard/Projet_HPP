package hpp;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Ordonnanceur implements Runnable {
	
	
	private HashMap<String, String> comToPost;
	private HashMap<String, Event> postToObjPost;
	
	private BlockingQueue<Event> produce1;
	private BlockingQueue<Event> produce2;
	
	public LocalDateTime ts;
	
	private Event currentObj;
	
	public List<Day> dayList;
	
	
	public Ordonnanceur(BlockingQueue<Event> p1, BlockingQueue<Event> p2 ) {
		
		
		this.comToPost = new HashMap<>();
		this.postToObjPost = new HashMap<>();
		
		this.produce1 = p1;
		this.produce2 = p2;
		
		this.dayList = new LinkedList<Day>(); //déclaration de la liste de Day
		
		for (int i=1; i<12; i++) {
			this.dayList.add(new Day(this.ts, 24*i, new LinkedList<Event>())); //ajout des 11 objets Day
		}
		
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		// TODO add a wait when the blocking queue is full 
	}
	
	public void chooseToQueue(BlockingQueue<Event> p1, BlockingQueue<Event> p2) throws InterruptedException {
		
		Event obj1 = p1.peek(); //post
		Event obj2 = p2.peek(); //comment
		
		
		if(obj1.getTs().isBefore(obj2.getTs())) {
			
			this.currentObj = p1.take();
			
		}
		else {
			
			this.currentObj = p2.take();
		}
		
		dayList.get(0).addEvent(this.currentObj); // ajout du currentObj dans le Day 1 (soit le premier élément de dayList)
		//TODO
		// vérifier si c'est une réponse + changer le postiD associé si oui
		// si c'est un comment (ou une réponse) faire appel à addNewComment
		
	}
	
	
	
	public void hashId() {
		
		
		String currentId = this.currentObj.getId();
		
		if(this.currentObj.getClass().getName() == "hpp.Post") {
			
			this.postToObjPost.put(currentId, this.currentObj);
			
		}
		else {
			
			if(this.currentObj.getPostReplied().getClass().getName() == "hpp.Post") {
				
				this.comToPost.put(currentId, this.currentObj.getPostReplied());
					
			}
			else {
					
				this.comToPost.put(currentId, comToPost.get(this.currentObj.getPostReplied()));
					
			}
		}		
	}
		
	public void incrementScores() {
		
		for (int i=10; i>=0; i--) {
			
			List<Event> modifiedEvents = new LinkedList<Event>();
			modifiedEvents = this.dayList.get(i).compareTS();
			
			// du Day10 au Day1
			for (int j=0; j<modifiedEvents.size();j++) {
				
				// si c'est un post et que son score interne n'est pas égal à 0 :
				if (modifiedEvents.get(j) instanceof Post) {
					
					if (((Post) modifiedEvents.get(j)).getScore()>0) {
						
						((Post) modifiedEvents.get(j)).decreaseInternScore();

					}
					
				}
				
				// si c'est un comment :
				else {
					String postID = ((Comment) modifiedEvents.get(j)).getPostRepliedId(); // récupération du postID associé au comment
					// on vérifie si le post est toujours dans la hashmap (si il est tjrs vivant)
					
					if (postToObjPost.containsKey(postID)) {
						Post p = (Post) postToObjPost.get(postID);// récupération de l'objet post ayant ce postID
						
						p.decreaseExternScore(); // décremenetation de l'extern score du post de 1
						
						if (i==10) { // pour les comments le score intern au comment est égal à 10-nombre de jours
							modifiedEvents.remove(j); //on supprime l'Event de modifiedEvents si c'est un comment vieux de 10 jours
						}
					}
					else {
						modifiedEvents.remove(j); //on supprime l'Event de modifiedEvents si le post associé n'existe plus
						// supprimer le comment de la hashmap !!!!
					}
				}
				
				dayList.get(i+1).addEvents(modifiedEvents); //déplacement des Events dans le Day suivant
			}
			
			// Day11
			
		}
	}
		

}

package hpp;

import java.util.concurrent.BlockingQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Ordonnanceur implements Runnable {

	private HashMap<String, String> comToPost;
	private HashMap<String, Event> postToObjPost;

	private BlockingQueue<Event> produce1;
	private BlockingQueue<Event> produce2;

	public LocalDateTime ts;

	private Event currentObj;

	public List<Day> dayList;
<<<<<<< Upstream, based on origin/master

	public List<String> top3;

	private String outputPath;

	public Ordonnanceur(BlockingQueue<Event> p1, BlockingQueue<Event> p2,String outPutPath) {

=======
	
	public List<String> top3;
	
	
	
	
	public Ordonnanceur(BlockingQueue<Event> p1, BlockingQueue<Event> p2 ) {
		
		
>>>>>>> 1c5893d Méthode updateTop3 pour ordonnanceur
		this.comToPost = new HashMap<>();
		this.postToObjPost = new HashMap<>();

		this.produce1 = p1;
		this.produce2 = p2;

		File f = new File(this.outputPath);
		f.delete();
		
<<<<<<< Upstream, based on origin/master
		this.dayList = new LinkedList<Day>(); // déclaration de la liste de Day

		for (int i = 1; i < 12; i++) {
			this.dayList.add(new Day(this.ts, 24 * i, new LinkedList<Event>())); // ajout des 11 objets Day
=======
		//Initialise la liste des scores avec des "" (On va comparer avec les post_id)
		this.top3 = new ArrayList<String>();
		for(int j = 0; j < 3; j++) {
			this.top3.add("");
		}
		
		
		
		this.dayList = new LinkedList<Day>(); //déclaration de la liste de Day
		
		for (int i=1; i<12; i++) {
			this.dayList.add(new Day(this.ts, 24*i, new LinkedList<Event>())); //ajout des 11 objets Day
>>>>>>> 1c5893d Méthode updateTop3 pour ordonnanceur
		}

	}

	public void run() {

		try {
			this.chooseToQueue();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.hashId();

		this.decrementScores();
	}
<<<<<<< Upstream, based on origin/master

	public void chooseToQueue() throws InterruptedException {

=======
	
	
	
	public void chooseToQueue() throws InterruptedException {
		
>>>>>>> 1c5893d Méthode updateTop3 pour ordonnanceur
		Event obj1 = this.produce1.peek();
		Event obj2 = this.produce2.peek();

		if (obj1.getTs().isBefore(obj2.getTs())) {

			this.currentObj = this.produce1.take();
			this.setTs(this.currentObj.getTs());

		} else {

			this.currentObj = this.produce2.take();
			this.setTs(this.currentObj.getTs());
		}

	}

	public void hashId() {

		String currentId = this.getCurrentObj().getId();

		// Si l'objet actuel est un Post
		if (this.getCurrentObj() instanceof Post) {

			this.getPostToPostObj().put(currentId, this.getCurrentObj()); // Lien entre l'ID du post et de l'objet Event
																			// associé dans une HashMap
			this.dayList.get(0).addEvent(this.getCurrentObj()); // ajout du currentObj dans le Day 1 (soit le premier
																// élément de dayList)

		} else if (this.getCurrentObj() instanceof Comment) { // Si l'objet actuel est un Comment

			if (this.getPostToPostObj().containsKey(((Comment) this.getCurrentObj()).getPostRepliedId())) { // Si le
																											// Comment
																											// répond à
																											// un Post

				this.getComToPost().put(currentId, ((Comment) this.getCurrentObj()).getPostRepliedId()); // Lien entre
																											// l'ID du
																											// Comment
																											// et l'ID
																											// du Post
																											// associé

				this.dayList.get(0).addEvent(this.getCurrentObj()); // ajout du currentObj dans le Day 1 (soit le
																	// premier élément de dayList)

			} else if (this.getComToPost().containsKey(((Comment) this.getCurrentObj()).getPostRepliedId())) { // Si le
																												// Comment
																												// répond
																												// à un
																												// Comment

				this.getComToPost().put(currentId,
						this.getComToPost().get(((Comment) this.getCurrentObj()).getPostRepliedId())); // Lien entre
																										// l'ID de la
																										// réponse et
																										// l'ID du Post
																										// associé
				((Comment) this.getCurrentObj())
						.changeId(this.getComToPost().get(((Comment) this.getCurrentObj()).getPostRepliedId()));
				this.dayList.get(0).addEvent(this.getCurrentObj()); // ajout du currentObj dans le Day 1 (soit le
																	// premier élément de dayList)
			}
			String userID = ((Comment) this.getCurrentObj()).getUserId();
			String postID = ((Comment) this.getCurrentObj()).getPostRepliedId();
			Post p = (Post) postToObjPost.get(postID);
			p.addNewComment(userID);
		}
	}

	public void decrementScores() {

		for (int i = 9; i >= 0; i--) {

			List<Event> modifiedEvents = new LinkedList<Event>();
			modifiedEvents = this.dayList.get(i).compareTS();

			// du Day10 au Day1
			for (int j = 0; j < modifiedEvents.size(); j++) {

				// si c'est un post et que son score interne n'est pas égal à 0 :
				if (modifiedEvents.get(j) instanceof Post) {

					if (((Post) modifiedEvents.get(j)).getScore() > 0) {

						((Post) modifiedEvents.get(j)).decreaseInternScore();

					}

				}

				// si c'est un comment :
				else {
					String postID = ((Comment) modifiedEvents.get(j)).getPostRepliedId(); // récupération du postID
																							// associé au comment
					// on vérifie si le post est toujours dans la hashmap (si il est tjrs vivant)

					if (postToObjPost.containsKey(postID)) {
						Post p = (Post) postToObjPost.get(postID);// récupération de l'objet post ayant ce postID

						p.decreaseExternScore(); // décremenetation de l'extern score du post de 1

						if (i == 9) { // pour les comments le score intern au comment est égal à 10-nombre de jours

							// suppression du comment dans la hashtable
							String commentIDtoDelete = modifiedEvents.get(j).getId();
							this.getComToPost().remove(commentIDtoDelete);

							modifiedEvents.remove(j); // on supprime l'Event de modifiedEvents si c'est un comment vieux
														// de 10 jours
						}
					} else {
						modifiedEvents.remove(j); // on supprime l'Event de modifiedEvents si le post associé n'existe
													// plus
					}
				}

				dayList.get(i + 1).addEvents(modifiedEvents); // ajout des Events dans le Day suivant
				dayList.get(i).removeEvents(modifiedEvents); // suppression des Events qui changent de jour
			}

			// Day11
			List<Event> listEventsDay11 = dayList.get(10).listEvent;
			for (int k = 0; k < listEventsDay11.size(); k++) {
				if (((Post) listEventsDay11.get(k)).getScore() == 0) {

					String postIDtoDelete = listEventsDay11.get(k).getId();
					Post postToDelete = (Post) postToObjPost.get(postIDtoDelete);
					postToDelete = null; // suppression de l'objet Post mort
					this.getPostToPostObj().remove(postIDtoDelete); // suppresion dans la hashtable
				}
			}

		}
	}
<<<<<<< Upstream, based on origin/master

=======
	
	
	/**
	 * tempTop3[0] = top 1
	 * tempTop3[1] = top 2
	 * tempTop3[2] = top 3
	 */
>>>>>>> 1c5893d Méthode updateTop3 pour ordonnanceur
	public void updateTop3() {
<<<<<<< Upstream, based on origin/master

	}

	public void write() {
		String fileContent = "";
		for (int i = 0; i < 3; i++) {
			String s = this.top3.get(i);
			
			if (s == "") {
				fileContent += ",-,-,-";
			} else {
				Post p = (Post) postToObjPost.get(s);
				if (i == 0)
					fileContent = this.ts.toString() + "," + p.getId() + "," + p.getUserName() + "," + p.getScore()
							+ "," + p.getCommenters();
				else
					fileContent = ","+p.getId() + "," + p.getUserName() + "," + p.getScore() + "," + p.getCommenters();
			}
		}

		FileWriter fw;
		try {
			fw = new FileWriter(this.outputPath, true);
			fw.write(fileContent);
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

=======
		
	
		String temp;
		int tempScore = 0;
		boolean isChanged = false;
		
		List<String> tempTop3 = new ArrayList<String>();
		
		
		
		for(Map.Entry<String, Event> mapEntry : this.getPostToPostObj().entrySet()) {
			
			
				if(((Post) mapEntry.getValue()).getScore() > tempScore) {
					
					tempScore = ((Post) mapEntry.getValue()).getScore();
					
					if(tempTop3.size() == 0) {
						
						tempTop3.add(0, (mapEntry.getValue().getId()));
						tempTop3.add(1, "");
						tempTop3.add(2, "");
					}
					else if(tempTop3.size() == 1){
						
						if(tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore()) {
							
							if(((Post) mapEntry.getValue()).getTs().isBefore(((Post) this.getPostToPostObj().get(tempTop3.get(0))).getTs())) {
								
								temp = tempTop3.get(0);
								tempTop3.add(0, (mapEntry.getValue().getId()));
								tempTop3.add(0, temp);
							}
							
							tempTop3.add(1, (mapEntry.getValue().getId()));
							tempTop3.add(2, "");
						}
						else {
							
							temp = tempTop3.get(0);
							tempTop3.add(0, mapEntry.getValue().getId());
							tempTop3.add(1, temp);
							
							tempTop3.add(2, "");
						}
					}
					else if(tempTop3.size() == 2) {
						
						if(tempScore <= ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore <= ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() ) {
							
							tempTop3.add(2, (mapEntry.getValue().getId()));
						}
						else if(tempScore <= ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore()) {
							
							temp = tempTop3.get(1);
							tempTop3.add(1, mapEntry.getValue().getId());
							tempTop3.add(2, temp);
						}
						else if(tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore()) {
							
							temp = tempTop3.get(1);
							tempTop3.add(2, temp);
							temp = tempTop3.get(0);
							tempTop3.add(1, temp);
							tempTop3.add(0, mapEntry.getValue().getId());
							
						}
						
					}
					else if(tempTop3.size() == 3) {
						
						if(tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							temp = tempTop3.get(1);
							tempTop3.add(2, temp);
							temp = tempTop3.get(0);
							tempTop3.add(1, temp);
							tempTop3.add(0, mapEntry.getValue().getId());
						}
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							tempTop3.add(2, mapEntry.getValue().getId());
						}
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							temp = tempTop3.get(1);
							tempTop3.add(2, temp);
							tempTop3.add(1, mapEntry.getValue().getId());
						}
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							temp = tempTop3.get(1);
							tempTop3.add(2, temp);
							temp = tempTop3.get(0);
							tempTop3.add(1, temp);
							tempTop3.add(0, mapEntry.getValue().getId());
						}
					}
					
				}
		}
		
		for(int i = 0; i < tempTop3.size(); i++) {
			
			if(this.top3.get(i) != tempTop3.get(i)) {
				this.top3.add(i, tempTop3.get(i));
				
				isChanged = true;
			}
		}
		
		
>>>>>>> 1c5893d Méthode updateTop3 pour ordonnanceur
	}

//-------------------------GETTER AND SETTER---------------------------

	public HashMap<String, Event> getPostToPostObj() {
		return this.postToObjPost;
	}

	public HashMap<String, String> getComToPost() {
		return this.comToPost;
	}

	public Event getCurrentObj() {
		return this.currentObj;
	}

	public void setTs(LocalDateTime tsP) {
		this.ts = tsP;
	}

}

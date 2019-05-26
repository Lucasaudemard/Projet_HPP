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

	public List<String> top3;

	private String outputPath;
	
	public boolean bq1Ended=false, bq2Ended=false;
	
	public int nbSup=0;
	//public List<String> listpostdelete;
	public int nbdans11=0;
	

	public Ordonnanceur(BlockingQueue<Event> p1, BlockingQueue<Event> p2,String outPutPath) {

		this.comToPost = new HashMap<>();
		this.postToObjPost = new HashMap<>();

		this.produce1 = p1;
		this.produce2 = p2;

		File f = new File(this.outputPath);
		f.delete();
		
		this.dayList = new LinkedList<Day>(); // déclaration de la liste de Day

		for (int i = 1; i < 12; i++) {
			this.dayList.add(new Day(this.ts, 24 * i, new LinkedList<Event>())); // ajout des 11 objets Day
		}
			
		//Initialise la liste des scores 
		this.top3 = new ArrayList<String>();
	
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

	public void chooseToQueue() throws InterruptedException {

		Event obj1 = this.produce1.peek();
		Event obj2 = this.produce2.peek();
		
		if(obj1.getId()=="PoisonPill" ) {
			this.currentObj = this.produce2.take();
			this.setTs(this.currentObj.getTs());
			this.bq1Ended=true;
		}
		
		else if(obj2.getId()=="PoisonPill" ) {
			this.currentObj = this.produce1.take();
			this.setTs(this.currentObj.getTs());
			this.bq2Ended=true;
		}
		
		else if (obj1.getId()!="PoisonPill" && obj2.getId()!="PoisonPill")
		{
			if(obj1.getTs().isBefore(obj2.getTs())) {
				
				this.currentObj = this.produce1.take();
				this.setTs(this.currentObj.getTs());
				
			}
			else if (obj2.getTs().isBefore(obj1.getTs())) {
				
				this.currentObj = this.produce2.take();
				this.setTs(this.currentObj.getTs());
			}
		}
		
		for (int i=0; i<10; i++) {
			this.dayList.get(i).updateCurrentTime(this.ts);
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
			p.addNewComment(userID, ts);
		}
	}
	

	public void decrementScores() {
for (int i=9; i>=0; i--) {
			if (!this.dayList.get(i).listEvent.isEmpty()) {
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
						
							if (i==9) { // pour les comments le score intern au comment est égal à 10-nombre de jours
							
							// suppression du comment dans la hashtable
								String commentIDtoDelete = modifiedEvents.get(j).getId();
								this.getComToPost().remove(commentIDtoDelete);
						
								modifiedEvents.remove(j); //on supprime l'Event de modifiedEvents si c'est un comment vieux de 10 jours
							}
						}
						else {
						modifiedEvents.remove(j); //on supprime l'Event de modifiedEvents si le post associé n'existe plus
						}
					}
				}
				dayList.get(i+1).addEvents(modifiedEvents); //ajout des Events dans le Day suivant
				dayList.get(i).removeEvents(modifiedEvents); //suppression des Events qui changent de jour
 			}
						
		}
		// Day11

					if (!this.dayList.get(10).listEvent.isEmpty()) {
						List<Event> listEventsDay11 =dayList.get(10).listEvent;
						
						for (int k=0;k<listEventsDay11.size();k++) {

							if (((Post) listEventsDay11.get(k)).getScore()==0) {
								
								String postIDtoDelete = listEventsDay11.get(k).getId();
								
								listEventsDay11.remove(k);

								Post postToDelete = (Post) postToObjPost.get(postIDtoDelete);
								postToDelete = null; // suppression de l'objet Post mort
								this.getPostToPostObj().remove(postIDtoDelete); //suppresion dans la hashtable
							}
							
						}

					}
					

	}

	public void updateTop3() {
		
		String temp; //Permet de changer de places les ID dans le tableau
		int tempScore = 0; //Valeur qui va récupérer la valeur max du score dans la HashMap à chaque update
		boolean isChanged = false; //Permet de dire à la fin si on modifie le tableau du top 3
		
		List<String> tempTop3 = this.top3; //Liste temporaire qui sert à comparer avec la liste Top 3
		
		
		
		for(Map.Entry<String, Event> mapEntry : this.getPostToPostObj().entrySet()) { //On parcourt la HasMap 
			
			
				if(((Post) mapEntry.getValue()).getScore() > tempScore) { //On actualise la valeur max
					
					tempScore = ((Post) mapEntry.getValue()).getScore();
					
					if(tempTop3.size() == 0) { //Si la liste est vide, on ajoute l'élément
						
						tempTop3.add(0, (mapEntry.getValue().getId()));
						tempTop3.add(1, "");
						tempTop3.add(2, "");
					}
					else if(tempTop3.size() == 1){ //Si la liste possède un élément
						
						if(tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore()) { //On compare si les scores sont les mêmes
							
							if(((Post) mapEntry.getValue()).getTs().isAfter(((Post) this.getPostToPostObj().get(tempTop3.get(0))).getTs())) { //On regarde qui est le plus récent, si oui, on change de place avec l'élément actuel et on décale le premier en 2eme position
								
								temp = tempTop3.get(0);
								tempTop3.add(0, (mapEntry.getValue().getId()));
								tempTop3.add(1, temp);
								
							}
							else { //Sinon on le met en 2eme position
								
								tempTop3.add(1, (mapEntry.getValue().getId()) );
								tempTop3.add(2, "");
								
							}
							
						}
						else if(tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore()){ //Si le score est supérieur, on change de place avec le 1er actuel
							
							temp = tempTop3.get(0);
							tempTop3.add(0, (mapEntry.getValue().getId()));
							tempTop3.add(1, temp);
						}
						else { //Sinon on le met en 2eme position
							
							tempTop3.add(1, (mapEntry.getValue().getId()) );
							tempTop3.add(2, "");
							
						}
					}
					else if(tempTop3.size() == 2) { //Si la liste contient 2 élément 
						
						//On effectue les mêmes tests mais pour tous les cas possibles en prenant en compte les scores égaux
						
						//Score égal au 1er
						if(tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() ) {
							
							//Ts plus récent que le 1er élément
							if(((Post) mapEntry.getValue()).getTs().isAfter(((Post) this.getPostToPostObj().get(tempTop3.get(0))).getTs())) {
								
								temp = tempTop3.get(1);
								tempTop3.add(2, temp);
								
								temp = tempTop3.get(0);
								tempTop3.add(0, (mapEntry.getValue().getId()));
								tempTop3.add(1, temp);
								
							}
							//Ts plus ancien que le 1er élément
							else {
								
								temp = tempTop3.get(1);
								tempTop3.add(1, (mapEntry.getValue().getId()));
								tempTop3.add(2, temp);
								
							}
						}
						//Score égal au 2eme
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore()) {
							
							//Ts plus récent que le 2eme élément
							if(((Post) mapEntry.getValue()).getTs().isAfter(((Post) this.getPostToPostObj().get(tempTop3.get(1))).getTs())) {
								
								temp = tempTop3.get(1);
								tempTop3.add(1, (mapEntry.getValue().getId()));
								tempTop3.add(2, temp);
								
							}
							//Ts plus ancien que le 2eme élément
							else {
								
								tempTop3.add(2, (mapEntry.getValue().getId()));
								
							}

						}
						//Score supérieur aux 2
						else if(tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore()) {
							
							temp = tempTop3.get(1);
							tempTop3.add(2, temp);
							temp = tempTop3.get(0);
							tempTop3.add(1, temp);
							tempTop3.add(0, mapEntry.getValue().getId());
							
						}
						//Score inférieur aux 2
						else {
							
							tempTop3.add(2, (mapEntry.getValue().getId()));
							
						}
						
					}
					else if(tempTop3.size() == 3) {//Si la liste possède 3 éléments (remplie)
						
						//Score supérieur aux 3
						if(tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							temp = tempTop3.get(1);
							tempTop3.add(2, temp);
							temp = tempTop3.get(0);
							tempTop3.add(1, temp);
							tempTop3.add(0, mapEntry.getValue().getId());
							
						}
						//Score supérieur au dernier élément
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
								
							tempTop3.add(2, mapEntry.getValue().getId());

						}
						//Score supérieur au 2ème élément
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
								
								temp = tempTop3.get(1);
								tempTop3.add(2, temp);
								tempTop3.add(1, mapEntry.getValue().getId());

						}
						//Score égal au 1er élément
						else if(tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							//Ts plus récent que le 1er élément
							if(((Post) mapEntry.getValue()).getTs().isAfter(((Post) this.getPostToPostObj().get(tempTop3.get(0))).getTs())) {
								
								temp = tempTop3.get(1);
								tempTop3.add(2, temp);
								temp = tempTop3.get(0);
								tempTop3.add(1, temp);
								tempTop3.add(0, mapEntry.getValue().getId());
								
							}
							//Ts plus ancien au 1er élément
							else {
								
								temp = tempTop3.get(1);
								tempTop3.add(2, temp);
								tempTop3.add(1, mapEntry.getValue().getId());
								
							}
							
						}
						//Score égal au 2eme élément
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore > ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							//Ts plus récent que le 2eme élément
							if(((Post) mapEntry.getValue()).getTs().isAfter(((Post) this.getPostToPostObj().get(tempTop3.get(1))).getTs())) {
								
								temp = tempTop3.get(1);
								tempTop3.add(2, temp);
								tempTop3.add(1, mapEntry.getValue().getId());
								
							}
							//Ts plus ancien que le 2eme élément
							else {
								
								tempTop3.add(2, mapEntry.getValue().getId());
								
							}

						}
						//Score egal au 3eme element
						else if(tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(0))).getScore() && tempScore < ((Post) this.getPostToPostObj().get(tempTop3.get(1))).getScore() && tempScore == ((Post) this.getPostToPostObj().get(tempTop3.get(2))).getScore()) {
							
							//Ts plus récent que le 3eme élément
							if(((Post) mapEntry.getValue()).getTs().isAfter(((Post) this.getPostToPostObj().get(tempTop3.get(2))).getTs())) {
								
								tempTop3.add(2, mapEntry.getValue().getId());
								
							}
							//Ts plus ancien que le 3eme élément et dans ce cas on ne fait rien
							
						}

					
					}
				}
		}
		
		//Une fois notre liste construite et mise à jour, on la compare avec la liste actuelle de top 3, en regardant si il y a une différence
		for(int i = 0; i < this.top3.size(); i++) {
			
			if(this.top3.get(i) != tempTop3.get(i)) {
				this.top3.add(i, tempTop3.get(i));
				
				isChanged = true; //Si il y a un changement, on le notifie
			}
		}
		
		if(isChanged) {
			this.write(); //On écrit dans le .dat et on actualise
		}
		
		
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

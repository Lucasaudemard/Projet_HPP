package hpp;

import java.util.concurrent.BlockingQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
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

	public String[] top3 = { "", "", "" };

	private String outputPath;

	boolean stop = false;

	public Ordonnanceur(BlockingQueue<Event> p1, BlockingQueue<Event> p2, String outPutPath) {

		this.comToPost = new HashMap<>();
		this.postToObjPost = new HashMap<>();

		this.produce1 = p1;
		this.produce2 = p2;
		this.outputPath = outPutPath;
		File f = new File(this.outputPath);
		f.delete();

		this.dayList = new LinkedList<Day>(); // déclaration de la liste de Day

		for (int i = 1; i < 12; i++) {
			this.dayList.add(new Day(this.ts, 24 * i, new LinkedList<Event>())); // ajout des 11 objets Day
		}

	}

	public void run() {
		while (!this.stop) {
			if (!this.produce1.isEmpty() && !this.produce2.isEmpty()) {
				try {
					this.chooseToQueue();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!this.stop) {
					this.hashId();
					this.decrementScores();
					this.updateTop3();
				}
			}
		}
		System.out.println("Ord : finished");

	}

	public void chooseToQueue() throws InterruptedException {

		Event obj1 = this.produce1.peek();
		Event obj2 = this.produce2.peek();

		if (obj1.getId() == "PoisonPill" && obj2.getId() != "PoisonPill") {
			this.currentObj = this.produce2.take();
			this.setTs(this.currentObj.getTs());
		}

		else if (obj2.getId() == "PoisonPill" && obj1.getId() != "PoisonPill") {
			this.currentObj = this.produce1.take();
			this.setTs(this.currentObj.getTs());
		}

		else if (obj1.getId() != "PoisonPill" && obj2.getId() != "PoisonPill") {
			if (obj1.getTs().isBefore(obj2.getTs())) {

				this.currentObj = this.produce1.take();
				this.setTs(this.currentObj.getTs());

			} else if (obj2.getTs().isBefore(obj1.getTs())) {
				this.currentObj = this.produce2.take();
				this.setTs(this.currentObj.getTs());
			}
		} else
			this.stop = true;

		if (!this.stop) {
			for (int i = 0; i < 10; i++) {
				this.dayList.get(i).updateCurrentTime(this.ts);
			}
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
				if (this.postToObjPost.containsKey(((Comment) this.getCurrentObj()).getPostRepliedId())) { // un Post

					this.getComToPost().put(currentId, ((Comment) this.getCurrentObj()).getPostRepliedId()); // Lien
																												// entre
																												// l'ID
																												// du
																												// Comment
																												// et
																												// l'ID
																												// du
																												// Post
																												// associé

					this.dayList.get(0).addEvent(this.getCurrentObj()); // ajout du currentObj dans le Day 1 (soit le
																		// premier élément de dayList)
					String userID = ((Comment) this.getCurrentObj()).getUserId();
					String postID = ((Comment) this.getCurrentObj()).getPostRepliedId();
					Post p = (Post) postToObjPost.get(postID);
					p.addNewComment(userID, this.ts);
				}

			} else if (this.getComToPost().containsKey(((Comment) this.getCurrentObj()).getPostRepliedId())) { // Si le
																												// Comment
																												// répond
																												// à un
																												// Comment
				if (this.postToObjPost
						.containsKey(this.getComToPost().get(((Comment) this.getCurrentObj()).getPostRepliedId()))) {
					this.getComToPost().put(currentId,
							this.getComToPost().get(((Comment) this.getCurrentObj()).getPostRepliedId())); // Lien entre
																											// l'ID de
																											// la
																											// réponse
																											// et
																											// l'ID du
																											// Post
																											// associé
					((Comment) this.getCurrentObj())
							.changeId(this.getComToPost().get(((Comment) this.getCurrentObj()).getPostRepliedId()));
					this.dayList.get(0).addEvent(this.getCurrentObj()); // ajout du currentObj dans le Day 1 (soit le

					String userID = ((Comment) this.getCurrentObj()).getUserId();
					String postID = ((Comment) this.getCurrentObj()).getPostRepliedId();
					Post p = (Post) postToObjPost.get(postID);
					p.addNewComment(userID, this.ts);

				} // premier élément de dayList)
			}

		}
	}

	public void decrementScores() {
		for (int i = 9; i >= 0; i--) {
			if (!this.dayList.get(i).listEvent.isEmpty()) {
				List<Event> modifiedEvents = new LinkedList<Event>();
				modifiedEvents = this.dayList.get(i).compareTS();
				List<Event> temp = new LinkedList<Event>();

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
								temp.add(modifiedEvents.get(j));
							}
						} else {
							modifiedEvents.remove(j);
							temp.add(modifiedEvents.get(j));// on supprime l'Event de modifiedEvents si le post associé
															// n'existe plus
						}
					}
				}
				dayList.get(i).removeEvents(modifiedEvents);
				modifiedEvents.removeAll(temp);
				dayList.get(i + 1).addEvents(modifiedEvents); // ajout des Events dans le Day suivant
				// suppression des Events qui changent de jour
			}

		}
		List<Event> temp1 = new LinkedList<Event>();

		// Day11

		if (!this.dayList.get(10).listEvent.isEmpty()) {
			List<Event> listEventsDay11 = dayList.get(10).listEvent;

			for (int k = 0; k < listEventsDay11.size(); k++) {

				if (((Post) listEventsDay11.get(k)).getScore() == 0) {

					String postIDtoDelete = listEventsDay11.get(k).getId();

					temp1.add(listEventsDay11.get(k));

					Post postToDelete = (Post) postToObjPost.get(postIDtoDelete);
					this.getPostToPostObj().remove(postIDtoDelete);
					postToDelete = null; // suppression de l'objet Post mort
				}

			}
			listEventsDay11.removeAll(temp1);

		}

	}

	public void updateTop3() {
		boolean hasChanged = false;
		String[] testtop3str = { "", "", "" };
		int[] testtop3 = { 0, 0, 0 };
		for (Map.Entry<String, Event> mapEntry : this.getPostToPostObj().entrySet()) { // On parcourt la HasMap
			Post currentPost = ((Post) mapEntry.getValue());
			int a = currentPost.getScore();
			if (a >= testtop3[0]) {
				if (a == testtop3[0]) {

					if (currentPost.getTs().isAfter(((Post) postToObjPost.get(testtop3str[0])).getTs())) {
						String tempS = testtop3str[1];
						Integer tempI = testtop3[1];
						testtop3str[1] = testtop3str[0];
						testtop3[1] = testtop3[0];
						testtop3str[2] = tempS;
						testtop3[2] = tempI;
						testtop3str[0] = currentPost.getId();
						testtop3[0] = currentPost.getScore();
					} else if (currentPost.getTs().isBefore(((Post) postToObjPost.get(testtop3str[0])).getTs())) {
						testtop3str[2] = testtop3str[1];
						testtop3[2] = testtop3[1];
						testtop3str[1] = currentPost.getId();
						testtop3[1] = currentPost.getScore();
					} else {
						if (currentPost.getLastCommentTimeStamp()
								.isAfter(((Post) postToObjPost.get(testtop3str[0])).getLastCommentTimeStamp())) {
							String tempS = testtop3str[1];
							Integer tempI = testtop3[1];
							testtop3str[1] = testtop3str[0];
							testtop3[1] = testtop3[0];
							testtop3str[2] = tempS;
							testtop3[2] = tempI;
							testtop3str[0] = currentPost.getId();
							testtop3[0] = currentPost.getScore();
						} else {
							testtop3str[2] = testtop3str[1];
							testtop3[2] = testtop3[1];
							testtop3str[1] = currentPost.getId();
							testtop3[1] = currentPost.getScore();
						}

					}
				} else {
					String tempS = testtop3str[1];
					Integer tempI = testtop3[1];
					testtop3str[1] = testtop3str[0];
					testtop3[1] = testtop3[0];
					testtop3str[2] = tempS;
					testtop3[2] = tempI;
					testtop3str[0] = currentPost.getId();
					testtop3[0] = currentPost.getScore();
				}
			} else if (a >= testtop3[1]) {
				if (a == testtop3[1]) {
					if (currentPost.getTs().isAfter(((Post) postToObjPost.get(testtop3str[1])).getTs())) {
						testtop3str[2] = testtop3str[1];
						testtop3[2] = testtop3[1];
						testtop3str[1] = currentPost.getId();
						testtop3[1] = currentPost.getScore();
					} else if (currentPost.getTs().isBefore(((Post) postToObjPost.get(testtop3str[1])).getTs())) {
						testtop3str[2] = currentPost.getId();
						testtop3[2] = currentPost.getScore();
					} else {
						if (currentPost.getLastCommentTimeStamp()
								.isAfter(((Post) postToObjPost.get(testtop3str[1])).getLastCommentTimeStamp())) {
							testtop3str[1] = testtop3str[2];
							testtop3[1] = testtop3[2];
							testtop3str[1] = currentPost.getId();
							testtop3[1] = currentPost.getScore();
						} else {
							testtop3str[2] = testtop3str[1];
							testtop3[2] = testtop3[1];
							testtop3str[1] = currentPost.getId();
							testtop3[1] = currentPost.getScore();
						}

					}

				} else {
					testtop3str[2] = testtop3str[1];
					testtop3[2] = testtop3[1];
					testtop3str[1] = currentPost.getId();
					testtop3[1] = currentPost.getScore();

				}
			} else if (a >= testtop3[2]) {
				if (a == testtop3[2]) {
					if (currentPost.getTs().isAfter(((Post) postToObjPost.get(testtop3str[2])).getTs())) {
						testtop3str[2] = currentPost.getId();
						testtop3[2] = currentPost.getScore();
					} else if (currentPost.getTs().isBefore(((Post) postToObjPost.get(testtop3str[2])).getTs())) {

					} else {
						if (currentPost.getLastCommentTimeStamp()
								.isAfter(((Post) postToObjPost.get(testtop3str[2])).getLastCommentTimeStamp())) {
							testtop3str[2] = currentPost.getId();
							testtop3[2] = currentPost.getScore();

						}
					}

				} else {
					testtop3str[2] = currentPost.getId();
					testtop3[2] = currentPost.getScore();

				}

			}
		}

		// Une fois notre liste construite et mise à jour, on la compare avec la liste
		// actuelle de top 3, en regardant si il y a une différence
		for (int i = 0; i <= 2; i++) {
			if (testtop3str[i] != this.top3[i]) {
				this.top3[i] = testtop3str[i];
				hasChanged = true;
			}
		}

		if (hasChanged) {
			this.write(); // On écrit dans le .dat et on actualise
		}

	}

	public void write() {
		String fileContent = "";
		for (int i = 0; i < 3; i++) {
			String s = this.top3[i];

			if (s == "") {
				fileContent += ",-,-,-";
			} else {
				Post p = (Post) postToObjPost.get(s);
				if (i == 0)
					fileContent += this.ts.toString() + "," + p.getId() + "," + p.getUserName() + "," + p.getScore()
							+ "," + p.getCommenters();
				else
					fileContent += "," + p.getId() + "," + p.getUserName() + "," + p.getScore() + ","
							+ p.getCommenters();
			}
		}
		fileContent += "\n";

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

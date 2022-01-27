package QuestPrerequisites;

import java.io.*;
import java.util.Scanner;

import lib280.base.CursorPosition280;
import lib280.exception.InvalidState280Exception;
import lib280.graph.Edge280;
import lib280.graph.GraphMatrixRep280;
import lib280.graph.Vertex280;
import lib280.list.LinkedList280;
import lib280.tree.ArrayedHeap280;

public class QuestProgression {
	
	// File format for quest data:
	// First line: Number of quests N
	// Next N lines consist of the following items, separated by commas:
	//     quest ID, quest name, quest area, quest XP
	//     (Quest ID's must be between 1 and N, but the line for each quest IDs may appear in any order).
	// Remaining lines consist of a comma separated pair of id's i and j where i and j are quest IDs indicating
	// that quest i must be done before quest j (i.e. that (i,j) is an edge in the quest graph).
	
	/**
	 * Read the quest data from a text file and build a graph of quest prerequisites.
	 * @param filename Filename from which to read quest data.
	 * @return A graph representing quest prerequisites.  If quest with id i must be done before a quest with id j, then there is an edge in the graph from vertex i to vertex j.
	 */
	public static GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> readQuestFile(String filename) {
		Scanner infile;
		
		// Attempt to open the input filename.
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Unable to open" + filename);
			e.printStackTrace();
			return null;
		}
		
		// Set the delimiters for parsing to commas, and vertical whitespace.
		infile.useDelimiter("[,\\v]");

		// Read the number of quests for which there is data.
		int numQuests = infile.nextInt();
		
		// read the quest data for each quest.
		LinkedList280<Quest> questList = new LinkedList280<Quest>();
		for(int i=0; i < numQuests; i++) {
			int qId = infile.nextInt();
			String qName = infile.next();
			String qArea = infile.next();
			int qXp = infile.nextInt();		
			questList.insertLast(new Quest(qId, qName, qArea, qXp));
		}
	
		// Make a graph with the vertices we created from the quest data.
		GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> questGraph = 
				new GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> (numQuests, true, "QuestPrerequisites.QuestVertex", "lib280.graph.Edge280");
		
		// Add enough vertices for all of our quests.
		questGraph.ensureVertices(numQuests);
		
		// Store each quest in a different vertex.  The quest with id i gets stored vertex i.
		questList.goFirst();
		while(questList.itemExists()) {
			questGraph.vertex(questList.item().id()).setQuest(questList.item());
			questList.goForth();
		}
		
		// Continue reading the input file for the quest prerequisite informaion and add an edge to the graph
		// for each prerequisite.
		while(infile.hasNext()) {
			questGraph.addEdge(infile.nextInt(), infile.nextInt());
		}
				
		infile.close();
		
		return questGraph;
	}
	

	/**
	 * Test whether vertex v has incoming edges or not
	 * @param G A graph.
	 * @param v The integer identifier of a node in G (corresponds to quest ID)
	 * @return Returns true if v has no incoming edges.  False otherwise.
	 */
	public static boolean hasNoIncomingEdges(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G, int v){

		// TODO Write this method

		// get node by looking up node in the graph with quest ID v


		for(int k=1; k<=G.numVertices(); k++){
			if(G.isAdjacent(G.vertex(k), G.vertex(v))){
				return false;
			}
		}
		return true;
	}


	
	
	/**
	 * Perform a topological sort of the quests in the quest prerequisite graph G, with priority given
	 * to the highest experience value among the available quests.
	 * @param G The graph on which to perform a topological sort.
	 * @return A list of quests that is the result of the topological sort, that is, the order in which the quests should be done if we always pick the available quest with the largest XP reward first.
	 */
	public static LinkedList280<Quest> questProgression(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G) {
	
		// TODO Write this method
		LinkedList280<Quest> L = new LinkedList280();
		ArrayedHeap280<Quest> H = new ArrayedHeap280(G.capacity());


		// insert quests without edges

		for(int k=1; k<=G.numVertices(); k++) {
			if(hasNoIncomingEdges(G, k)) {
				H.insert(G.vertex(k).quest);
			}
		}





					while(!H.isEmpty()){
						// remove quest from top of heap and insert to list

						L.insertLast(H.item());
						Quest savedRoot = H.item();
						H.deleteItem();

						G.goFirst();
						while(!G.after()) {

										// represents node n from the linked list

							for(int k=1; k<=G.numVertices(); k++){		// represents node m

								// check if there is an edge from n to m
								if(G.isAdjacent(G.vertex(savedRoot.id()), G.vertex(k))){

									G.eSearch(G.vertex(savedRoot.id()), G.vertex(k));

									if(G.eItemExists()){
										G.deleteEItem();
										if(hasNoIncomingEdges(G, k)){
											H.insert(G.vertex(k).quest());}
									}
								}
							}

							G.goForth();
						}


					}
					

					if(G.numEdges() != 0){
						throw new InvalidState280Exception("It has a cycle!");
					}else{
						return L;
					}
				}

	
	public static void main(String args[]) {
		// Read the quest data and construct the graph.

		System.out.println("Starts here");
		// If you get an error reading the file here and you're using Eclipse, 
		// remove the 'QuestPrerequisites-Template/' portion of the filename.
		GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> questGraph = readQuestFile("/Users/admin/Documents/CMPT 280/AS7/QuestPrerequisites-Template/quests16.txt");

		//System.out.println(questGraph);
		System.out.println("Before sorting");
		// Perform a topological sort on the graph.
		LinkedList280<Quest> questListForMaxXp = questProgression(questGraph);
		System.out.println("Middle here");
		// Display the quests to be completed in the order determined by the topologial sort.
		questListForMaxXp.goFirst();
		while(questListForMaxXp.itemExists()) {
			System.out.println(questListForMaxXp.item());
			questListForMaxXp.goForth();
		}
		System.out.println("Ends here");
				
	}
}
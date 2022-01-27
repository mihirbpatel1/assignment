import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.opencsv.CSVReader;

import lib280.base.Pair280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.hashtable.KeyedChainedHashTable280;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;
import lib280.tree.OrderedSimpleTree280;

// This project uses a JAR called opencsv which is a library for reading and
// writing CSV (comma-separated value) files.
// 
// You don't need to do this for this project, because it's already done, but
// if you want to use opencsv in other projects on your own, here's the process:
//
// 1. Download opencsv-3.1.jar from http://sourceforge.net/projects/opencsv/
// 2. Drag opencsv-3.1.jar into your project.
// 3. Right-click on the project in the package explorer, select "Properties" (at bottom of popup menu)
// 4. Choose the "Libraries" tab
// 5. Click "Add JARs"
// 6. Select the opencsv-3.1.jar from within your project from the list.
// 7. At the top of your .java file add the following imports:
//        import java.io.FileReader;
//        import com.opencsv.CSVReader;
//
// Reference documentation for opencsv is here:  
// http://opencsv.sourceforge.net/apidocs/overview-summary.html



public class QuestLog extends KeyedChainedHashTable280<String, QuestLogEntry> {


	//	/**	Array to store linked lists for separate chaining. */
//	protected LinkedList280<I>[] hashArray;
//
//	/**	Position of the current item in its list. */
//	protected LinkedIterator280<I> itemListLocation;
//
//	/** Default maximum load factor. */
//	protected static final double defaultMaxLoadFactor = 1.5;
//
//	/** Starting size of the hash table.	 */
//	protected static final int defaultHashArrayLength = 32;
//
//	/** Actual maximum load factor for this instance */
//	protected double maxLoadFactor;
	public QuestLog() {
		super();
	}

	/**
	 * Obtain an array of the keys (quest names) from the quest log.  There is
	 * no expectation of any particular ordering of the keys.
	 *
	 * @return The array of keys (quest names) from the quest log.
	 */
	public String[] keys() {
		// TODO/DONE Implement this method.
		String[] keyList = new String[this.defaultHashArrayLength];
		this.goFirst(); //now in first chain linkedList
		int i = 0;
		findNextItem(0);
		while (this.itemExists()) {
			keyList[i] = this.itemKey();
			i++;
			this.goForth();
		}
		String[] newList = new String[i];
		for (int k = 0; k < i; k++) {
			newList[k] = keyList[k];
		}
		return newList;  // Remove this line you're ready.  It's just to prevent compiler errors.
	}

	/**
	 * Format the quest log as a string which displays the quests in the log in
	 * alphabetical order by name.
	 *
	 * @return A nicely formatted quest log.
	 */
	public String toString() throws ContainerEmpty280Exception {
		// TODO/DONE Implement this method.
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Empty log and cannot print out");
		}
		String[] quests = this.keys();
		Arrays.sort(quests);    //this causes null pointer exception
		String print = "";
		for (int i = 0; i < quests.length; i++) {
			search(quests[i]);    //cursor now on specific item on list
			String item = " : " + this.item().getQuestArea() + " , Level Range: " + this.item().getRecommendedMinLevel() + "-" + this.item().getRecommendedMaxLevel() + "\n";
			quests[i] += item;
		}

		for (int index = 0; index < quests.length; index++) {
			print += quests[index];
		}
		return print;
	}


	/**
	 * Obtain the quest with name k, while simultaneously returning the number of
	 * items examined while searching for the quest.
	 *
	 * @param k Name of the quest to obtain.
	 * @return A pair in which the first item is the QuestLogEntry for the quest named k, and the
	 * second item is the number of items examined during the search for the quest named k.
	 * Note: if no quest named k is found, then the first item of the pair should be null.
	 */
	public Pair280<QuestLogEntry, Double> obtainWithCount(String k) {
		// TODO Implement this method.


		// Write a method that returns a Pair280 which contains the quest log entry with name k, 
		// and the number QuestLogEntry objects that were examined in the process.  You need to write
		// this method from scratch without using any of the superclass methods (mostly because 
		// the superclass methods won't be terribly useful unless you can modify them, which you
		// aren't allowed to do!).
		QuestLogEntry itemFound =this.obtain(k);
		//find linkedlist
		int position = this.hashPos(k);
		hashArray[position].goFirst();
		double count =0;

		//count number of items examined
		while(!hashArray[position].after()){
			count++;
			if (hashArray[position].item().key().compareTo(k) == 0){
				break;
			}
			else{
				hashArray[position].goForth();
			}
		}
		Pair280<QuestLogEntry, Double> tally = new Pair280<QuestLogEntry, Double>(itemFound, count);
		return tally;
	}


	public static void main(String args[]) {
		// Make a new Quest Log
		QuestLog hashQuestLog = new QuestLog();


		// Make a new ordered binary lib280.tree.
		OrderedSimpleTree280<QuestLogEntry> treeQuestLog =
				new OrderedSimpleTree280<QuestLogEntry>();


		// Read the quest data from a CSV (comma-separated value) file.
		// To change the file read in, edit the argument to the FileReader constructor.
		CSVReader inFile;
		try {
			//NOTE: if you are using ECLIPSE, remove the 'QuestLog/' portion of the
			//input filename on the next line.
			inFile = new CSVReader(new FileReader("/Users/admin/Documents/CMPT 280/QuestLog-Template/quests4.csv"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
			return;
		}

		String[] nextQuest;
		try {
			// Read a row of data from the CSV file
			while ((nextQuest = inFile.readNext()) != null) {
				// If the read succeeded, nextQuest is an array of strings containing the data from
				// each field in a row of the CSV file.  The first field is the quest name,
				// the second field is the quest region, and the next two are the recommended
				// minimum and maximum level, which we convert to integers before passing them to the
				// constructor of a QuestLogEntry object.
				QuestLogEntry newEntry = new QuestLogEntry(nextQuest[0], nextQuest[1],
						Integer.parseInt(nextQuest[2]), Integer.parseInt(nextQuest[3]));
				// Insert the new quest log entry into the quest log.
				hashQuestLog.insert(newEntry);
				treeQuestLog.insert(newEntry);
			}
		} catch (IOException e) {
			System.out.println("Something bad happened while reading the quest information.");
			e.printStackTrace();
		}

		// Print out the hashed quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
		System.out.println(hashQuestLog);

		// Print out the lib280.tree quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
		System.out.println(treeQuestLog.toStringInorder());


		// TODO Determine the average number of elements examined during access for hashed quest log.
		// (call hashQuestLog.obtainWithCount() for each quest in the log and find average # of access)
		Pair280 hash1 = hashQuestLog.obtainWithCount("Defeat Goliad");
		System.out.println("# of items examined per query for Defeat Goliath:" + hash1.secondItem());
		Pair280 hash2 = hashQuestLog.obtainWithCount("Destroy the Rats in the Farmer's Basement");
		System.out.println("# of items examined per query for Destroy the Rats: " + hash2.secondItem());
		Pair280 hash3 = hashQuestLog.obtainWithCount("Infiltrate the Bandit's Lair");
		System.out.println("# of items examined per query for Infiltrate the Bandit: " + hash3.secondItem());
		Pair280 hash4 = hashQuestLog.obtainWithCount("Locate the Lich's Lair");
		System.out.println("# of items examined per query for Locate the Lich's Lair: " + hash4.secondItem());

		double sum = (((Double) hash1.secondItem()) + ((Double) hash2.secondItem()) + ((Double) hash3.secondItem()) + ((Double) hash4.secondItem()));
		double ave = sum/4;
		System.out.println("");
		System.out.println("Avg . # of items examined per query in the hashed quest log with 4 entries : "+ave);


		// TODO Determine the average number of elements examined during access for lib280.tree quest log.
		// (call treeQuestLog.searchCount() for each quest in the log and find average # of access)


		double tree1 = treeQuestLog.searchCount((QuestLogEntry) hash1.firstItem());
		double tree2 = treeQuestLog.searchCount((QuestLogEntry) hash2.firstItem());
		double tree3 = treeQuestLog.searchCount((QuestLogEntry) hash3.firstItem());
		double tree4 = treeQuestLog.searchCount((QuestLogEntry) hash4.firstItem());

		sum = tree1 + tree2 + tree3+ tree4;
		ave = sum/4;
		System.out.println("");
		System.out.println("Avg . # of items examined per query in the tree quest log with 4 entries : "+ave);

	}
}

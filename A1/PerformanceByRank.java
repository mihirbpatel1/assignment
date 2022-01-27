import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import lib280.list.LinkedList280;

public class PerformanceByRank {

    public static LinkedList280<Crew> readCrewData(String path) {
        Scanner infile = null;

        try {
            infile = new Scanner(new File(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: File not found!");
        }

        // Initialize output list.
        LinkedList280<Crew> pirateCrew = new LinkedList280<Crew>();

        // While there is more stuff to read...
        while(infile.hasNext()) {
            // Read the three values for a Crew record
            int rank = infile.nextInt();
            double pay = infile.nextDouble();
            int sacks = infile.nextInt();

            // Create a crew object from the data
            Crew c = new Crew(rank, pay, sacks);

            // Place the new Crew instnace in the linked list.
            pirateCrew.insertFirst(c);
        }

        // Close the input file like a good citizen. :)
        infile.close();

        // Return the list of Crew objects.
        return pirateCrew;
    }


    public static void main( String args[] ) {
        // Read the data for Jack's pirate crew.

        // If you are getting a "File Not Found" error here, you may adjust the
        // path to piratecrew.txt as needed.
        LinkedList280<Crew> pirateCrew = readCrewData("piratecrew.txt");

	
        LinkedList280<Crew>[] array  = new LinkedList280[10];
        for (int i=0; i<array .length;i++) {
            array[i] = new LinkedList280<Crew>();
        }

       
        LinkedIterator280<Crew> arrayIterator  = pirateCrew.iterator();

       
        while (!arrayIterator .after()) {
           
            array[arrayIterator .item().rank].insert(arrayIterator.item());
            arrayIterator .goForth();
        }

       
        for (int i=0;i<10;i++) {

            
            float guineas = 0f;
            int sacks = 0;

            float rate = guineas / (float)sacks;
            arrayIterator  = array[i].iterator();

           
            while (!arrayIterator .after()) {
                guineas += arrayIterator.item().getPay();
                sacks += arrayIterator.item().getGrainSacks();
                arrayIterator .goForth();
            }

            

            System.out.println("Jack's rank " + Integer.toString(i) + " crew members were paid "
                    + Float.toString(rate) + " guines per sack of grain plundred.");
        }
    }

}

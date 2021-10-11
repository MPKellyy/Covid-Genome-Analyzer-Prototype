import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class lineageAverageConverter {
    //Variables
    String lineageAverage;


    //Methods
    public lineageAverageConverter(String lineageAverage) {
        this.lineageAverage = lineageAverage;
    }

    public String convertAverageToPatternInput() {
        //Setting up required variables
        String input = "";
        char currentNucleotide;

        Map<Character, String> nucleotideInts = new HashMap<Character, String>();

        nucleotideInts.put('a', "1 -1 ");
        nucleotideInts.put('t', "2 -1 ");
        nucleotideInts.put('g', "3 -1 ");
        nucleotideInts.put('c', "4 -1 ");
        nucleotideInts.put('@', "-2\n");

        //Converting nucleotides to readable AI format
        for(int i = 0; i < lineageAverage.length(); i++) {
            currentNucleotide = lineageAverage.charAt(i);
            input += nucleotideInts.get(currentNucleotide);
        }


        //Exporting to txt file
        try {
            File myObj = new File("CSPAM-TKS-ERMINER-input.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter fileWrite = new FileWriter("CSPAM-TKS-ERMINER-input.txt");
            fileWrite.write(input);
            fileWrite.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        return input;
    }

    public String convertAverageToAprioriInput() {
        //Setting up required variables
        String input = "";
        char currentNucleotide;

        Map<Character, String> nucleotideInts = new HashMap<Character, String>();

        nucleotideInts.put('a', "1 ");
        nucleotideInts.put('t', "2 ");
        nucleotideInts.put('g', "3 ");
        nucleotideInts.put('c', "4 ");
        nucleotideInts.put('@', "\n");

        //Converting nucleotides to readable AI format
        for(int i = 0; i < lineageAverage.length(); i++) {
            currentNucleotide = lineageAverage.charAt(i);
            input += nucleotideInts.get(currentNucleotide);
        }


        //Exporting to txt file
        try {
            File myObj = new File("Apriori-input.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter fileWrite = new FileWriter("Apriori-input.txt");
            fileWrite.write(input);
            fileWrite.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        return input;
    }
}

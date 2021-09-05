import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        //Reading/saving Strings from text file
        ArrayList<String> wordList = readTextfile();

        //Setting VariantGroup
        VariantGroup test = new VariantGroup(30000);
        test.addGenomes(wordList);

        //Verifying Results
        Map<String, String> similarities = test.findSimilaritiesOrdered(4);

        for(String key: similarities.keySet()) {
            System.out.println(similarities.get(key) + ": " + key);
        }

        //TODO: Order sequences by position
        //TODO: Identify basic genetic sequences
        //TODO: Find differences?
        //TODO: Look for differences in similarities

    }

    /**
     * Function reads in genomes from a text file
     * @return Returns an ArrayList of genomes read in from file
     */
    public static ArrayList<String> readTextfile() {
        ArrayList<String> genomeArray = new ArrayList<String>();

        try {
            File file = new File("genomes.txt");
            Scanner scanner = new Scanner(file);
            String genome = "";
            String nextLine = "";

            while(scanner.hasNextLine()) {
                nextLine = scanner.nextLine();

                if(nextLine.equals("/")) {
                    genomeArray.add(genome);
                    genome = "";
                }
                else {
                    genome += nextLine;
                    genome = genome.replaceAll("[^atgc]", "");
                    genome = genome.replace(" ", "");
                }

            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

        return genomeArray;
    }
}

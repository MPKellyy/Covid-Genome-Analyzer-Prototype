import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

//        ArrayList<Character> test2 = test.generateSimilaritySequence();
//
//        for(Character letter: test2){
//            System.out.println(letter);
//        }

        //Verifying Results
        Map<String, String> similarities = test.findSimilaritiesOrdered(6);

        for(String key: similarities.keySet()) {
            System.out.println(key + ": " + similarities.get(key));
        }

        System.out.println(writeFilteredGenomes(wordList));

        //TODO: Don't let n affect similarity logic
        //TODO: Translate to amino acids
        //TODO: All Amino acids
        //TODO: All Amino acid sequences
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
                    //System.out.println(genome);
                    genomeArray.add(genome);
                    genome = "";
                }
                else {
                    genome += nextLine;
                    genome = genome.replaceAll("[^atgcn]", "");
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

    public static boolean writeFilteredGenomes(ArrayList<String> genomeList) {
        try {
            File myObj = new File("filteredGenomes.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter fileWrite = new FileWriter("filteredGenomes.txt");

            for(String genome: genomeList) {
                fileWrite.write(genome);
                fileWrite.write("\n\n\n\n");
            }

            fileWrite.close();

            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }
}

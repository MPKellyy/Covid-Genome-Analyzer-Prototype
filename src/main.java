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
        /**
         * Association Phase
         *
         * Identify which strain emerged first in the real world and every emergent strain after.
         * For example, the Beta strain seems to have emerged in the real world first, followed by the Alpha strain.
         * This is a parent-child relationship, where Beta is the parent, and Alpha is the child strain.
         * Likewise, Alpha is the parent to the Delta strain, and so forth.
         *
         * The collection of parent-child associations in order of appearance is called a lineage.
         * Each strain early in the lineage can be denoted as an ancestor, an every subsequent strain as a successor.
         * For example, the Beta strain is the ancestor to Alpha, Delta, Kappa...etc (the rest of the successors).
         * Likewise, the Alpha strain is the ancestor to Delta, Kappa, Gamma, etc, while also being a succesor to Beta.
         *
         * The primary goal prior to executing this code is to identify a lineage pattern.
         */



        /**
         * Self-Similarity Identification Phase
         *
         *  Each strain has a set of genome samples read from a text file.
         *  These samples of the same strain have slight genetic variations to each other.
         *
         *  The goal of this phase is to filter out the variations and save the commonalities
         *  between each sample.
         *  The similarities within a strain's samples will be called the strain average
         */
        //Constants for genome size and filter size
        final int MAX_SIZE = 30000;//Max size a corona virus genome could ever be
        final int FILTER_NUM = 12;//Only looking for nucleotide sequences 12 base pairs and over

        //Setting up variant groups (aka strains)
        //Note: These are declared in lineage order (top to bottom)
        VariantGroup Beta = new VariantGroup("Beta", MAX_SIZE);
        VariantGroup Alpha = new VariantGroup("Alpha", MAX_SIZE);
        VariantGroup Delta = new VariantGroup("Delta", MAX_SIZE);
        VariantGroup Kappa = new VariantGroup("Kappa", MAX_SIZE);
        VariantGroup Gamma = new VariantGroup("Gamma", MAX_SIZE);
        VariantGroup Iota = new VariantGroup("Iota", MAX_SIZE);
        VariantGroup Eta = new VariantGroup("Eta", MAX_SIZE);
        VariantGroup Lambda = new VariantGroup("Lambda", MAX_SIZE);

        //Calculating strain average within each variant group (results printed in a text file)
        generateResult(Beta,"Beta", FILTER_NUM);
        generateResult(Alpha,"Alpha", FILTER_NUM);
        generateResult(Delta,"Delta", FILTER_NUM);
        generateResult(Kappa,"Kappa", FILTER_NUM);
        generateResult(Gamma,"Gamma", FILTER_NUM);
        generateResult(Iota,"Iota", FILTER_NUM);
        generateResult(Eta,"Eta", FILTER_NUM);
        generateResult(Lambda,"Lambda", FILTER_NUM);



        /**
         * Parent-Child Similarity Phase
         *
         * Goal of this phase is to find similarities between each parent and child's strain average.
         * These similarities will be denoted as unit averages (where a unit is a parent-child combination).
         *
         * These unit averages will be compiled into a string in order of strain lineage (ancestor to successor).
         * The end result is a sequence of nucleotide sequences that were kept between each parent-child
         * strain in the lineage.
         *
         * Note: Each computation as of now takes a VERY long time to compute (approx 2-3 hours).
         * To save time, I have included a txt file that already has the compiled results
         * called "lineage-average.txt".
         */
        //String that will hold lineageAverage
        String lineageAverage = "";

        //If lineage-average.txt is present, contents from precomputed file are extracted
        try {
            File file = new File("lineage-average.txt");
            Scanner scanner = new Scanner(file);
            String nextLine;

            while(scanner.hasNextLine()) {
                nextLine = scanner.nextLine();
                lineageAverage += nextLine;
            }

            lineageAverage = lineageAverage.replaceAll(" ", "");

            scanner.close();
        }
        //If the precomputed file is not found, lineage average is calculate (approx 2-4 hr)
        catch (FileNotFoundException e) {
            //Setting up array that will hold all of the unit averages
            ArrayList<String> similarityArray = new ArrayList<String>();

            //Creating parent-child associations
            ParentChildComparator parentChildBA = new ParentChildComparator(Beta, Alpha);
            ParentChildComparator parentChildAD = new ParentChildComparator(Alpha, Delta);
            ParentChildComparator parentChildDK = new ParentChildComparator(Delta, Kappa);
            ParentChildComparator parentChildKG = new ParentChildComparator(Kappa, Gamma);
            ParentChildComparator parentChildGI = new ParentChildComparator(Gamma, Iota);
            ParentChildComparator parentChildIE = new ParentChildComparator(Iota, Eta);
            ParentChildComparator parentChildEL = new ParentChildComparator(Eta, Lambda);

            //Adding the unit averages into a singular array
            updateSimilarityArray(similarityArray, parentChildBA.computeUnitAverage(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildAD.computeUnitAverage(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildDK.computeUnitAverage(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildKG.computeUnitAverage(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildGI.computeUnitAverage(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildIE.computeUnitAverage(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildEL.computeUnitAverage(FILTER_NUM));

            //Removing duplicates (duplicates include substrings of strings in the arraylist)
            ArrayList<String> filteredSimilarities = (ArrayList<String>) similarityArray.clone();

            for(String sequence1: similarityArray) {
                for(String sequence2: similarityArray) {
                    if(!sequence1.equals(sequence2) && sequence1.contains(sequence2)) {
                        filteredSimilarities.remove(sequence2);
                    }
                }
            }

            //Concatenating all unit averages into one string
            for(String similarity: filteredSimilarities) {
                lineageAverage += similarity;
            }

            writeLineageAverage(lineageAverage);
        }

        lineageAverageConverter testing = new lineageAverageConverter(lineageAverage);

        System.out.println(testing.convertAverageToInput());

    }

    /**
     * Function reads in genomes from a text file
     * @return Returns an ArrayList of genomes read in from file
     */
    public static ArrayList<String> readTextfile(String fileName) {
        ArrayList<String> genomeArray = new ArrayList<String>();

        try {
            File file = new File(fileName + ".txt");
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

    /**
     * Function used to create txt file containing the strain average result (similarities within a strain)
     * @param variant Type of strain
     * @param variantName Strain name
     * @param filterNum Size for filtering sequence sizes
     */
    public static void generateResult(VariantGroup variant, String variantName, int filterNum) {
        ArrayList<String> wordList;
        wordList = readTextfile(variantName);
        variant.addGenomes(wordList);
        variant.writeAnalysis(filterNum);
    }

    /**
     * Function used to update the array containing all of the unit averages in a lineage
     * @param similarityArray Array containing unit averages
     * @param newFragments Incoming unit averages to be added
     */
    public static void updateSimilarityArray(ArrayList<String> similarityArray, ArrayList<String> newFragments) {
        for(String fragment: newFragments) {
            similarityArray.add(fragment);
        }
    }

    /**
     * Function used to write the filtered and compiled unit averages in a lineage
     * @param similaritySequence compiled and filtered unit averages
     * @return true/false based on operation completion
     */
    public static boolean writeLineageAverage(String similaritySequence) {
        try {
            File myObj = new File("lineage-average.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
                return false;
            }

            FileWriter fileWrite = new FileWriter("lineage-average.txt");
            fileWrite.write(similaritySequence);
            fileWrite.close();

            return true;

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }
}

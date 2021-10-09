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
         * Note: Each computation as of now takes a VERY long time to compute (approx 3-4 hours).
         * To save time, I have included a txt file that already has the compiled results
         * called "unfiltered-lineage-similarities.txt".
         */
        //Setting up array that will hold all of the unit averages
        ArrayList<String> similarityArray = new ArrayList<String>();


        //If the pre-computed file is present, the unit averages will be extracted from the file
        try {
            File file = new File("unfiltered-lineage-similarities.txt");
            Scanner scanner = new Scanner(file);
            String fragment = "";
            String nextLine = "";

            while(scanner.hasNextLine()) {
                nextLine = scanner.nextLine();

                if(nextLine.equals("/")) {
                    similarityArray.add(fragment);
                    fragment = "";
                }
                else {
                    fragment += nextLine;
                }

            }
            scanner.close();

        }
        //If the file is not present, the code will undergo the long computation process
        catch (FileNotFoundException e) {
            //Creating parent-child associations
            ParentChildComparator parentChildBA = new ParentChildComparator(Beta, Alpha);
            ParentChildComparator parentChildAD = new ParentChildComparator(Alpha, Delta);
            ParentChildComparator parentChildDK = new ParentChildComparator(Delta, Kappa);
            ParentChildComparator parentChildKG = new ParentChildComparator(Kappa, Gamma);
            ParentChildComparator parentChildGI = new ParentChildComparator(Gamma, Iota);
            ParentChildComparator parentChildIE = new ParentChildComparator(Iota, Eta);
            ParentChildComparator parentChildEL = new ParentChildComparator(Eta, Lambda);

            //Adding the unit averages into a singular array
            updateSimilarityArray(similarityArray, parentChildBA.computeSimilarityString(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildAD.computeSimilarityString(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildDK.computeSimilarityString(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildKG.computeSimilarityString(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildGI.computeSimilarityString(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildIE.computeSimilarityString(FILTER_NUM));
            updateSimilarityArray(similarityArray, parentChildEL.computeSimilarityString(FILTER_NUM));

            //Writing a the lineage average to a text file for faster computation next time code is ran
            writeUnfilteredLineageSimilarities(similarityArray);
        }

        //Removing duplicates from found between unit averages in lineage

        //These lines of code are a work around to a formatting issue I encountered with the text files...
        //TODO: Optimize this better
        String tempStr = similarityArray.toString();
        tempStr = tempStr.substring(1, tempStr.length()-1);
        tempStr = tempStr.replaceAll("[, ]", "");

        String[] tempArray = tempStr.split("/");

        //Will require an arraylist for the filtration process
        ArrayList<String> filteredSimilarities = new ArrayList<String>();

        for(String contents: tempArray) {
            filteredSimilarities.add(contents);
        }

        //Removing duplicates (duplicates include substrings of strings in the arraylist)
        for(String sequence1: tempArray) {
            for(String sequence2: tempArray) {
                if(!sequence1.equals(sequence2) && sequence1.contains(sequence2)) {
                    filteredSimilarities.remove(sequence2);
                }
            }
        }

        //Concatenating all unit averages into one string
        String similarityString = "";
        for(String similarity: filteredSimilarities) {
            similarityString += similarity;
        }

        System.out.println(similarityString);

        writeLineageSimilarity(similarityString);





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
                return false;
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
     * Function used to write the unfiltered unit averages found across lineage
     * This is used to speed up computation speed everytime code is ran
     * @param similarityList list containing all units averages
     * @return true/false depending on operation completion
     */
    public static boolean writeUnfilteredLineageSimilarities(ArrayList<String> similarityList) {
        try {
            File myObj = new File("unfiltered-lineage-similarities.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
                return false;
            }

            FileWriter fileWrite = new FileWriter("unfiltered-lineage-similarities.txt");

            for(String fragment: similarityList) {
                fileWrite.write(fragment +"\n");
                fileWrite.write('/' + "\n");
            }

            fileWrite.close();

            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Function used to extract unit averages from a txt file rather than undergoing a lengthy computation
     * @param fileName File name
     * @return true/false depending on operation completion
     */
    public static ArrayList<String> readUnfilteredLineageSimilarities(String fileName) {
        ArrayList<String> fragmentArray = new ArrayList<String>();

        try {
            File file = new File(fileName + ".txt");
            Scanner scanner = new Scanner(file);
            String fragment = "";
            String nextLine = "";

            while(scanner.hasNextLine()) {
                nextLine = scanner.nextLine();

                if(nextLine.equals("/")) {
                    fragmentArray.add(fragment);
                    fragment = "";
                }
                else {
                    fragment += nextLine;
                }

            }
            scanner.close();
        }
        catch (FileNotFoundException e) {

        }

        return fragmentArray;
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
    public static boolean writeLineageSimilarity(String similaritySequence) {
        try {
            File myObj = new File("lineage-similarity.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
                return false;
            }

            FileWriter fileWrite = new FileWriter("lineage-similarity.txt");
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

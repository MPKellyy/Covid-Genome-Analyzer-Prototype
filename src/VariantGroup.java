import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for comparing similarities between genomes of a viral strain
 */
public class VariantGroup {
    /**
     * NOTES:
     * Need to ensure string lengths are same. Save length of largest string, add x for to left over strings, update fcn? deletes?
     */
    private ArrayList<String> genomes;
    private int MAX_SIZE;
    private String variantName;

    /**
     * Constructor for VariantGroup
     */
    public VariantGroup(String variantName, int MAX_SIZE) {
        genomes = new ArrayList<String>();
        this.variantName = variantName;

        if(MAX_SIZE <= 0) {
            this.MAX_SIZE = 1;
        }
        else {
            this.MAX_SIZE = MAX_SIZE;
        }

    }

    /**
     * Allows user to add a single genome string to structure
     * @param genome string sequence of a genome
     */
    public void addGenome(String genome) {
        genomes.add(genomeAdjuster(genome));
    }

    /**
     * Allows user to add a list of genome strings
     * @param inputGenomes ArrayList of genome strings
     */
    public void addGenomes(ArrayList<String> inputGenomes) {
        for(String genome: inputGenomes)
            genomes.add(genomeAdjuster(genome));
    }

    /**
     * If the genome is stored in structure, it will be deleted
     * @param genome genome to be deleted
     * @return
     */
    public boolean removeGenome(String genome) {
        if(genomes.contains(genome)) {
            genomes.remove(genome);
            return true;
        }

        return false;
    }

    /**
     * Checks if structure has the inputted genome
     * @param genome string of genome being looked for
     * @return
     */
    public boolean hasGenome(String genome) {
        return genomes.contains(genome);
    }

    /**
     * Returns a clone of all stored genomes
     * @return
     */
    public ArrayList<String> getGenomes() {
        return (ArrayList<String>) genomes.clone();
    }

    /**
     * Creates a new ArrayList to store genome strings in
     */
    public void clearGenomes() {
        genomes = new ArrayList<String>();
    }

    /**
     * Function used to identify similarities between genomes currently stored
     * Note: differences are saved as '-' within the returned Character ArrayList
     * @return returns ArrayList of characters compared. Elements with '-' mark differences found
     * at a given index
     */
    public ArrayList<Character> generateSimilaritySequence() {
        //Checking if stored genome list is empty
        if(genomes.isEmpty())
            return new ArrayList<Character>();

        //Setting first saved genome as the string being compared
        String base = genomes.get(0);
        //Keeps track if all genomes match at a specified position in their sequence
        boolean allMatch = true;
        //List that will keep track of nucleotide positions
        ArrayList<Character> unfilteredSequence = new ArrayList<Character>();
        Character currentNucleotide;

        //Looping through every element of first genome
        for(int i = 0; i < base.length(); i++) {
            currentNucleotide = base.charAt(i);

            if(currentNucleotide == 'n') {
                for(String genome: genomes) {
                    if(genome.charAt(i) != 'n') {
                        currentNucleotide = genome.charAt(i);
                        break;
                    }
                }
            }

            //Looping through all genomes other than the first (which is being compared to)
            for(int j = 1; j < genomes.size(); j++) {
                //If at any point at least one genome does not match, comparisons are stopped for current position
                if((genomes.get(j).charAt(i) != currentNucleotide) && (genomes.get(j).charAt(i) != 'n') && (currentNucleotide != 'n')) {
                    allMatch = false;
                    //System.out.println("Failing Char: " + genomes.get(j).charAt(i));
                    break;
                }
            }

            //If all matched, add the matching character to the Character ArrayList
            if(allMatch)
                unfilteredSequence.add(currentNucleotide);
            //Else, mark difference with a '-'
            else
                unfilteredSequence.add('-');

            allMatch = true;
        }

        return unfilteredSequence;
    }

    /**
     * Generates a string of the genome comparisons unfiltered (with '-' marking differences)
     * @return
     */
    public String getUnfilteredSimString() {
        //String that will hold nucleotides from genome comparison similarity
        String unfilteredResult = "";

        //Concatenating nucleotide to sequence string
        for(Character nucleotide: generateSimilaritySequence()) {
            unfilteredResult += nucleotide;
        }

        return unfilteredResult;
    }

    /**
     * Finds similar nucleotide sequences between same strains and their locations within genomes
     * @return
     */
    public Map<String, String> findSimilaritiesOrdered(int filterSequenceSizeBelow) {
        //Case for if genomes list is empty
        if(genomes.isEmpty())
            return new TreeMap<String, String>();

        //Saves generated unfilteredNucleotide Sequence
        ArrayList<Character> unfilteredSequence = generateSimilaritySequence();
        //Map for saving sequences to genomes respective locations
        Map<String, String> similaritiesOrdered = new TreeMap<String, String>();
        //String for sequence location on genome
        String order = "";
        //String for saving nucleotide
        String sequence = "";
        //Integer for saving position of the start of a new sequence
        int start = 0;
        //Boolean for keeping track of consecutive sequencing
        boolean needStart = unfilteredSequence.get(0) == '-';

        //Loop through unfiltered nucleotide similarity sequence
        for(int i = 0; i < unfilteredSequence.size(); i++) {
            //Case for when a '-' is first encountered
            if(unfilteredSequence.get(i) == '-' && !needStart) {
                order = Integer.toString(start).length() + "-" + Integer.toString(start) + "-" + Integer.toString(i-1);
                if(sequence.length() >= filterSequenceSizeBelow)
                    similaritiesOrdered.put(order, sequence);
                order = "";
                sequence = "";
                needStart = true;
                start = -1;
            }
            //Case for when a '-' is followed by a valid nucleotide
            else if(needStart && unfilteredSequence.get(i) != '-') {
                start = i;
                sequence += unfilteredSequence.get(i);
                needStart = false;
            }
            //Case for when a nucleotide is followed by another nucleotides
            else if (unfilteredSequence.get(i) != '-'){
                sequence += unfilteredSequence.get(i);
            }
        }

        //Ensuring final sequence gets saved if possible
        if(start != -1) {
            order = Integer.toString(start) + "-" + Integer.toString(unfilteredSequence.size()-1);
            if(sequence.length() >= filterSequenceSizeBelow)
                similaritiesOrdered.put(order, sequence);
        }

        return similaritiesOrdered;
    }

    /**
     * Function used to ensure that genome string length abides by the MAX_SIZE passed in by user
     * @param genome
     * @return
     */
    private String genomeAdjuster(String genome) {
        if(genome.length() > MAX_SIZE) {
            genome = genome.substring(0, MAX_SIZE);
        }
        else if(genome.length() < MAX_SIZE) {
            for(int i = genome.length(); i < MAX_SIZE; i++) {
                genome += "-";
            }
        }

        return genome;
    }

    /**
     * Function used to output strain average to txt file (commonly occurring sequences within a strsin)
     * @param filterSequenceSizeBelow
     * @return
     */
    public boolean writeAnalysis(int filterSequenceSizeBelow) {
        try {
            String fileName = variantName + "-analysis-results" + ".txt";
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
                return false;
            }

            FileWriter fileWrite = new FileWriter(fileName);


            fileWrite.write("Name of Variant: " + variantName);
            fileWrite.write("\n\n\n\n");

            fileWrite.write("Similarity Sequences: ");
            fileWrite.write("\n\n");

            Map<String, String> similarities = findSimilaritiesOrdered(filterSequenceSizeBelow);

            for(String key: similarities.keySet()) {
                String keyCopy = key;
                keyCopy = keyCopy.substring(2, keyCopy.length());
                fileWrite.write("[\n" + keyCopy + "\n:::\n" + similarities.get(key) + "\n]\n\n");
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

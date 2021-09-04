import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Constructor for VariantGroup
     */
    public VariantGroup(int MAX_SIZE) {
        genomes = new ArrayList<String>();

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
        //Setting first saved genome as the string being compared
        String base = genomes.get(0);
        //Keeps track if all genomes match at a specified position in their sequence
        boolean allMatch = true;
        //List that will keep track of nucleotide positions
        ArrayList<Character> unfilteredSequence = new ArrayList<Character>();

        //Looping through every element of first genome
        for(int i = 0; i < base.length(); i++) {

            //Looping through all genomes other than the first (which is being compared to)
            for(int j = 1; j < genomes.size(); j++) {
                //If at any point at least one genome does not match, comparisons are stopped for current position
                if(genomes.get(j).charAt(i) != base.charAt(i)) {
                    allMatch = false;
                    break;
                }
            }

            //If all matched, add the matching character to the Character ArrayList
            if(allMatch)
                unfilteredSequence.add(base.charAt(i));
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
    public Map<String, String> findSimilaritiesOrdered() {
        //Saves generated unfilteredNucleotide Sequence
        ArrayList<Character> unfilteredSequence = generateSimilaritySequence();
        //Map for saving sequences to genomes respective locations
        Map<String, String> similaritiesOrdered = new HashMap<String, String>();
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
                order = Integer.toString(start) + "-" + Integer.toString(i-1);
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
            similaritiesOrdered.put(order, sequence);
        }

        return similaritiesOrdered;
    }

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

}

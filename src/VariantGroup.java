import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariantGroup {
    /**
     * NOTES:
     * Need to ensure string lengths are same. Save length of largest string, add x for to left over strings, update fcn? deletes?
     */
    private ArrayList<String> genomes;

    public VariantGroup() {
        genomes = new ArrayList<String>();
    }

    //Add genome
    public void addGenome(String genome) {
        genomes.add(genome);
    }

    //Add genomes
    public void addGenomes(ArrayList<String> inputGenomes) {
        for(String genome: inputGenomes)
            genomes.add(genome);
    }

    //Remove genomes
    public boolean removeGenome(String genome) {
        if(genomes.contains(genome)) {
            genomes.remove(genome);
            return true;
        }

        return false;
    }

    //Check if in VariantGroup
    public boolean hasGenome(String genome) {
        return genomes.contains(genome);
    }

    //Get all genomes
    public ArrayList<String> getGenomes() {
        return (ArrayList<String>) genomes.clone();
    }

    //Clears stored genomes
    public void clearGenomes() {
        genomes = new ArrayList<String>();
    }

    //Generate Similarity Sequence
    public ArrayList<Character> generateSimilaritySequence() {
        //Similarity Logic
        String base = genomes.get(0);
        boolean allMatch = true;
        ArrayList<Character> unfilteredSequence = new ArrayList<Character>();

        for(int i = 0; i < base.length(); i++) {

            for(int j = 1; j < genomes.size(); j++) {
                if(genomes.get(j).charAt(i) != base.charAt(i)) {
                    allMatch = false;
                    break;
                }
            }

            if(allMatch)
                unfilteredSequence.add(base.charAt(i));
            else
                unfilteredSequence.add('-');

            allMatch = true;
        }

        return unfilteredSequence;
    }

    //Generate Unfiltered Similarity String
    public String getUnfilteredSimString() {
        //Getting String unfiltered result
        String unfilteredResult = "";

        for(Character letter: generateSimilaritySequence()) {
            unfilteredResult += letter;
        }

        return unfilteredResult;
    }

    //Generate Similarity Map (filtered)
    public Map<Character, Integer> getFilteredSimMap() {
        ArrayList<Character> similaritySequence = generateSimilaritySequence();
        Map<Character, Integer> filteredSimMap = new HashMap<Character, Integer>();

        for(int k = 0; k < similaritySequence.size(); k++)
        {
            System.out.println(similaritySequence.get(k) + ": " + k);
        }

        for(int i = 0; i < similaritySequence.size(); i++) {
            if(similaritySequence.get(i) != '-')
                filteredSimMap.put(similaritySequence.get(i), i);
        }

        return filteredSimMap;
    }

}

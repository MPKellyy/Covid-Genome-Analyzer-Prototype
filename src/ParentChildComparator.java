import java.util.*;

public class ParentChildComparator {
    //Variables
    private VariantGroup parent;
    private VariantGroup child;

    //Methods
    /**
     * ParentChildComparator constructor
     * @param parent Parent strain
     * @param child Child strain
     */
    ParentChildComparator(VariantGroup parent, VariantGroup child) {
        this.parent = parent;
        this.child = child;
    }

    /**
     * Computes the unit average between a parent and its child
     * @param filterNum Filter size of similar nucleotide sequences
     * @return ArrayList containing unit average
     */
    ArrayList<String> computeSimilarityString(int filterNum){
        //Setting up necessary variables
        ArrayList<String> allCommonSequences = new ArrayList<String>();
        ArrayList<String> unfilteredSimilarities = new ArrayList<String>();

        Map<String, String> parentMap = parent.findSimilaritiesOrdered(filterNum);
        Map<String, String> childMap = child.findSimilaritiesOrdered(filterNum);

        String currentParentSequence = "";
        String currentChildSequence = "";

        //Extracting contents from parent and child similarity maps into strings for easier analysis
        for(String parentKey: parentMap.keySet()) {
            currentParentSequence += parentMap.get(parentKey) + "@@@@";
        }

        for(String childKey: childMap.keySet()) {
            currentChildSequence += childMap.get(childKey) + "@@@@";
        }

        //To increase efficiency, if statement below keeps track of which strain has the most/least amount of sequences
        //If the current parent has less sequences than the child, the two are swapped code-wise to reduce
        //the amount of iterations in the computation (result will be the same regardless, just a matter of saved time)
        if(currentParentSequence.length() < currentChildSequence.length()) {
            String temp = currentParentSequence;
            currentParentSequence = currentChildSequence;
            currentChildSequence = temp;

            Map<String, String> tempParent = new TreeMap<String, String>();
            Map<String, String> tempChild = new TreeMap<String, String>();
            tempParent.putAll(parentMap);
            tempChild.putAll(childMap);
            parentMap.clear();
            childMap.clear();
            parentMap.putAll(tempChild);
            childMap.putAll(tempParent);
        }


        //Loop that compares parent and child strain averages
        for (int j = 0; j < currentChildSequence.length(); j++) {
            String commonSequence = "";
            String previousString = "";

            for(int k = j; k < currentChildSequence.length(); k++) {
                commonSequence += currentChildSequence.charAt(k);

                if(currentParentSequence.contains(previousString) && !currentParentSequence.contains(commonSequence)) {
                    unfilteredSimilarities.add(previousString);
                }

                previousString = commonSequence;
            }
        }

        ArrayList<String> filteredSimilarities = (ArrayList<String>) unfilteredSimilarities.clone();

        //Removing duplicates
        for(String sequence1: unfilteredSimilarities) {
            for(String sequence2: unfilteredSimilarities) {
                if(!sequence1.equals(sequence2) && sequence1.contains(sequence2))
                    filteredSimilarities.remove(sequence2);
            }
        }

        //Additional filtering, ensuring unwanted fragment sizes are not saved
        for(String sequence: filteredSimilarities) {
            if(sequence.contains("@@@@")) {
                String[] substrings = sequence.split("@@@@");

                for (String substring: substrings){
                    if(sequence.length() >= filterNum)
                        allCommonSequences.add(substring);
                }
            }

            else if(sequence.length() >= filterNum) {
                allCommonSequences.add(sequence);
            }
        }

        return allCommonSequences;
    }



}

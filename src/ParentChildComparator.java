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
    ArrayList<String> computeUnitAverage(int filterNum){
        //Setting up necessary variables
        ArrayList<String> allCommonSequences = new ArrayList<String>();

        Map<String, String> parentMap = parent.findSimilaritiesOrdered(filterNum);
        Map<String, String> childMap = child.findSimilaritiesOrdered(filterNum);
        Map<String, String> tempSave = new HashMap<String, String>();

        String s1 = "";
        String s2 = "";
        String currentS;
        String previousS;

        //Extracting contents from parent and child similarity maps into strings for easier analysis
        for(String parentKey: parentMap.keySet()) {
            s1 += parentMap.get(parentKey) + "*";
        }

        for(String childKey: childMap.keySet()) {
            s2 += childMap.get(childKey) + "*";
        }

        //To increase efficiency, if statement below keeps track of which strain has the most/least amount of sequences
        //If the current parent has less sequences than the child, the two are swapped code-wise to reduce
        //the amount of iterations in the computation (result will be the same regardless, just a matter of saved time)
        if(s1.length() < s2.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }


        //These nested loops check to see if every substring of the child similarities is within the parent similarities
        for(int i = 0; i < s2.length(); i++) {
            currentS = "";

            //This line of code separates the child string into the original saved sequences (ensures that correct sequences are used for comparisons)
            //For example, child string = 'hello*world'
            //Rather than checking if 'hello*world' is in parent string, 'hello' and 'world' are checked separately
            if(s2.charAt(i) == '*')
                continue;

            for(int j = i; j < s2.length(); j++) {
                previousS = currentS;
                currentS += s2.charAt(j);

                //If a common substring that was not seen before, save it in an arraylist
                if(s1.contains(previousS) && !s1.contains(currentS) && !tempSave.containsKey(previousS) && !previousS.equals("")) {

                    //Checking if the similar nucleotide fragment is the requested size, only adds if size is with desired range
                    if(previousS.length() >= filterNum) {
                        //Adding sequence/fragment
                        allCommonSequences.add(previousS);

                        //Map that keeps track of the substrings of the sequences already saved
                        //This map is used to ensure that no duplicate strings (same strings or substrings of strings) are saved
                        for(int k = 0; k < previousS.length(); k++) {
                            tempSave.put(previousS.substring(k), previousS);
                        }
                    }

                    currentS = "";
                }
                //If a sequence was not found in the parent and a new sequence needs to be checked,
                else if(currentS.contains("*")) {
                    if(s1.contains(previousS) && !tempSave.containsKey(previousS) && !previousS.equals("") && previousS.length() >= filterNum) {
                        //Adding sequence/fragment
                        allCommonSequences.add(previousS);

                        //Map that keeps track of the substrings of the sequences already saved
                        //This map is used to ensure that no duplicate strings (same strings or substrings of strings) are saved
                        for(int k = 0; k < previousS.length(); k++) {
                            tempSave.put(previousS.substring(k), previousS);
                        }
                    }
                    currentS = "";
                }
            }
        }

        //Printing out sequences to let user know that computation has been completed for this unit
        for(String sequence: allCommonSequences)
        {
            System.out.println(sequence);
        }

        return allCommonSequences;
    }

}

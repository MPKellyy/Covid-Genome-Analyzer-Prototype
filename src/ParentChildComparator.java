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
        Map<String, String> parentMap = parent.findSimilaritiesOrdered(filterNum);
        Map<String, String> childMap = child.findSimilaritiesOrdered(filterNum);

        String s = "";
        String t = "";

        //Extracting contents from parent and child similarity maps into strings for easier analysis
        for(String parentKey: parentMap.keySet()) {
            s += parentMap.get(parentKey) + "*";
        }

        for(String childKey: childMap.keySet()) {
            t += childMap.get(childKey) + "-";
        }

//        s = "Hello";
//        t = "Hello World";


        //Ensuring strings are same size
        int sSize = s.length();
        int tSize = t.length();

        if(sSize > tSize) {
            int difference = sSize - tSize;
            for(int i = 0; i < difference; i++) {
                t += "-";
            }
        }
        else if(tSize > sSize) {
            int difference = tSize - sSize;

            for(int i = 0; i < difference; i++) {
                s += "*";
            }
        }


        //Setting up variables
        int[][] table = new int[s.length()][t.length()];
        ArrayList<String> holder = new ArrayList<String>();

        int previousSequence;
        int currentSequence;

        boolean foundSequence = false;
        ArrayList<String> words = new ArrayList<>();


        //Setting up table
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < t.length(); j++) {
                if (s.charAt(i) != t.charAt(j)) {
                    continue;
                }

                table[i][j] = (i == 0 || j == 0) ? 1
                        : 1 + table[i - 1][j - 1];
            }
        }


        //Looping top diag
        for (int i = table.length - 1; i > 0; i--) {
            String temp = "";
            String word = "";

            currentSequence = 0;
            previousSequence = 0;
            foundSequence = false;

            for (int j = 0, x = i; x <= table.length - 1; j++, x++) {
                previousSequence = currentSequence;
                temp = temp+ Integer.toString(table[x][j]);
                currentSequence = table[x][j];

                if(foundSequence && (currentSequence != previousSequence+1) && (currentSequence == 0)){
                    foundSequence = false;

                    if(!words.contains(word) && word.length() >= filterNum)
                        words.add(word);

                    word = "";
                }
                else if(foundSequence && x == table.length - 1) {
                    foundSequence = false;
                    word += s.charAt(x);

                    if(!words.contains(word) && word.length() >= filterNum)
                        words.add(word);

                    word = "";
                }
                else if(foundSequence) {
                    word += s.charAt(x);
                }
                else if(currentSequence == previousSequence+1) {
                    word += s.charAt(x);
                    foundSequence = true;
                }
            }
            //System.out.println(temp);
        }





        //Looping bottom diag
        for (int i = 0; i <= table.length - 1; i++) {
            String temp = "";
            String word = "";

            currentSequence = 0;
            previousSequence = 0;
            foundSequence = false;

            for (int j = 0, y = i; y <= table.length - 1; j++, y++) {
                previousSequence = currentSequence;
                temp = temp+ Integer.toString(table[j][y]);
                currentSequence = table[j][y];

                if(foundSequence && (currentSequence != previousSequence+1) && currentSequence == 0){
                    foundSequence = false;

                    if(!words.contains(word) && word.length() >= filterNum)
                        words.add(word);

                    word = "";

                }
                else if(foundSequence && y == table.length - 1) {
                    foundSequence = false;
                    word += s.charAt(j);

                    if(!words.contains(word) && word.length() >= filterNum)
                        words.add(word);

                    word = "";
                }
                else if(foundSequence) {
                    word += s.charAt(j);
                }
                else if(currentSequence == previousSequence+1) {
                    word += s.charAt(j);
                    foundSequence = true;
                }
            }
            //System.out.println(temp);
        }



        //Removing duplicates (duplicates include substrings of strings in the arraylist)
        ArrayList<String> filteredSimilarities = (ArrayList<String>) words.clone();

        for(String sequence1: words) {
            for(String sequence2: words) {
                if(!sequence1.equals(sequence2) && sequence1.contains(sequence2)) {
                    filteredSimilarities.remove(sequence2);
                }
            }
        }


        //Printing out result
        for(String word: filteredSimilarities) {
            System.out.println(word);
        }


        return filteredSimilarities;
    }

}

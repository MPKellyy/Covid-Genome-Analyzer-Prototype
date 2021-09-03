import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        //Setting up ArrayList
        ArrayList<String> wordList = new ArrayList<String>();
        String word1 = "salmon";
        wordList.add(word1);
//        String word2 = "salmin";
//        wordList.add(word2);
//        String word3 = "solman";
//        wordList.add(word3);
//        String word4 = "silmen";
//        wordList.add(word4);

        VariantGroup test = new VariantGroup();
        test.addGenomes(wordList);

//        int index = 0;
//        for(Character letter: test.generateSimilaritySequence()) {
//            System.out.println(letter + ": " + index);
//            index++;
//        }

        //System.out.println(test.getUnfilteredSimString());


        Map<Character, Integer> filteredSequence = test.getFilteredSimMap();
        for(Character key: filteredSequence.keySet()) {
            //System.out.println(key + ": " + filteredSequence.get(key));
        }


        //TODO: Generate Map of Pair of index locations as value and consecutive Strings as value


    }
}

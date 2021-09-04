import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        //Setting up ArrayLists
        ArrayList<String> wordList = new ArrayList<String>();
        String word1 = "-almon";
        wordList.add(word1);
        String word2 = "-almin";
        wordList.add(word2);
        String word3 = "-olmon";
        wordList.add(word3);
        String word4 = "-ilmon";
        wordList.add(word4);

        //Setting VariantGroup
        VariantGroup test = new VariantGroup();
        test.addGenomes(wordList);


        //Verifying Results
        Map<String, String> similarities = test.findSimilaritiesOrdered();

        for(String key: similarities.keySet()) {
            System.out.println(similarities.get(key) + ": " + key);
        }

        //TODO: Be able to handle strings of different lengths
        //TODO: Read strings from a textfile
        //TODO: Find differences?
        //TODO: Look for differences in similarities

    }
}

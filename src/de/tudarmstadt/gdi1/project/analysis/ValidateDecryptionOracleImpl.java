package de.tudarmstadt.gdi1.project.analysis;

import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;
import de.tudarmstadt.gdi1.project.alphabet.DistributionImpl;

/**
 * Created by Nansu on 26.02.14.
 */
public class ValidateDecryptionOracleImpl implements ValidateDecryptionOracle {

    protected Distribution distribution;
    protected Dictionary dictionary;



    public ValidateDecryptionOracleImpl(Distribution distribution, Dictionary dictionary){
            this.distribution = distribution;
            this.dictionary = dictionary;
    }


    /**
     * Given a plaintext this method returns true, if that plaintext is the
     * "correct" plaintext. Depending on the implementation the definition of
     * "correct" varies. A "cheating" oracle might know the plaintext one is
     * after. This is great for testing and debugging. An actual implementation
     * would need to make use of a {@link Distribution} and a {@link Dictionary}
     * to determine whether a plaintext is "good".
     *
     * @param plaintext the plaintext to test
     * @return true if the plaintext is the correct plaintext
     */
    @Override
    public boolean isCorrect(String plaintext) {
        int leastWord;                      // detects how many words should be correct in the dictionary, based on longest word in dictionary
        int rankRange = 1;                  // set to how many ranks of characters it should compare
        boolean result;
        String dummy = plaintext.replaceAll("//s+","");   // get rid of all spaces and similiar structures

        if (dummy.length() > longestWord(dictionary))
                     leastWord = (int) dummy.length() / longestWord(dictionary);   // depending on size of String, find appropiate number of correct words
        else leastWord = 1;                                                       // find at least 1 word

        result = checkAlphabet(dummy) & checkRank(dummy,rankRange) & (checkContent(dummy) >= leastWord);  // check for all

        return result;
    }

    /**
     * check for found words from the dictionary in the given text
     * @param text text to be checked
     * @return    number of found words
     */
    public int checkContent(String text){

        int x= 0;

        for ( int i = 0; i < dictionary.size();i++){   // run through all dictionary
            if (text.indexOf(dictionary.get(i)) != -1)  // check if current item in dictionary is in text
                x = x + 1 ;
        }

        return x;
    }

    /**
     * check if given text only contains characters valid to the alphabet
     * @param text text to be checked
     * @return   true if all characters are legit
     */
    public boolean checkAlphabet(String text){

        boolean result;

        result = dictionary.getAlphabet().allows(text) &    // check both alphabets of given dictionary and distribution
                 distribution.getAlphabet().allows(text);

        return result;
    }

    /**
     * check if the ranks of a character is the same of the distribution
     * @param text  given text to be checked
     * @param range how many ranks should be compared
     * @return      if the rankings match
     */
    public boolean checkRank(String text, int range){

        DistributionImpl stringDist = new DistributionImpl(distribution.getAlphabet(),text); // create distribution of text
        boolean result = true;

        for ( int i = 1; i <= range ; i++){                          // run to how many ranks should be checked
              char mostUsedDist = distribution.getByRank(1,i).charAt(0);
              char mostUsedString = stringDist.getByRank(1,i).charAt(0);

            result = result & ( mostUsedDist == mostUsedString);
        }
        return result;
    }

    /**
     * find longest word in given dictionary
     * @param dic   dictionary
     * @return      size of the longest word in the dictionary
     */
    public int longestWord(Dictionary dic){
        int result = 0;

        for ( int i = 0; i < dic.size(); i++){ // run through whole dictionary
            if (dic.get(i).length() > result)
                result = dic.get(i).length();  // enter the longer word from previous found
        }
        return result;
    }



}

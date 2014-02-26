package de.tudarmstadt.gdi1.project.analysis.vigenere;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;
import de.tudarmstadt.gdi1.project.alphabet.DistributionImpl;
import de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic.VigenereImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noxicon on 25.02.14.
 */
public class VigenereCryptanalysisImpl implements VigenereCryptanalysis{
    /**
     * Attack to determine all possible length of the used key based on a given
     * ciphertext.
     *
     * @param ciphertext the ciphertext
     * @return the possible key lengths (in ascending order)
     */
    @Override
    public List<Integer> knownCiphertextAttack(String ciphertext) {
        return getKeyLength(ciphertext);
    }

    @Override
    public String knownPlaintextAttack(String ciphertext, String plaintext, Alphabet alphabet) {
        return null;
    }

    /**
     * Attack to determine the used key based on a given cipher- and
     * (corresponding) plaintext and a given distribution on the alphabet.
     *
     * @param ciphertext   the ciphertext
     * @param plaintext    the corresponding plaintext
     * @param distribution the distribution
     * @return the key, a part of the key, or null
     */
    @Override
    public Object knownPlaintextAttack(String ciphertext, String plaintext, Distribution distribution) {
        return null;
    }

    /**
     * Attack to determine the used key based on a given cipher- and
     * (corresponding) plaintexts and a given distribution on the alphabet.
     *
     * @param ciphertext
     * @param plaintext
     * @param distribution The distribution
     * @param dictionary   @return the key, a part of the key, or null
     */
    @Override
    public Object knownPlaintextAttack(String ciphertext, String plaintext, Distribution distribution, Dictionary dictionary) {
        return null;
    }

    /**
     * Attack to determine the used key based on a given ciphertext, a given
     * distribution on the alphabet and a list of known plaintext cribs.
     *
     * @param ciphertext   the ciphertext
     * @param distribution the distribution
     * @param cribs        the list of substrings known to appear in the plaintext
     * @return the key, a part of the key, or null
     */
    @Override
    public String knownCiphertextAttack(String ciphertext, Distribution distribution, List<String> cribs) {
        List<Integer> keys = getKeyLength(ciphertext);
        String temp = "";
        String ret = "";


        for ( int i= 0; i < keys.size(); i++){   // run through all found keys
            ret = "";
            for ( int j = 1; j <= keys.get(i); j++){  // run through the size of the current key
                temp = extract(ciphertext,keys.get(i), j); // extracted chars dependent on the current size of key

                DistributionImpl keyDist = new DistributionImpl(distribution.getAlphabet(),temp);  // make distribution for extracted string
                char mostUsedPlain = distribution.getByRank(1, 1).charAt(0);                       // most used in given alphabet
                char mostUsedKey = keyDist.getByRank(1, 1).charAt(0);                              // most used in current extracted string

                int plain = distribution.getAlphabet().getIndex(mostUsedPlain);                 // get numbers to determine shift range
                int key = distribution.getAlphabet().getIndex(mostUsedKey);

                int shift = key - plain;
                char keyChar = distribution.getAlphabet().getChar(shift);                       // get code char

                ret = ret + keyChar;
            }

            VigenereImpl keyTest = new VigenereImpl(ret , distribution.getAlphabet());

            temp = keyTest.decrypt(ciphertext);                                              // decrypt with found pass code
            int x = 0;

            for ( int j = 0; j < cribs.size(); j++){    // run through cribs array size
                if ( temp.contains(cribs.get(j)) )      // check if string in current cribs is contained in decrypted string
                    x = x + 1;
            }

            if (x ==cribs.size())                     // pass code worked - return the code
                return ret;
        }
        return "";                                      // no valid pass code found
    }

    /**
     * extracts every nth character in a sequence of m  , of a String
     * @param m      sequence length
     * @param n      nth character to be extracted
     * @param input   String
     * @return      String of all the nth characters
     */
    public String extract ( String input, int m, int n){
        int x = 1;                                 // counter of sequence length
        String ret = "";

        for (int i = 0; i < input.length(); i++){  // run to length of string

            if (x == n)                            // counter in a sequence on the character to be extracted
                ret += input.charAt(i);

            if (x == m)                           // reset sequence counter
                x = 1;
            else
                x = x + 1;

        }
        return ret;
    }


    /**
     * calculates all divisors of a number
     * @param number
     * @return a List of dividends
     */
    public List<Integer> getDividends(int number){
        List<Integer> ret = new ArrayList<Integer>();
        for (int i = 1; i <= number ; i++) {   // checks all numbers starting from one to the number itself
            if ( number % i == 0)              // if mod 0 the current number is a divident
                ret.add(i);
        }
        return ret;
    }


    /**
     * calculates the ggT of 2 numbers
     * @param a
     * @param b
     * @return   ggT of a and b
     */
    public int ggT (int a, int b ){

        if (a == b)                           //euklid algorithm
             return(a);
        else
            if (a > b) return ggT(a-b,b);
            else       return ggT(b-a,a);

        }

    /**
     * returns the ggT of a List of numbers
     * @param inputList   list of numbers
     * @return  ggT of all the numbers
     */
    public int ggT ( List<Integer> inputList){
        if (inputList.size() < 2)                       // check if list is smaller 2
                return inputList.get(0);                // return item, only one in list
        else {
        int x = ggT(inputList.get(0),inputList.get(1)); // first pair
        for (int i = 2; i < inputList.size(); i++){     // skip first pair, run to rest of the list
             x = ggT(inputList.get(i), x);              // check ggT of previous pair with new item
        }

        return x;
        }
    }

    /**
     * calculates the distance between a multiple word sequence in a string
     * @param input   String
     * @param sequence  length of a sequence
     * @return a List of distances between same sequences
     */
    public List<Integer> getDistance ( String input, int sequence){
        List<Integer> ret = new ArrayList<Integer>();
        String temp;
        String split;

        for ( int i = 0; i < input.length() - sequence; i++){        // run through string length
            temp = input.substring(i,i+sequence);                    // contains the word to be checked
            split = input.substring(input.indexOf(temp)+ sequence ); // contains the rest of the string, cutting the search word
            if ( split.indexOf(temp) != -1)                          // word is contained -> add into result
                ret.add(split.indexOf(temp) + sequence);
        }
        return ret;
    }

    /**
     * returns possible key lengthes for a coded word
     * @param chiffre coded word
     * @return  possible key lengthes
     */
    public List<Integer> getKeyLength(String chiffre){
        List<Integer> ret = new ArrayList<Integer>();

        for (int i = 3; i <= chiffre.length(); i++){   // run from smallest reasonable keylength to the max wordcount
           ret.addAll(getDistance(chiffre,i));         // take all distances
        }
        ret=(getDividends(ggT(ret)));                  // calculate ggt of all distances and get all dividends of the ggt
        return ret;
    }


}

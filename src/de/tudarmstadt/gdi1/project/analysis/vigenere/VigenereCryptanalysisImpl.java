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

        // run through all found keys
        for ( int i= 0; i < keys.size(); i++){
            // setback ret , cause previous key was not valid
            ret = "";
            // run through the size of the current key
            for ( int j = 1; j <= keys.get(i); j++){
                // extracted chars dependent on the current key size
                temp = extract(ciphertext,keys.get(i), j);

                // make distribution for extracted string
                DistributionImpl keyDist = new DistributionImpl(distribution.getAlphabet(),temp);
                // most used in given alphabet
                char mostUsedPlain = distribution.getByRank(1, 1).charAt(0);
                // most used in current extracted string
                char mostUsedKey = keyDist.getByRank(1, 1).charAt(0);

                // get numbers to determine shift range
                int plain = distribution.getAlphabet().getIndex(mostUsedPlain);
                int key = distribution.getAlphabet().getIndex(mostUsedKey);
                // calculate shift amount
                int shift = key - plain;
                // get code char
                char keyChar = distribution.getAlphabet().getChar(shift);

                ret = ret + keyChar;
            }

            // create Vigenere object with found passcode string for current key
            VigenereImpl keyTest = new VigenereImpl(ret , distribution.getAlphabet());
            // decrypt with found pass code
            temp = keyTest.decrypt(ciphertext);
            int x = 0;

            // run through cribs array size
            for ( int j = 0; j < cribs.size(); j++){
                // check if string in current cribs is contained in decrypted string
                if ( temp.contains(cribs.get(j)) )
                    x = x + 1;
            }
            // pass code worked - return the code
            if (x ==cribs.size())
                return ret;
        }
        // no valid pass code found
        return "";
    }

    /**
     * extracts every nth character in a sequence of m  , of a String (cipher used)
     * @param m      sequence length
     * @param n      nth character to be extracted
     * @param input   String
     * @return      String of all the nth characters
     */
    public String extract ( String input, int m, int n){
        // counter of sequence length
        int x = 1;
        String ret = "";

        // run to length of string
        for (int i = 0; i < input.length(); i++){
            // counter in a sequence on the character to be extracted
            if (x == n)
                ret += input.charAt(i);

            // reset sequence counter - end of sequence reached
            if (x == m)
                x = 1;
            // continue sequence counter
            else
                x = x + 1;

        }
        return ret;
    }


    /**
     * calculates all divisors of a number( will take ggT)
     * @param number
     * @return a List of dividends
     */
    public List<Integer> getDividends(int number){
        List<Integer> ret = new ArrayList<Integer>();
        // checks all numbers starting from one to the number itself
        for (int i = 1; i <= number ; i++) {
            // if mod == 0 the current number is a divident
            if ( number % i == 0)
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
        // euklid algorithm
        if (a == b)
             return(a);
        else
            if (a > b) return ggT(a-b,b);
            else       return ggT(b-a,a);

        }

    /**
     * returns the ggT of a List of numbers ( takes the generated list all the distances )
     * @param inputList   list of numbers
     * @return  ggT of all the numbers
     */
    public int ggT ( List<Integer> inputList){
        // check if list is smaller 2
        if (inputList.size() < 2)
               // return item, only one in list
                return inputList.get(0);
        else {
        // first pair ggT
        int x = ggT(inputList.get(0),inputList.get(1));
        // skip first pair, run to rest of the list
        for (int i = 2; i < inputList.size(); i++){
            // check ggT of previous ggT  with new item
             x = ggT(inputList.get(i), x);
        }

        return x;
        }
    }

    /**
     * calculates all distances of multiple word sequences in a string ( takes cipher text)
     * @param input   String
     * @param sequence  length of a sequence
     * @return a List of distances between same sequences
     */
    public List<Integer> getDistance ( String input, int sequence){
        List<Integer> ret = new ArrayList<Integer>();
        String temp;
        String split;

        // run through string length
        for ( int i = 0; i < input.length() - sequence; i++){
            // contains the word to be checked
            temp = input.substring(i,i+sequence);
            // contains the rest of the string, cutting the search word and everything before it
            split = input.substring(input.indexOf(temp)+ sequence );
            // word is contained -> add into result
            if ( split.indexOf(temp) != -1)
                ret.add(split.indexOf(temp) + sequence);
        }
        return ret;
    }

    /**
     * returns possible key lengthes for a coded word
     * @param cipher coded word
     * @return  possible key lengthes
     */
    public List<Integer> getKeyLength(String cipher){
        List<Integer> ret = new ArrayList<Integer>();

        // run from smallest reasonable keylength to the max wordcount
        for (int i = 3; i <= cipher.length(); i++){
            // take all distances
           ret.addAll(getDistance(cipher,i));
        }
        // calculate ggt of all distances and get all dividends of the ggt
        ret=(getDividends(ggT(ret)));
        return ret;
    }


}

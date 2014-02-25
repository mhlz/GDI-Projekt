package de.tudarmstadt.gdi1.project.analysis.vigenere;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;

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
        return null;
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
        return null;
    }

    /**
     * calculates all divisors of a number
     * @param number
     * @return a List of dividends
     */
    public List<Integer> getDividends(int number){
        List<Integer> ret = new ArrayList<Integer>();
        for (int i = 1; i <= number ; i++) {
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

        if (a == b)
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
        if (inputList.size() < 2)
                return inputList.get(0);
        else {
        int x = ggT(inputList.get(0),inputList.get(1));
        for (int i = 2; i < inputList.size(); i++){
             x = ggT(inputList.get(i), x);
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

        for ( int i = 0; i < input.length() - sequence; i++){
            temp = input.substring(i,i+sequence);
            split = input.substring(input.indexOf(temp)+ sequence );
            if ( split.indexOf(temp) != -1)
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

        for (int i = 3; i <= chiffre.length(); i++){
           ret.addAll(getDistance(chiffre,i));
        }
        ret=(getDividends(ggT(ret)));
        return ret;
    }


}

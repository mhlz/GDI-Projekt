package de.tudarmstadt.gdi1.project.analysis.vigenere;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;

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
}

package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;
import de.tudarmstadt.gdi1.project.analysis.EncryptionOracle;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipher;

/**
 * Created by Hannes on 2/24/14.
 *
 * @author Hannes Güdelhöfer
 */
public class MonoalphabeticCpaNpaCryptanalysisImpl implements MonoalphabeticCpaNpaCryptanalysis {
    @Override
    public char[] knownPlaintextAttack(String ciphertext, String plaintext, Alphabet alphabet) {
        return new char[0];
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

    @Override
    public char[] chosenPlaintextAttack(EncryptionOracle<MonoalphabeticCipher> oracle, Alphabet alphabet) {
        return new char[0];
    }
}

package de.tudarmstadt.gdi1.project.analysis.caeser;

import de.tudarmstadt.gdi1.project.alphabet.*;
import de.tudarmstadt.gdi1.project.analysis.KnownCiphertextAnalysis;
import de.tudarmstadt.gdi1.project.analysis.KnownPlaintextAnalysis;

/**
 * Created by Hannes on 2/19/14.
 *
 * @author Hannes Güdelhöfer
 */
public class CaesarCryptanalysisImpl implements CaesarCryptanalysis, KnownCiphertextAnalysis, KnownPlaintextAnalysis {


    /**
     * Attack to determine the used key based on a given cipher- and
     * (corresponding) plaintext.
     *
     * @param ciphertext the ciphertext
     * @param plaintext  the corresponding plaintext
     * @param alphabet   the alphabet
     * @return the key, a part of the key, or null
     */
    @Override
    public Integer knownPlaintextAttack(String ciphertext, String plaintext, Alphabet alphabet) {
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
    public Integer knownPlaintextAttack(String ciphertext, String plaintext, Distribution distribution) {
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
    public Integer knownPlaintextAttack(String ciphertext, String plaintext, Distribution distribution, Dictionary dictionary) {
        return null;
    }


    /**
     * Attack to determine the used key based on a given ciphertext and a given
     * distribution on the alphabet.
     *
     *
     * @param ciphertext   the ciphertext
     * @param distribution the distribution
     * @return the key, a part of the key, or null
     */
    @Override
    public Integer knownCiphertextAttack(String ciphertext, Distribution distribution) {
        DistributionImpl cipherDist = new DistributionImpl(distribution.getAlphabet(), ciphertext);

        int testCommonLetters = 3;
        double ret = 0;

        //cycling to as many letters as specified
        for(int i = 0; i < testCommonLetters; i++) {
            String mostUsedCipher = cipherDist.getByRank(1, i + 1); //this is the most used letter
            String mostUsedPlain = distribution.getByRank(1, i + 1); //this is the most used letter

            // mostUsedPlain is the base letter in the alphabet
            int base = 0;
            for(Character c: distribution.getAlphabet().asCharArray()) {
                if(c.toString().equals(mostUsedPlain)) {
                    break;
                }
                base++;
            }

            // mostUsedCipher is the shift letter in the alphabet
            int shift = 0;
            for(Character c: cipherDist.getAlphabet().asCharArray()) {
                if(c.toString().equals(mostUsedCipher)) {
                    break;
                }
                shift++;
            }

            // factoring each iteration, the first letter has a factor of 1, the second 9, third 27, ...
            // this is to make sure, that the first one is the importants one, maybee we should go to x^2 here
            // shift - base is the amount which the alphabet got shifted
            double fakt = (testCommonLetters * testCommonLetters * testCommonLetters) / ( (testCommonLetters - i) * (testCommonLetters - i) * (testCommonLetters - i) );
            ret += (double)(shift - base) / fakt;
        }
        return (int) (ret + 0.5);
    }

    /**
     * Attack to determine the used key based on a ciphertext and a given
     * dictionary.
     *
     *
     * @param ciphertext the ciphertext
     * @param dictionary the dictionary
     * @return the key, a part of the key, or null
     */
    @Override
    public Integer knownCiphertextAttack(String ciphertext, Dictionary dictionary) {
        return null;
    }

    /**
     * Attack to determine the used key based on a given ciphertext and a given
     * distribution on the alphabet.
     *
     * @param ciphertext   the ciphertext
     * @param distribution
     * @param dict         the dictionary containing all used words in the plaintext  @return the key, a part of the key, or null
     */
    @Override
    public Integer knownCiphertextAttack(String ciphertext, Distribution distribution, Dictionary dict) {
        return null;
    }
}

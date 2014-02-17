package de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipherImpl;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by reckter on 2/17/14.
 */
public class MonoalphabeticCipherImpl extends SubstitutionCipherImpl implements MonoalphabeticCipher {
    protected Alphabet source;
    protected Alphabet destination;

    public MonoalphabeticCipherImpl(Alphabet source, Alphabet destination) {
        this.source = source;
        this.destination = destination;
    }

    /**
     * Translates the given character that is on the given position in the text
     * into its encrypted equivalent.
     *
     * @param chr the character that needs to be translated
     * @param i   the position the character stands in the text
     * @return the translated/encrypted character
     */
    @Override
    public char translate(char chr, int i) {
        return destination.getChar(source.getIndex(chr));
    }

    /**
     * translates the given character that is on the given position in the text
     * back into its decrypted equivalent
     *
     * @param chr the character that needs to be reverse translated
     * @param i   the position of the character in the text
     * @return the reversetranslated/decrypted character
     */
    @Override
    public char reverseTranslate(char chr, int i) {
        return  source.getChar(destination.getIndex(chr));
    }
}

package de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

/**
 * Created by reckter on 2/17/14.
 */
public class CaesarImpl extends MonoalphabeticCipherImpl implements Caesar {

    public CaesarImpl(int key, Alphabet source) {
        super(source, new UtilsImpl().shiftAlphabet(source, key));
    }
}

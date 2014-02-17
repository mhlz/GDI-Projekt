package de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;

/**
 * Created by reckter on 2/17/14.
 */
public class CaesarImpl extends MonoalphabeticCipherImpl implements Caesar {

    public CaesarImpl(int key, Alphabet source) {
        super(source, source);
        char[] tempArray = source.asCharArray();
        Character[] destArray = new Character[tempArray.length];

        for(int i = key; i < tempArray.length; i++){
            destArray[i - key] = tempArray[i];
        }
        for(int i = 0; i < key; i++){
            destArray[tempArray.length - key + i] = tempArray[i];
        }
        this.destination = new AlphabetImpl(destArray);
    }
}

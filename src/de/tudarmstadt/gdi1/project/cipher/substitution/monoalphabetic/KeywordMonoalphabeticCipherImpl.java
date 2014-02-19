package de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;
import sun.print.resources.serviceui;

import java.util.ArrayList;

/**
 * Created by Hannes on 2/17/14.
 *
 * @author Hannes Güdelhöfer
 */
public class KeywordMonoalphabeticCipherImpl extends MonoalphabeticCipherImpl implements KeywordMonoalphabeticCipher {

	/**
	 *
	 * @param password
	 * @param alphabet
	 */
    public KeywordMonoalphabeticCipherImpl(String password, Alphabet alphabet) {
        super(alphabet, alphabet);

        char[] sourceArray = alphabet.asCharArray();
        ArrayList<Character> passList = new ArrayList<Character>();

        ArrayList<Character> restAlphabet = new ArrayList<Character>();

        for(Character c: password.toCharArray()) {
            if(!passList.contains(c)) {
                passList.add(c);
            }
        }

        for(Character c: sourceArray) {
            if(!passList.contains(c)) {
                restAlphabet.add(c);
            }
        }

        for(Character c: alphabet.asCharArray()) {
            if(!passList.contains(c)) {
                passList.add(c);
            }
        }

        Character[] destArray = new Character[sourceArray.length];

        for(int i = 0; i < passList.size(); i++) {
            destArray[i] = passList.get(i);
        }

        for(int i = sourceArray.length - 1; i >= password.length(); i--) {
            destArray[i] = restAlphabet.get(sourceArray.length - i - 1);
        }

        this.destination = new AlphabetImpl(destArray);

    }
}

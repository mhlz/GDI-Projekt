package de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.ArrayList;

/**
 * a keyword monoalphabtic cipher
 */
public class KeywordMonoalphabeticCipherImpl extends MonoalphabeticCipherImpl implements KeywordMonoalphabeticCipher {

	/**
	 * Creates a new Keyword-Cipher
	 *
	 * @param password the word, that will be used for the beginning of the destination Alphabet
	 * @param alphabet the source alphabet
	 */
	public KeywordMonoalphabeticCipherImpl(String password, Alphabet alphabet) {
		super(alphabet, alphabet);

		char[] sourceArray = alphabet.asCharArray();
		ArrayList<Character> passList = new ArrayList<Character>();

		ArrayList<Character> restAlphabet = new ArrayList<Character>();

		// we delete all duplicates out of the word, so that passList only contains each letter once
		for(Character c : password.toCharArray()) {
			if(!passList.contains(c)) {
				passList.add(c);
			}
		}

		// we compute the rest of the alphabet, so each letter that isn't in passList, is in here
		for(Character c : sourceArray) {
			if(!passList.contains(c)) {
				restAlphabet.add(c);
			}
		}


		Character[] destArray = new Character[sourceArray.length];
		// putting the password into the destinationArray
		for(int i = 0; i < passList.size(); i++) {
			destArray[i] = passList.get(i);
		}

		// reversing the alphabet
		Alphabet reversAlph = new UtilsImpl().reverseAlphabet(new AlphabetImpl(restAlphabet));

		//putting the reveres Alphabet into the destination array
		for(int i = passList.size(); i < destArray.length; i++) {
			destArray[i] = reversAlph.getChar(i - passList.size());
		}

		//finaly setting the destination to the right alphabet
		this.destination = new AlphabetImpl(destArray);

	}
}

package de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.utils.Utils;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.ArrayList;

/**
 * @author Mischa Holz
 */
public class VigenereImpl extends PolyalphabeticCipherImpl implements Vigenere {

	/**
	 * Construct a new Vigenere encryption
	 * @param key Keyword for the encryption
	 * @param alphabet Source alphabet
	 */
	public VigenereImpl(String key, Alphabet alphabet) {
		// create the super class with the protected default constructor
		super();

		// fill the ciphertextarray by shifiting the source alphabet according to the position
		// of the character in the key
		ArrayList<Alphabet> vigenereAlphabets = new ArrayList<Alphabet>();
		Utils utils = new UtilsImpl();
		for(Character c : key.toCharArray()) {
			vigenereAlphabets.add(utils.shiftAlphabet(alphabet, alphabet.getIndex(c)));
		}

		// assign the plain text alphabet and the cphertextalphabets
		this.plaintTextAlphabet = alphabet;
		this.cipherTextAlphabets = vigenereAlphabets.toArray(new Alphabet[vigenereAlphabets.size()]);
	}
}

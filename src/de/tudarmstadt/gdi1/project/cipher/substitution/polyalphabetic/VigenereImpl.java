package de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.utils.Utils;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.ArrayList;

/**
 * @author Mischa Holz
 */
public class VigenereImpl extends PolyalphabeticCipherImpl implements Vigenere {

	public VigenereImpl(String key, Alphabet alphabet) {
		super();

		ArrayList<Alphabet> vigenereAlphabets = new ArrayList<Alphabet>();
		Utils utils = new UtilsImpl();
		for(Character c : key.toCharArray()) {
			vigenereAlphabets.add(utils.shiftAlphabet(alphabet, alphabet.getIndex(c)));
		}

		this.plaintTextAlphabet = alphabet;
		this.cipherTextAlphabets = vigenereAlphabets.toArray(new Alphabet[vigenereAlphabets.size()]);
	}
}

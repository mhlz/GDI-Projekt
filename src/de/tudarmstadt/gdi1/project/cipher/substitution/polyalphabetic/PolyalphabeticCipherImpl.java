package de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipherImpl;
import de.tudarmstadt.gdi1.project.exception.InvalidAlphabetListException;
import de.tudarmstadt.gdi1.project.exception.InvalidCharacterException;

/**
 * @author Mischa Holz
 */
public class PolyalphabeticCipherImpl extends SubstitutionCipherImpl implements PolyalphabeticCipher {

	protected Alphabet plaintTextAlphabet;
	protected Alphabet[] cipherTextAlphabets;

	protected PolyalphabeticCipherImpl() {

	}

	public PolyalphabeticCipherImpl(Alphabet plaintTextAlphabet, Alphabet[] cipherTextAlphabets) {
		if(cipherTextAlphabets.length == 0) {
			throw new InvalidAlphabetListException("The list of alphabets must include at least one alphabet!");
		}
		this.plaintTextAlphabet = plaintTextAlphabet;
		this.cipherTextAlphabets = cipherTextAlphabets;
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
		i %= cipherTextAlphabets.length;
		int plainPos = plaintTextAlphabet.getIndex(chr);
		if(i == -1) {
			throw new InvalidCharacterException("The character '" + chr + "' is not part of the plain text alphabet!");
		}
		return cipherTextAlphabets[i].getChar(plainPos);
	}

	/**
	 * translates the given character that is on the given position in the text
	 * back into its decrypted equivalent
	 *
	 * @param chr the character that needs to be reversetranslated
	 * @param i   the position of the character in the text
	 * @return the reversetranslated/decrypted character
	 */
	@Override
	public char reverseTranslate(char chr, int i) {
		i %= cipherTextAlphabets.length;
		int plainPos = cipherTextAlphabets[i].getIndex(chr);
		if(i == -1) {
			throw new InvalidCharacterException("The character '" + chr + "' is not part of the cipher text alphabet!");
		}
		return plaintTextAlphabet.getChar(plainPos);
	}
}

package de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipherImpl;
import de.tudarmstadt.gdi1.project.exception.InvalidAlphabetListException;
import de.tudarmstadt.gdi1.project.exception.InvalidCharacterException;

/**
 * a basic polyalphabetic cipher
 *
 * @author Mischa Holz
 */
public class PolyalphabeticCipherImpl extends SubstitutionCipherImpl implements PolyalphabeticCipher {

	/**
	 * the plain text alphabet
	 */
	protected Alphabet plaintTextAlphabet;

	/**
	 * all the cipher text aphabets
	 */
	protected Alphabet[] cipherTextAlphabets;

	/**
	 * Default constructor to be used in case an implementation needs to calculate its own alphabets
	 */
	protected PolyalphabeticCipherImpl() {
		// this function is used in case an implementation needs to calculate its own alphabets
	}

	/**
	 * Create a new Polyalhpabetic cipher
	 *
	 * @param plaintTextAlphabet  Alphabet of the plain text
	 * @param cipherTextAlphabets Alphabets of the cipher text
	 */
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
	 * Every character gets translated using the target alphabet at the position of the character.
	 *
	 * @param chr the character that needs to be translated
	 * @param i   the position the character stands in the text
	 * @return the translated/encrypted character
	 */
	@Override
	public char translate(char chr, int i) {
		// calculate the correct ciphertext alphabet
		i %= cipherTextAlphabets.length;
		// get the position of the plain text character that is about to be translated
		int plainPos = plaintTextAlphabet.getIndex(chr);
		if(i == -1) {
			throw new InvalidCharacterException("The character '" + chr + "' is not part of the plain text alphabet!");
		}
		// translate the character into the appropriate cipher text alphabet
		return cipherTextAlphabets[i].getChar(plainPos);
	}

	/**
	 * translates the given character that is on the given position in the text
	 * back into its decrypted equivalent.
	 * Every character gets translated using the target alphabet at the position of the character.
	 *
	 * @param chr the character that needs to be reversetranslated
	 * @param i   the position of the character in the text
	 * @return the reversetranslated/decrypted character
	 */
	@Override
	public char reverseTranslate(char chr, int i) {
		// calculate the correct ciphertext alphabet
		i %= cipherTextAlphabets.length;
		// get the position of the cipher text character that is about to be translated
		int cipherPos = cipherTextAlphabets[i].getIndex(chr);
		if(i == -1) {
			throw new InvalidCharacterException("The character '" + chr + "' is not part of the cipher text alphabet!");
		}
		// translate the character into the appropriate plain text alphabet
		return plaintTextAlphabet.getChar(cipherPos);
	}
}

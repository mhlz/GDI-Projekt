package de.tudarmstadt.gdi1.project.cipher.enigma;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.exception.InvalidAlphabetException;

/**
 * Represents the reverse rotor of an Enigma machine
 *
 * @author Mischa Holz
 */
public class ReverseRotorImpl implements ReverseRotor {

	/**
	 * Input alphabet
	 */
	protected Alphabet in;

	/**
	 * output alphabet
	 */
	protected Alphabet out;

	/**
	 * Constructs a reverserotor. The alphabets have to be the same size and no letter can be translated to itself.
	 * @param entryAlph input alphabet
	 * @param exitAlph output alphabet
	 */
	public ReverseRotorImpl(Alphabet entryAlph, Alphabet exitAlph) {
		// check for same size
		if(entryAlph.size() != exitAlph.size()) {
			throw new InvalidAlphabetException("Both alphabets must be the same size!");
		}

		// make sure that no letter is translated to itself
		for(int i = 0; i < entryAlph.size(); i++) {
			if(entryAlph.getChar(i) == exitAlph.getChar(i)) {
				throw new InvalidAlphabetException("The letter '" + entryAlph.getChar(i) + "' can't be translated to itself!");
			}
		}

		// assign properties
		this.in = entryAlph;
		this.out = exitAlph;

		// Check for symmetrical alphabets. Translating twice should result in the same character if the alphabets are symmetrical
		for(Character c : in) {
			if(c != this.translate(this.translate(c))) {
				throw new InvalidAlphabetException("The alphabets have to be symmetrical!");
			}
		}
	}

	/**
	 * passes the given character through the ReverseRotor of an enigma.
	 *
	 * @param c the character that should be encrypted
	 * @return the encrypted character
	 */
	@Override
	public char translate(char c) {
		// pretty straightforward translating. Just return corresponding output letter from the input alphabet
		return out.getChar(in.getIndex(c));
	}
}

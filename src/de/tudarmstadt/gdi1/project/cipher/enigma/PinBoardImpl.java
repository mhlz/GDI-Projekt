package de.tudarmstadt.gdi1.project.cipher.enigma;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.exception.InvalidAlphabetException;

/**
 * Represents the pinboard of an Enigma machine
 *
 * @author Mischa Holz
 */
public class PinBoardImpl implements PinBoard {

	/**
	 * Input alphabet
	 */
	protected Alphabet in;

	/**
	 * output alphabet
	 */
	protected Alphabet out;

	/**
	 * Creates a PinBoard.
	 *
	 * @param source      Source alphabet
	 * @param destination Destination alphabet
	 */
	public PinBoardImpl(Alphabet source, Alphabet destination) {
		// check for length
		if(source.size() != destination.size()) {
			throw new InvalidAlphabetException("Both alphabets must have the same length!");
		}

		// assign properties
		in = source;
		out = destination;

		// Check for symmetrical alphabets. Translating twice should result in the same character if the alphabets are symmetrical
		for(Character c : in) {
			if(c != this.translate(this.translate(c))) {
				throw new InvalidAlphabetException("The alphabets have to be symmetrical!");
			}
		}
	}

	/**
	 * passes the given character through the pinboard.
	 *
	 * @param c the character that should be passed through the pinboard.
	 * @return The translated Character at the end of the pinboard
	 */
	@Override
	public char translate(char c) {
		// translating by getting the corresponding letter from the output alphabet
		return out.getChar(in.getIndex(c));
	}
}

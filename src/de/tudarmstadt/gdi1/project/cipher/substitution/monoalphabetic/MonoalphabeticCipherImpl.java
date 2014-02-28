package de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipherImpl;
import de.tudarmstadt.gdi1.project.exception.InvalidCharacterException;

/**
 * a basic mono alphabetic cipher
 *
 * @author Hannes
 */
public class MonoalphabeticCipherImpl extends SubstitutionCipherImpl implements MonoalphabeticCipher {

	/**
	 * the source alphabet
	 */
	protected Alphabet source;

	/**
	 * the cipher alphabet
	 */
	protected Alphabet destination;

	/**
	 * Default constructor to create a new monoalphabetic cipher
	 *
	 * @param source      Source alphabet
	 * @param destination Target alphabet
	 */
	public MonoalphabeticCipherImpl(Alphabet source, Alphabet destination) {
		this.source = source;
		this.destination = destination;
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
		if(!source.contains(chr)) {
			throw new InvalidCharacterException("can't encrypt the character '" + chr + "', because it isn't in the alphabet provided");
		}
		return destination.getChar(source.getIndex(chr));
	}

	/**
	 * translates the given character that is on the given position in the text
	 * back into its decrypted equivalent
	 *
	 * @param chr the character that needs to be reverse translated
	 * @param i   the position of the character in the text
	 * @return the reversetranslated/decrypted character
	 */
	@Override
	public char reverseTranslate(char chr, int i) {
		if(!destination.contains(chr)) {
			throw new InvalidCharacterException("can't decrypt the character '" + chr + "', because it isn't in the alphabet provided");
		}
		return source.getChar(destination.getIndex(chr));
	}
}

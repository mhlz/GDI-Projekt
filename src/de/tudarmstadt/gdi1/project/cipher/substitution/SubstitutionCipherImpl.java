package de.tudarmstadt.gdi1.project.cipher.substitution;


/**
 * basic Substituion cipher
 */
public abstract class SubstitutionCipherImpl implements SubstitutionCipher {


	/**
	 * Encrypt a text according to the encryption method of the cipher
	 *
	 * @param text the plaintext to encrypt
	 * @return the encrypted plaintext (=ciphertext)
	 */
	@Override
	public final String encrypt(String text) {
		// translate every character individually and puts them together to a string
		StringBuilder out = new StringBuilder();
		char[] textArray = text.toCharArray();
		for(int i = 0; i < textArray.length; i++) {
			out.append(this.translate(textArray[i], i));
		}

		return out.toString();
	}

	/**
	 * Decrypt a text according to the decryption method of the cipher
	 *
	 * @param text the ciphertext to decrypt
	 * @return the decrypted ciphertext (=plaintext)
	 */
	@Override
	public final String decrypt(String text) {
		// reverseTranslate every character individually and puts them together to a string
		StringBuilder out = new StringBuilder();
		char[] textArray = text.toCharArray();
		for(int i = 0; i < textArray.length; i++) {
			out.append(this.reverseTranslate(textArray[i], i));
		}
		return out.toString();
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
	public abstract char translate(char chr, int i);

	/**
	 * translates the given character that is on the given position in the text
	 * back into its decrypted equivalent
	 *
	 * @param chr the character that needs to be reversetranslated
	 * @param i   the position of the character in the text
	 * @return the reversetranslated/decrypted character
	 */
	@Override
	public abstract char reverseTranslate(char chr, int i);
}

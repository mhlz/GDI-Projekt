package de.tudarmstadt.gdi1.project.cipher.enigma;

import java.util.List;

/**
 * Implements an Enigma machine
 *
 */
public class EnigmaImpl implements Enigma {

	/**
	 * Rotors
	 */
	protected List<Rotor> rotors;

	/**
	 * PinBoard
	 */
	protected PinBoard pinBoard;

	/**
	 * Reverse rotor
	 */
	protected ReverseRotor reverseRotor;

	/**
	 * Constructs an Enigma machine with the given rotors, pinboard and reverserotor
	 *
	 * @param rotors       Rotor parts of the Enigma
	 * @param pinboard     PinBoard of the Enigma
	 * @param reverseRotor Reverse rotor of the Enigma
	 */
	public EnigmaImpl(List<Rotor> rotors, PinBoard pinboard, ReverseRotor reverseRotor) {
		this.rotors = rotors;
		this.pinBoard = pinboard;
		this.reverseRotor = reverseRotor;
	}

	/**
	 * Encrypt a text according to the encryption method of the cipher
	 *
	 * @param text the plaintext to encrypt
	 * @return the encrypted plaintext (=ciphertext)
	 */
	@Override
	public String encrypt(String text) {
		// string builder for better performance
		StringBuilder ret = new StringBuilder();

		// translate every character individually
		for(char c : text.toCharArray()) {
			// go through the pin board
			char cAfterPin = pinBoard.translate(c);
			// go forwards through the rotors
			for(Rotor r : rotors) {
				cAfterPin = r.translate(c, true);
			}
			// goes through the reverserotor
			char cAfterReverse = reverseRotor.translate(cAfterPin);
			// backwards through the rotors
			for(int i = rotors.size() - 1; i >= 0; i--) {
				cAfterReverse = rotors.get(i).translate(cAfterReverse, false);
			}

			// append string builder
			ret.append(pinBoard.translate(cAfterReverse));

			// move rotors. Only move the next one if the previous one made a full rotation
			for(Rotor rotor : rotors) {
				if(!rotor.rotate()) { // break unless the rotor just returned to its starting position
					break;
				}
			}
		}

		// after encrypting/decrypting a text, reset all rotors
		for(Rotor rotor : rotors) {
			rotor.reset();
		}

		return ret.toString();
	}

	/**
	 * Decrypt a text according to the decryption method of the cipher
	 *
	 * @param text the ciphertext to decrypt
	 * @return the decrypted ciphertext (=plaintext)
	 */
	@Override
	public String decrypt(String text) {
		// Encryption and decryption follow the same algorithm
		return this.encrypt(text);
	}
}

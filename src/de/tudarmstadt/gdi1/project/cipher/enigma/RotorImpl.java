package de.tudarmstadt.gdi1.project.cipher.enigma;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;
import de.tudarmstadt.gdi1.project.exception.InvalidAlphabetException;

import java.util.ArrayList;

/**
 * Implements a configurable rotor of the Enigma machine
 *
 */
public class RotorImpl implements Rotor {

	/**
	 * input alphabet
	 */
	protected Alphabet in;

	/**
	 * Current output alphabet
	 */
	protected Alphabet out;

	/**
	 * Current shift amount in the input alphabet for every letter
	 */
	protected ArrayList<Integer> shift;

	/**
	 * Current rotation status
	 */
	protected int rotationStatus;

	/**
	 * The number of rotations this rotor started at
	 */
	protected int startRotation;

	/**
	 * Constructs a rotor. Every letter in the input alphabet has to be translated to the output alphabet.
	 *
	 * @param in            Input alphabet
	 * @param out           Output alphabet
	 * @param startPosition Starting position of the rotor
	 */
	public RotorImpl(Alphabet in, Alphabet out, int startPosition) {
		// check for same length
		if(in.size() != out.size()) {
			throw new InvalidAlphabetException("Both alphabets must have the same length!");
		}

		// assign properties
		this.in = in;
		this.out = out;
		this.rotationStatus = 0;
		this.startRotation = startPosition;

		// calculate the difference between every letter in the input and output alphabet and save that in the shift array
		this.shift = new ArrayList<Integer>();
		for(int i = 0; i < in.size(); i++) {
			this.shift.add(in.getIndex(out.getChar(i)) - i);
		}
		// rotate the rotor the requested amount of times
		for(int i = 0; i < startPosition; i++) {
			this.rotate();
		}
	}

	/**
	 * passes a given character through the rotor of an enigma.
	 *
	 * @param c       the character that should be passed through the rotor
	 * @param forward true if we pass the character forward through the rotor.
	 *                Should be true before the ReverseRotor has been passed and
	 *                false afterwards.
	 * @return the translated character.
	 */
	@Override
	public char translate(char c, boolean forward) {
		// return the matching character from the input and output alphabets
		// pretty straightforward since we calculate the output alphabet in every rotation
		if(forward) {
			return out.getChar(in.getIndex(c));
		} else {
			return in.getChar(out.getIndex(c));
		}
	}

	/**
	 * rotates the rotor to its next position.
	 *
	 * @return true if the rotor reached is intial position (i.e., the next
	 * rotor has to be rotated), otherwise false
	 */
	@Override
	public boolean rotate() {
		// initialize a new array
		ArrayList<Integer> newShift = new ArrayList<Integer>();

		// rotate the array
		// new position 0 is loaded from old position 1 and so on...
		int position = 1;
		for(Integer i : shift) {
			newShift.add(shift.get(position % shift.size()));
			position++;
		}

		// add to the rotation status and put it back inbounds if necessary
		rotationStatus++;
		rotationStatus %= shift.size();

		// assign the shift property
		shift = newShift;

		// calculate the new output alphabet
		String newAlphabet = "";
		for(int i = 0; i < in.size(); i++) {
			// shift the index according to the shift array
			int index = i;
			index += shift.get(index);
			// put the index back inbounds if necessary
			index %= shift.size();
			if(index < 0) {
				index += shift.size();
			}
			// add the new character to the alphabet
			newAlphabet += in.getChar(index);
		}
		// create the alphabet from the string
		out = new AlphabetImpl(newAlphabet);

		// if this reached the starting position again, return true
		return rotationStatus == this.startRotation;
	}

	/**
	 * resets the rotor to its default position
	 */
	@Override
	public void reset() {
		// rotate until this reaches the starting position
		while(!rotate()) {
		}
	}
}

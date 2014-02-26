package de.tudarmstadt.gdi1.project.cipher.enigma;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Mischa Holz
 */
public class RotorImpl implements Rotor {

	protected Alphabet in;
	protected Alphabet out;

	protected ArrayList<Integer> shift;
	protected int shiftAmount;

	public RotorImpl(Alphabet in, Alphabet out) {
		this.in = in;
		this.out = out;
		shiftAmount = 0;

		shift = new ArrayList<Integer>();
		for(int i = 0; i < in.size(); i++) {
			shift.add(in.getIndex(out.getChar(i)) - i);
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
		if(forward) {
			int index = in.getIndex(c);
			index += shift.get(index);
			index %= in.size();
			if(index < 0) {
				index += in.size();
			}
			return in.getChar(index);
		} else {
			int index = in.getIndex(c);
			index -= shift.get(index);
			index %= in.size();
			if(index < 0) {
				index += in.size();
			}
			return in.getChar(index);
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
		ArrayList<Integer> newShift = new ArrayList<Integer>();
		int position = 1;
		for(Integer i : shift) {
			newShift.add(shift.get(position % shift.size()));
			position++;
		}

		shiftAmount++;
		shiftAmount %= shift.size();

		shift = newShift;
		return shiftAmount == 0;
	}

	/**
	 * resets the rotor to its default position
	 */
	@Override
	public void reset() {
		while(!rotate()) {}
	}
}

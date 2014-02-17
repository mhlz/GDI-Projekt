package de.tudarmstadt.gdi1.project.alphabet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Mischa Holz
 */
public class AlphabetImpl implements Alphabet {

	protected Set<Character> characters;

	public AlphabetImpl() {
		characters = new HashSet<Character>();
	}

	/**
	 * Searches for a character in the alphabet
	 *
	 * @param chr the character to find
	 * @return the position of given character in the alphabet (0-based), or -1
	 * if character is not in alphabet
	 */
	@Override
	public int getIndex(char chr) {
		return 0;
	}

	/**
	 * Retrieves a character from the alphabet
	 *
	 * @param index the position (0-based) of the character to retrieve
	 * @return the character on position index in the alphabet
	 */
	@Override
	public char getChar(int index) {
		return 0;
	}

	/**
	 * @return the number of characters in the alphabet
	 */
	@Override
	public int size() {
		return 0;
	}

	/**
	 * Checks if the given character is in the alphabet
	 *
	 * @param chr the character that should be checked
	 * @return true if the character is in the alphabet
	 */
	@Override
	public boolean contains(char chr) {
		return false;
	}

	/**
	 * Checks if the given string contains only characters that are allowed in
	 * the alphabet
	 *
	 * @param word the string that should be checked
	 * @return true if the word contains only allowed characters
	 */
	@Override
	public boolean allows(String word) {
		return false;
	}

	/**
	 * Normalizes the given string. This means deleting all the characters that
	 * are not part of the alphabet.
	 *
	 * @param input the string that should be normalized.
	 * @return the normalized string
	 */
	@Override
	public String normalize(String input) {
		return null;
	}

	/**
	 * Returns the underlying characters in correct order as a char array of
	 * size {@link #size()}
	 *
	 * @return a char array representing the alphabet
	 */
	@Override
	public char[] asCharArray() {
		return new char[0];
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Character> iterator() {
		return null;
	}
}

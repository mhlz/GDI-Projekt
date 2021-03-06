package de.tudarmstadt.gdi1.project.alphabet;

import de.tudarmstadt.gdi1.project.exception.InvalidCharacterException;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a cryptographic alphabet
 *
 */
public class AlphabetImpl implements Alphabet {

	/**
	 * Saves the characters of the array
	 */
	protected ArrayList<Character> characters;

	/**
	 * Default constructor of an empty alphabet
	 */
	public AlphabetImpl() {
		characters = new ArrayList<Character>();
	}

	/**
	 * Create an alphabet by using a collection of characters
	 *
	 * @param characters Alphabet collection
	 */
	public AlphabetImpl(Collection<Character> characters) {
		this(characters.toArray(new Character[characters.size()]));
	}

	/**
	 * Create an alphabet by using a character array
	 *
	 * @param characters Array of characters that make up the alphabet
	 */
	public AlphabetImpl(Character[] characters) {
		this();
		for(char c : characters) {
			if(this.characters.contains(c)) {
				throw new InvalidCharacterException("The character '" + c + "' exists twice!");
			}
			this.characters.add(c);
		}
	}

	/**
	 * Create an alphabet with characters from a string
	 *
	 * @param characters String containing the characters for the alphabet
	 */
	public AlphabetImpl(String characters) {
		this((new UtilsImpl()).toCharacterArray(characters.toCharArray()));
	}

	/**
	 * Checks to see if this Alphabet is equal to another
	 *
	 * @param obj other alphabet
	 * @return boolean if both contain the same characters in the same order
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Alphabet) {
			Alphabet other = (Alphabet) obj;
			// checks if the sizes are the same
			if(other.size() != this.size()) {
				return false;
			}

			// checks if every letter is on the same spot in the other alphabet
			for(int i = 0; i < this.size(); i++) {
				if(other.getChar(i) != this.getChar(i)) {
					return false;
				}
			}

			return true;
		} else {
			// so the other isn't an alphabet, so we call super.equals()
			return super.equals(obj);
		}
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
		return characters.indexOf(chr);
	}

	/**
	 * Retrieves a character from the alphabet
	 *
	 * @param index the position (0-based) of the character to retrieve
	 * @return the character on position index in the alphabet
	 */
	@Override
	public char getChar(int index) {
		return characters.get(index);
	}

	/**
	 * @return the number of characters in the alphabet
	 */
	@Override
	public int size() {
		return characters.size();
	}

	/**
	 * Checks if the given character is in the alphabet
	 *
	 * @param chr the character that should be checked
	 * @return true if the character is in the alphabet
	 */
	@Override
	public boolean contains(char chr) {
		return characters.contains(chr);
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
		// check every single character for being a part of this alphabet
		for(int i = 0; i < word.length(); i++) {
			if(!contains(word.charAt(i))) {
				return false;
			}
		}
		return true;
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
		StringBuilder ret = new StringBuilder();
		for(int i = 0; i < input.length(); i++) {
			// only add the character to the return string if it's part of the alphabet
			if(!contains(input.charAt(i))) {
				continue;
			}
			ret.append(input.charAt(i));
		}
		return ret.toString();
	}

	/**
	 * Returns the underlying characters in correct order as a char array of
	 * size {@link #size()}
	 *
	 * @return a char array representing the alphabet
	 */
	@Override
	public char[] asCharArray() {
		// convert the characters array list to a Character array and convert that to a char array
		return ((new UtilsImpl()).toCharArray(this.asCharacterArray()));
	}

	/**
	 * Returns the underlying characters in correct order as a Character array of
	 * size {@link #size()}
	 *
	 * @return a char array representing the alphabet
	 */
	public Character[] asCharacterArray() {
		return characters.toArray(new Character[characters.size()]);
	}

	/**
	 * Returns the underlying characters in correct order as a List of
	 * size {@link #size()}
	 *
	 * @return a char array representing the alphabet
	 */
	public List<Character> asCharacterList() {
		return characters;
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Character> iterator() {
		return characters.iterator();
	}

	/**
	 * Returns a string representing the alphabet. This is used for outputting it to the console
	 *
	 * @return String for outputting the alphabet to a console
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("[");
		for(Character character : characters) {
			out.append(character);
		}
		out.append("]");
		return out.toString();
	}
}

package de.tudarmstadt.gdi1.project.alphabet;

import de.tudarmstadt.gdi1.project.exception.InvalidCharacterException;

import java.util.*;

/**
 * @author Mischa Holz
 */
public class AlphabetImpl implements Alphabet {

	protected TreeSet<Character> characters;

	public AlphabetImpl() {
		characters = new TreeSet<Character>();
	}

    public AlphabetImpl(Collection<Character> characters) {
        this(characters.toArray(new Character[characters.size()]));
    }

    public AlphabetImpl(Character[] characters) {
        this();
        for(char c: characters){
            if (this.characters.contains(c)) {
                throw new InvalidCharacterException("The character '" + c + "' exists twice!");
            }
            this.characters.add(c);
        }
    }

    public AlphabetImpl(String characters) { //TODO this code is reduntant; remove it somehow
        this();
        for(char c: characters.toCharArray()){
            if (this.characters.contains(c)) {
                throw new InvalidCharacterException("The character '" + c + "' exists twice!");
            }
            this.characters.add(c);
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
		int i = 0;
		for(Character c : characters) {
			if(c.equals(chr)) {
				return i;
			}
			i++;
		}

		return -1;
	}

	/**
	 * Retrieves a character from the alphabet
	 *
	 * @param index the position (0-based) of the character to retrieve
	 * @return the character on position index in the alphabet
	 */
	@Override
	public char getChar(int index) {
		int i = 0;
		for(Character c : characters) {
			if(i == index) {
				return c;
			}
			i++;
		}

		return (char)0;
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
		String ret = "";
		for(int i = 0; i < input.length(); i++) {
			if(!contains(input.charAt(i))) {
				continue;
			}
			ret += input.charAt(i);
		}
		return ret;
	}

	/**
	 * Returns the underlying characters in correct order as a char array of
	 * size {@link #size()}
	 *
	 * @return a char array representing the alphabet
	 */
	@Override
	public char[] asCharArray() {
		char[] ret = new char[characters.size()];
		int i = 0;
		for(Character c : characters) {
			ret[i] = c;
			i++;
		}
		return ret;
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
}

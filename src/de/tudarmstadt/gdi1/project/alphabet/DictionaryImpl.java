package de.tudarmstadt.gdi1.project.alphabet;

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Scans a text for words and saves them for later uses
 *
 */
public class DictionaryImpl implements Dictionary {

	/**
	 * source alphabet
	 */
	protected Alphabet alphabet;

	/**
	 * sorted set of words
	 */
	protected TreeSet<String> words;

	/**
	 * Default constructor which extracts words from the text
	 *
	 * @param text     Text
	 * @param alphabet Alphabet of the words and the text
	 */
	public DictionaryImpl(String text, Alphabet alphabet) {
		this.alphabet = alphabet;
		words = new TreeSet<String>();

		// finds all words and then saves them if they are allowed in the alphabet
		Scanner scanner = new Scanner(text).useDelimiter("[ ,!?.]");
		while(scanner.hasNext()) {
			String word = scanner.next();
			if(alphabet.allows(word) && !word.equals("")) {
				words.add(word);
			}
		}

		scanner.close();
	}


	/**
	 * Checks if a word is contained in the dictionary
	 *
	 * @param word the word
	 * @return true, if the word is contained in the dictionary, otherwise false
	 */
	@Override
	public boolean contains(String word) {
		return words.contains(word);
	}

	/**
	 * @return the Alphabet that defines the characterspace of the dictionary
	 */
	@Override
	public Alphabet getAlphabet() {
		return alphabet;
	}

	/**
	 * @return the number of entries in the dictionary
	 */
	@Override
	public int size() {
		return words.size();
	}

	/**
	 * gets an item at a specific position (sorted in natural order) in the
	 * dictionary
	 *
	 * @param index the index of the item that should be retrieved.
	 * @return the item at the index. If the index is out of bounds an
	 * indexOutOfBounds exception is thrown
	 */
	@Override
	public String get(int index) {
		int i = 0;
		for(String s : words) {
			if(i == index) {
				return s;
			}
			i++;
		}
		return null;
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<String> iterator() {
		return words.iterator();
	}
}

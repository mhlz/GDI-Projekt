package de.tudarmstadt.gdi1.project.analysis;

import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;
import de.tudarmstadt.gdi1.project.alphabet.DistributionImpl;

/**
 * validates a possible decryption for a ciphertext
 *
 */
public class ValidateDecryptionOracleImpl implements ValidateDecryptionOracle {

	/**
	 * the distribution of the plaintext
	 */
	protected Distribution distribution;

	/**
	 * a dictionary of the plaintext
	 */
	protected Dictionary dictionary;


	/**
	 * constructor for a ValidateDecryptionOracle
	 * checks if a string, is a valid string for, given distribution and dictionary
	 *
	 * @param distribution distribution
	 * @param dictionary   dictionary
	 */
	public ValidateDecryptionOracleImpl(Distribution distribution, Dictionary dictionary) {
		this.distribution = distribution;
		this.dictionary = dictionary;
	}


	/**
	 * Given a plaintext this method returns true, if that plaintext is the
	 * "correct" plaintext. Depending on the implementation the definition of
	 * "correct" varies. A "cheating" oracle might know the plaintext one is
	 * after. This is great for testing and debugging. An actual implementation
	 * would need to make use of a {@link Distribution} and a {@link Dictionary}
	 * to determine whether a plaintext is "good".
	 *
	 * @param plaintext the plaintext to test
	 * @return true if the plaintext is the correct plaintext
	 */
	@Override
	public boolean isCorrect(String plaintext) {
		// detects how many words should be correct in the dictionary, based on longest word in dictionary
		int leastWord;
		// set to how many ranks of characters it should compare
		int rankRange = 1;
		boolean result;
		// get rid of all spaces and similiar structures
		String filterPlaintext = plaintext.replaceAll("//s+", "");


		// depending on size of String, find appropiate number of correct words to be found
		if(filterPlaintext.length() > longestWord(dictionary)) {
			leastWord = (int) filterPlaintext.length() / longestWord(dictionary);
		} else {
			// find at least 1 word
			leastWord = 1;
		}

		// check for all aspects
		result = checkAlphabet(filterPlaintext) && checkRank(filterPlaintext, rankRange) && (checkContent(filterPlaintext) >= leastWord);

		return result;
	}

	/**
	 * check for found words from the dictionary in the given text
	 *
	 * @param text text to be checked
	 * @return number of found words
	 */
	public int checkContent(String text) {

		int x = 0;

		// run through whole dictionary
		for(int i = 0; i < dictionary.size(); i++) {
			// check if current item in dictionary is in text
			if(text.indexOf(dictionary.get(i)) != -1) {
				x = x + 1;
			}
		}

		return x;
	}

	/**
	 * check if given text only contains characters valid to the alphabet
	 *
	 * @param text text to be checked
	 * @return true if all characters are legit
	 */
	public boolean checkAlphabet(String text) {

		// check both alphabets of given dictionary and distribution
		return dictionary.getAlphabet().allows(text) &&
				distribution.getAlphabet().allows(text);

	}

	/**
	 * check if the ranks of a character is the same of the distribution
	 *
	 * @param text  given text to be checked
	 * @param range how many ranks should be compared
	 * @return if the rankings match
	 */
	public boolean checkRank(String text, int range) {

		// create distribution of text
		DistributionImpl stringDist = new DistributionImpl(distribution.getAlphabet(), text);

		// run to how many ranks should be checked
		for(int i = 1; i <= range; i++) {
			// most used char in given distribution
			char mostUsedDist = distribution.getByRank(1, i).charAt(0);
			// most used char in String
			char mostUsedString = stringDist.getByRank(1, i).charAt(0);
			// compare both most used chars, by rank
			if(mostUsedDist != mostUsedString) {
				return false;
			}
		}
		return true;
	}

	/**
	 * find longest word in given dictionary
	 *
	 * @param dic dictionary
	 * @return size of the longest word in the dictionary
	 */
	public int longestWord(Dictionary dic) {
		int result = 0;

		// run through whole dictionary
		for(int i = 0; i < dic.size(); i++) {
			if(dic.get(i).length() > result) {
				// enter the longer word from previous found
				result = dic.get(i).length();
			}
		}
		return result;
	}


}

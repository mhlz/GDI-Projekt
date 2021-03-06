package de.tudarmstadt.gdi1.project.analysis.vigenere;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;
import de.tudarmstadt.gdi1.project.alphabet.DistributionImpl;
import de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic.VigenereImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * an attack on a Vignere cipher
 */
public class VigenereCryptanalysisImpl implements VigenereCryptanalysis {
	/**
	 * Attack to determine all possible length of the used key based on a given
	 * ciphertext.
	 *
	 * @param ciphertext the ciphertext
	 * @return the possible key lengths (in ascending order)
	 */
	@Override
	public List<Integer> knownCiphertextAttack(String ciphertext) {
		return getKeyLength(ciphertext);
	}


	/**
	 * Attack to determine the used key based on a given cipher- and
	 * (corresponding) plaintext.
	 *
	 * @param ciphertext the ciphertext
	 * @param plaintext  the corresponding plaintext
	 * @param alphabet   the alphabet
	 * @return the key, a part of the key, or null
	 */
	@Override
	public String knownPlaintextAttack(String ciphertext, String plaintext, Alphabet alphabet) {
		List<Integer> keyLenghts = new ArrayList<Integer>();
		char[] keyPart = new char[plaintext.length()];
		String stringKeyPart = "";
		char[] keyPartCalc = new char[plaintext.length()];
		String stringKeyPartCalc = "";
		char[] compare = new char[plaintext.length()];
		String stringCompare = "";
		String lastCheckedKey = "";
		int counter;
		boolean lastTryTrue = false;

		//calculate the keyPart from known plain- and ciphertext
		for(int i = 0; i < ciphertext.length(); i++) {
			int tmp = alphabet.getIndex(ciphertext.charAt(i)) - alphabet.getIndex(plaintext.charAt(i));
			if(tmp < 0) {
				tmp += alphabet.size();
			}
			keyPart[i] = alphabet.getChar(tmp);
		}

		//convert calculated keyPart from char Array to String
		for(char c : keyPart) {
			stringKeyPart += c;
		}

		//if getKeyLenghts returns no found repetitions in keyPart, return whole keyPart as key
		keyLenghts = getKeyLength(stringKeyPart);
		if(keyLenghts.size() == 0) {
			return stringKeyPart;
		}

		for(int i = 0; i < keyLenghts.size(); i++) {

			//test every given keyLength
			for(int n = 0; n < keyLenghts.get(i); n++) {

				//reinitiate Strings to prevent errors
				stringCompare = "";
				stringKeyPartCalc = "";

				//set counter to split Strings
				counter = -1;

				//extract possible key with size i from the keyPart...
				for(int m = 0; m < keyLenghts.get(i); m++) {
					compare[m] = keyPart[m];
					counter++;
				}

				//...and convert it to String
				for(int m = 0; m < keyLenghts.get(i); m++) {
					stringCompare += compare[m];
				}

				//save the remaining keyPart to use it for comparsion...
				for(int m = 0; m < keyPart.length - counter; m++) {
					keyPartCalc[m] = keyPart[m + counter];
				}

				//...and again convert it to String
				for(char c : keyPartCalc) {
					stringKeyPartCalc += c;
				}

				//if comparsion is true
				if(stringKeyPartCalc.contains(stringCompare)) {
					//save that the rest of the key contains the checked sequence and save it
					lastTryTrue = true;
					lastCheckedKey = stringCompare;

				} else if(lastTryTrue) {

					//check if last key is still in rest of keyPart (possible if key is used multiple times in a row)
					if(stringKeyPartCalc.contains(lastCheckedKey)) {
						return stringCompare;
					}
					//else return last checked key
					return lastCheckedKey;
				}

			}
		}
		//if all keySizes were tested and the last Checked key was correct, it is the right one, so it will be returned
		if(lastTryTrue) {
			if(stringKeyPartCalc.contains(lastCheckedKey)) {
				return stringCompare;
			}
		}
		return null;
	}

	/**
	 * Attack to determine the used key based on a given cipher- and
	 * (corresponding) plaintext and a given distribution on the alphabet.
	 *
	 * @param ciphertext   the ciphertext
	 * @param plaintext    the corresponding plaintext
	 * @param distribution the distribution
	 * @return the key, a part of the key, or null
	 */
	@Override
	public Object knownPlaintextAttack(String ciphertext, String plaintext, Distribution distribution) {
		return null;
	}

	/**
	 * Attack to determine the used key based on a given cipher- and
	 * (corresponding) plaintexts and a given distribution on the alphabet.
	 *
	 * @param ciphertext
	 * @param plaintext
	 * @param distribution The distribution
	 * @param dictionary   @return the key, a part of the key, or null
	 */
	@Override
	public Object knownPlaintextAttack(String ciphertext, String plaintext, Distribution distribution, Dictionary dictionary) {
		return null;
	}

	/**
	 * Attack to determine the used key based on a given ciphertext, a given
	 * distribution on the alphabet and a list of known plaintext cribs.
	 *
	 * @param ciphertext   the ciphertext
	 * @param distribution the distribution
	 * @param cribs        the list of substrings known to appear in the plaintext
	 * @return the key, a part of the key, or null
	 */
	@Override
	public String knownCiphertextAttack(String ciphertext, Distribution distribution, List<String> cribs) {
		List<Integer> keys = getKeyLength(ciphertext);
		String ret = "";

		// run through all found keys
		for(int i = 0; i < keys.size(); i++) {
			ret = "";
			// run through the size of the current key
			for(int j = 1; j <= keys.get(i); j++) {

				// extracted chars dependent on the current size of key
				String extractedCharacters = extract(ciphertext, keys.get(i), j);

				// make distribution for extracted string
				DistributionImpl keyDist = new DistributionImpl(distribution.getAlphabet(), extractedCharacters);

				// most used in given alphabet
				char mostUsedPlain = distribution.getByRank(1, 1).charAt(0);

				// most used in current extracted string
				char mostUsedKey = keyDist.getByRank(1, 1).charAt(0);

				// get numbers to determine shift range
				int plain = distribution.getAlphabet().getIndex(mostUsedPlain);
				int key = distribution.getAlphabet().getIndex(mostUsedKey);

				int shift = key - plain;
				// get code char
				char keyChar = distribution.getAlphabet().getChar(shift);

				ret = ret + keyChar;
			}

			VigenereImpl keyTest = new VigenereImpl(ret, distribution.getAlphabet());

			// decrypt with found pass code
			String plaintext = keyTest.decrypt(ciphertext);
			int x = 0;

			// run through cribs array size
			for(int j = 0; j < cribs.size(); j++) {
				// check if string in current cribs is contained in decrypted string
				if(plaintext.contains(cribs.get(j))) {
					x = x + 1;
				}
			}

			// pass code worked - return the code
			if(x == cribs.size()) {
				return ret;
			}
		}

		// no valid pass code found
		return "";
	}

	/**
	 * extracts every nth character in a sequence of sequenceLength  , of a String
	 *
	 * @param sequenceLength sequence length
	 * @param offset         nth character to be extracted
	 * @param input          String
	 * @return String of all the nth characters
	 */
	public String extract(String input, int sequenceLength, int offset) {
		// counter of sequence length
		int x = 1;
		String ret = "";
		// run to length of string
		for(int i = 0; i < input.length(); i++) {

			// counter in a sequence on the character to be extracted
			if(x == offset) {
				ret += input.charAt(i);
			}

			// reset sequence counter
			if(x == sequenceLength) {
				x = 1;
			} else {
				x = x + 1;
			}

		}
		return ret;
	}


	/**
	 * calculates all divisors of a number
	 *
	 * @param number
	 * @return a List of dividends
	 */
	public List<Integer> getDividends(int number) {
		List<Integer> ret = new ArrayList<Integer>();
		// checks all numbers starting from one to the number itself
		for(int i = 1; i <= number; i++) {
			// if mod 0 the current number is a divident
			if(number % i == 0) {
				ret.add(i);
			}
		}
		return ret;
	}


	/**
	 * calculates the ggT of 2 numbers
	 *
	 * @param a
	 * @param b
	 * @return ggT of a and b
	 */
	public int ggT(int a, int b) {

		//euklid algorithm
		if(a == b) {
			return (a);
		} else if(a > b) {
			return ggT(a - b, b);
		} else {
			return ggT(b - a, a);
		}

	}

	/**
	 * returns the ggT of a List of numbers
	 *
	 * @param inputList list of numbers
	 * @return ggT of all the numbers
	 */
	public int ggT(List<Integer> inputList) {
		// check if list is smaller 2
		if(inputList.size() < 2) {
			// return item, only one in list
			return inputList.get(0);
		} else {
			// first pair
			int x = ggT(inputList.get(0), inputList.get(1));
			// skip first pair, run to rest of the list

			for(int i = 2; i < inputList.size(); i++) {
				// check ggT of previous pair with new item
				x = ggT(inputList.get(i), x);
			}

			return x;
		}
	}

	/**
	 * calculates the distance between a multiple word sequence in a string
	 *
	 * @param input    String
	 * @param sequence length of a sequence
	 * @return a List of distances between same sequences
	 */
	public List<Integer> getDistance(String input, int sequence) {
		List<Integer> ret = new ArrayList<Integer>();
		String temp;
		String split;

		// run through string length
		for(int i = 0; i < input.length() - sequence; i++) {
			// contains the word to be checked
			temp = input.substring(i, i + sequence);
			// contains the rest of the string, cutting the search word
			split = input.substring(input.indexOf(temp) + sequence);
			// word is contained -> add into result
			if(split.indexOf(temp) != -1) {
				ret.add(split.indexOf(temp) + sequence);
			}
		}
		return ret;
	}

	/**
	 * returns possible key lengthes for a coded word
	 *
	 * @param chiffre coded word
	 * @return possible key lengthes
	 */
	public List<Integer> getKeyLength(String chiffre) {
		List<Integer> ret = new ArrayList<Integer>();
		// run from smallest reasonable keylength to the max wordcount
		for(int i = 3; i <= chiffre.length(); i++) {
			// take all distances
			ret.addAll(getDistance(chiffre, i));
		}
		if(!ret.isEmpty()) {
			// calculate ggt of all distances and get all dividends of the ggt
			ret = (getDividends(ggT(ret)));
		}
		return ret;
	}


}

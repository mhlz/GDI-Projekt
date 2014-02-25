package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.*;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipherImpl;

import java.util.*;

/**
 * Created by Hannes on 2/25/14.
 *
 * @author Hannes Güdelhöfer
 */
public class MonoalphabeticCribCryptanalysisImpl implements MonoalphabeticCribCryptanalysis, BacktrackingAnalysis {

	Map<Character, Character> key;

	int iterarionCount;
	int recursionCount;


	/**
	 * This methods marks the starting point of the recursion for a backtracking
	 * session
	 *
	 * @param key                      the current (partial) solution for the reconstructed key. An
	 *                                 assignment is stored as character from source alphabet maps to
	 *                                 character from target alphabet.
	 * @param ciphertext               the known ciphertext
	 * @param alphabet                 the plaintext alphabet
	 * @param distribution             the distribution
	 * @param dictionary               a dictionary
	 * @param cribs                    a list of words contained in the plaintext
	 * @param validateDecryptionOracle An oracle that allows to test whether a reconstructed
	 *                                 plaintext is correct.
	 * @return
	 */
	@Override
	public Map<Character, Character> reconstructKey(Map<Character, Character> key, String ciphertext, Alphabet alphabet, Distribution distribution, Dictionary dictionary, List<String> cribs, ValidateDecryptionOracle validateDecryptionOracle) {
		recursionCount++;
		boolean isFull = true;
		for (Character c : key.values()) {
			if (c == (char) -1) {
				isFull = false;
				break;
			}
		}
		if (isFull) {
			if (validateDecryptionOracle.isCorrect(new MonoalphabeticCipherImpl(alphabet, new AlphabetImpl(key.values())).decrypt(ciphertext))) {
				recursionCount--;
				return key;
			} else {
				recursionCount--;
				return null; //this doesn't look save...
			}
		}

		Character nextCharacter = getNextSourceChar(key, alphabet, distribution, dictionary, cribs);
		Collection<Character> potentialAssignments = getPotentialAssignments(nextCharacter, key, ciphertext, alphabet, distribution, dictionary);

		for (Character potentialReplacement : potentialAssignments) {
			iterarionCount++;
			key.put(nextCharacter, potentialReplacement);
			this.key = key;
			Map<Character, Character> tmp = reconstructKey(new HashMap<Character, Character>(key), ciphertext, alphabet, distribution, dictionary, cribs, validateDecryptionOracle);
			if (tmp != null) {
				recursionCount--;
				return tmp;
			}
		}
		recursionCount--;
		return null; //this doesn't either
	}

	/**
	 * Returns a list of (sorted) assignments for the next
	 *
	 * @param targetCharacter the character from the source alphabet for which the
	 *                        assignment is computed
	 * @param key             the current partial key
	 * @param ciphertext      the ciphertext
	 * @param alphabet        the plaintext alphabet
	 * @param distribution    the distribution
	 * @param dictionary      a dictionary
	 * @return
	 */
	@Override
	public Collection<Character> getPotentialAssignments(Character targetCharacter, Map<Character, Character> key, String ciphertext, Alphabet alphabet, Distribution distribution, Dictionary dictionary) {

		TreeSet<Character> ret = new TreeSet<Character>();

		if (ciphertext.contains(" ")) {
			Dictionary cipherWords = new DictionaryImpl(ciphertext, alphabet);
			for (String cipherWord : cipherWords) {                                   // for every word in the ciphertext
				if (cipherWord.indexOf(targetCharacter) != -1) {                     // that contains our targetCharacter

					for (String plainWord : dictionary) {                             // we find the words in the dictionary
						if (plainWord.length() == cipherWord.length()) {             // that share the same length

							for (int i = 0; i < cipherWord.length(); i++) {          // and the get the characters that share the same position as our targetCharacter
								if (cipherWord.charAt(i) == targetCharacter) {       // because we use a treeSet no chacrater can be returned twice
									ret.add(plainWord.charAt(i));
								}
							}

						}
					}

				}
			}
		} else {

			for (Character c : alphabet) {
				if (!key.values().contains(c)) {
					ret.add(c);
				}
			}

		}

		return ret;
	}

	/**
	 * Returns the next character from the source alphabet that should be
	 * handled (that is for which a target character from the cipher alphabet
	 * should be chosen).
	 *
	 * @param key          the current partial key
	 * @param alphabet     the plaintext alphabet
	 * @param distribution the distribution
	 * @param dictionary   a dictionary
	 * @param cribs        a list of words contained in the plaintext
	 * @return a character from the alphabet
	 */
	@Override
	public Character getNextSourceChar(Map<Character, Character> key, Alphabet alphabet, Distribution distribution, Dictionary dictionary, List<String> cribs) {
		for (int i = 0; i <= distribution.getAlphabet().size(); i++) {
			if (key.get(distribution.getByRank(1, i + 1).charAt(0)) == (char) -1) {
				String tmp = distribution.getByRank(1, i + 1);
				return tmp.charAt(0);
			}
		}
		return null;
	}

	/**
	 * The method is given a current partial solution and checks whether this
	 * partial solution can lead to a correct solution or not.
	 *
	 * @param alphabet     the alphabet
	 * @param ciphertext   the ciphertext
	 * @param key          the current partial key
	 * @param distribution the distribution
	 * @param dictionary   a dictionary
	 * @param cribs        a list of words contained in the plaintext
	 * @return true, if the current partial solution is consistent with a
	 * correct solution
	 */
	@Override
	public boolean isPromisingPath(Alphabet alphabet, String ciphertext, Map<Character, Character> key, Distribution distribution, Dictionary dictionary, Collection<String> cribs) {
		return false;
	}

	/**
	 * Attack to determine the used key based on a ciphertext and a given
	 * distribution and dictionary as well as a list of words that appear in the
	 * plaintext (cribs).
	 *
	 * @param ciphertext   the ciphertext
	 * @param distribution the distribution
	 * @param dictionary   the dictionary
	 * @param cribs        A list of words known to be in the plaintext
	 * @return The reconstructed key represented as a char array
	 */
	@Override
	public char[] knownCiphertextAttack(String ciphertext, Distribution distribution, Dictionary dictionary, List<String> cribs) {
		return new char[0];
	}

	/**
	 * Attack to determine the used key based on a ciphertext and a given
	 * distribution and dictionary as well as a list of words that appear in the
	 * plaintext (cribs). In addition an oracle is provided that allows to
	 * verify whether a decryption is the correct one.
	 *
	 * @param ciphertext               the ciphertext
	 * @param distribution             the distribution
	 * @param dictionary               the dictionary
	 * @param cribs                    A list of words known to be in the plaintext
	 * @param validateDecryptionOracle a verification oracle allowing to validate a decryption.
	 * @return The reconstructed key represented as a char array
	 */
	@Override
	public char[] knownCiphertextAttack(String ciphertext, Distribution distribution, Dictionary dictionary, List<String> cribs, ValidateDecryptionOracle validateDecryptionOracle) {
		key = new HashMap<Character, Character>();
		for (Character c : distribution.getAlphabet()) {
			key.put(c, (char) -1);
		}
		iterarionCount = 0;
		key = reconstructKey(key, ciphertext, distribution.getAlphabet(), distribution, dictionary, cribs, validateDecryptionOracle);

		char[] ret = new char[key.values().size()];
		int i = 0;
		for (Character c : key.values()) {
			ret[i] = c;
			i++;
		}


		return ret;
	}

	/**
	 * Returns a description of the current state of the algorithm
	 *
	 * @param sourceAlphabet
	 * @param targetKey
	 * @return a description of the current state.
	 */
	@Override
	public String getState(Alphabet sourceAlphabet, Alphabet targetKey) {
		StringBuilder out = new StringBuilder();
		StringBuilder target = new StringBuilder("[");
		for(Character c: targetKey) {
			target.append("'").append(c).append("',");
		}
		target.deleteCharAt(target.length() - 1).append("]");

		StringBuilder real = new StringBuilder("[");
		for(Character c: key.values()) {
			real.append("'");
			if(c == (char) -1){
				real.append(" ");
			} else {
				real.append(c);
			}
			real.append("',");
		}
		real.deleteCharAt(real.length() - 1).append("]");

		StringBuilder rightString = new StringBuilder("[");
		int i = 0;
		for(Character c: key.values()) {
			rightString.append(" ");
			if(c.equals(targetKey.getChar(i))){
				rightString.append("1");
			} else {
				rightString.append("0");
			}
			rightString.append(" ,");
			i++;
		}
		rightString.deleteCharAt(rightString.length() - 1).append("]");

		out.append("target          : ").append(target).append(System.lineSeparator());
		out.append("guess           : ").append(real).append(System.lineSeparator());
		out.append("correct         : ").append(rightString).append(System.lineSeparator());
		out.append("iterations      : ").append(iterarionCount).append(System.lineSeparator());
		out.append("recursions      : ").append(recursionCount).append(System.lineSeparator());
		int right = 0;
		i = 0;
		for(Character c: key.values()) {
			if(c.equals(targetKey.getChar(i))){
				right++;
			} else {
				break;
			}
		}
		out.append("characters right: ").append(right).append(System.lineSeparator());
		return out.toString();

	}

	public String toString() {
		StringBuilder out = new StringBuilder();
		for (Character c : key.keySet()) {
			out.append(c);
		}
		out.append(" : ");

		for (Character c : key.values()) {
			out.append(c);
		}
		return out.toString();
	}
}

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

	Distribution cipherDistribution;
	String path;
	int iterarionCount;
	int recursionCount;

	Collection<String> cribs;


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
		if(!isPromisingPath(alphabet, ciphertext,key,distribution, dictionary,cribs)) {
			recursionCount--;
			return null;
		}


		if (alphabet.size() == key.values().size()) {
			ArrayList<Character> tmpList = new ArrayList<Character>();
			for(Character c: alphabet){
				tmpList.add(key.get(c));
			}
			if (validateDecryptionOracle.isCorrect(new MonoalphabeticCipherImpl(alphabet, new AlphabetImpl(tmpList)).decrypt(ciphertext))) {
				recursionCount--;
				return key;
			} else {
				recursionCount--;
				return null;
			}
		}

		Character nextCharacter = getNextSourceChar(key, alphabet, distribution, dictionary, cribs);
		Collection<Character> potentialAssignments = getPotentialAssignments(nextCharacter, key, ciphertext, alphabet, distribution, dictionary);

		for (Character potentialReplacement : potentialAssignments) {
			iterarionCount++;
			key.put(nextCharacter, potentialReplacement);
			this.key = key;
			path += nextCharacter;
			Map<Character, Character> tmp = reconstructKey(new HashMap<Character, Character>(key), ciphertext, alphabet, distribution, dictionary, cribs, validateDecryptionOracle);
			path = path.substring(0, path.length() - 1);
			if (tmp != null) {
				recursionCount--;
				return tmp;
			}
		}
		recursionCount--;
		return null;
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
		ArrayList<Character> ret = new ArrayList<Character>();

		int characterRank = 1;
		int i = 1;
		if(cipherDistribution != null) {
			for(String str: distribution.getSorted(1)){
				if(str.charAt(0) == targetCharacter) {
					characterRank = i;
					break;
				}
				i++;
			}

			for(int error = 0; error < 3; error++){
				if(characterRank - error > 0) {
					char c = cipherDistribution.getByRank(1,characterRank - error).charAt(0);
					if(!ret.contains(c)) {
						ret.add(c);
					}
				}
				if(characterRank + error <= alphabet.size()) {
					char c = cipherDistribution.getByRank(1,characterRank + error).charAt(0);
					if(!ret.contains(c)) {
						ret.add(c);
					}
				}
			}
		}

		for (Character c : alphabet) {
			if (!key.values().contains(c)) {
				if(!ret.contains(c)) {
					ret.add(c);
				}
			}
		}
		ret.removeAll(key.values());
		ArrayList<Character> ret2 = new ArrayList<Character>();
		for(Character c: ret) {
			if(!ret2.contains(c)) {
				ret2.add(c);
			}
		}

		return ret2;
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

		if(!key.containsKey(distribution.getByRank(1, 1).charAt(0))) {
			String tmp = distribution.getByRank(1, 1);
			return tmp.charAt(0);
		}
		if(cribs != null){
			for(String word: cribs) {
				for(Character c: word.toCharArray()) {
					if(!key.containsKey(c)) {
						return c;
					}
				}
			}
		}

		for (int i = 0; i <= distribution.getAlphabet().size(); i++) {
			if (!key.containsKey(distribution.getByRank(1, i + 1).charAt(0))) {
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

		if(cribs.size() == 0){
			return true;
		}

		//creating a complete alphabet out of the key
		ArrayList<Character> targetTmp = new ArrayList<Character>();
		for(Character c: alphabet) {
			if(key.containsKey(c)) {
				targetTmp.add(key.get(c));
			} else {
				targetTmp.add((char) -1);
			}
		}

		char[] restTarget = alphabet.asCharArray();

		LinkedList<Character> restToAdd = new LinkedList<Character>();
		for(Character c: restTarget) {
			if(!targetTmp.contains(c)){
				restToAdd.add(c);
			}
		}

		ArrayList<Character> target = new ArrayList<Character>();

		for(Character c: targetTmp) {
			if(c == (char) -1) {
				target.add(restToAdd.poll());
			} else {
				target.add(c);
			}
		}

		Alphabet tempAlph = new AlphabetImpl(key.keySet());
		MonoalphabeticCipherImpl cipher = new MonoalphabeticCipherImpl(alphabet, new AlphabetImpl(target));
		String plaintext = cipher.decrypt(ciphertext);
		for(String word: cribs) {
			for(int i = 1; i <= word.length(); i++){
				String tmp = word.substring(0, i);
				if(tempAlph.allows(tmp)) {
					if(!plaintext.contains(tmp)) {
						return false;
					}
				} else {
					i = word.length() + 1;
				}
			}
		}

		return true;
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
		iterarionCount = 0;
		this.cribs = cribs;
		cipherDistribution = new DistributionImpl(dictionary.getAlphabet(), ciphertext);
		path = "";

		key = reconstructKey(key, ciphertext, distribution.getAlphabet(), distribution, dictionary, cribs, validateDecryptionOracle);

		char[] ret = new char[key.values().size()];
		int i = 0;
		for (Character c : distribution.getAlphabet()) {
			ret[i] = key.get(c);
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

		if(key == null || key.size() == 0){
			return "no partial key found (probably not started yet)";
		}
		Map<Character, Character> key = new TreeMap<Character, Character>(this.key);

		StringBuilder out = new StringBuilder();

		StringBuilder alph = new StringBuilder("[");
		for(Character c: sourceAlphabet) {
			alph.append("'").append(c).append("',");
		}
		alph.deleteCharAt(alph.length() - 1).append("]");

		StringBuilder target = new StringBuilder("[");
		for(Character c: targetKey) {
			target.append("'").append(c).append("',");
		}
		target.deleteCharAt(target.length() - 1).append("]");

		StringBuilder real = new StringBuilder("[");
		for(Character c: sourceAlphabet) {
			real.append("'");
			if(key.containsKey(c)){
				real.append(key.get(c));
			} else {
				real.append(" ");

			}
			real.append("',");
		}
		real.deleteCharAt(real.length() - 1).append("]");

		StringBuilder rightString = new StringBuilder("[");
		int count = 0;
		int i = 0;
		for(Character c: sourceAlphabet) {
			rightString.append(" ");
			if(key.containsKey(c) && key.get(c) != null && key.get(c).equals(targetKey.getChar(i))){
				rightString.append("1");
				count++;
			} else {
				rightString.append("0");
			}
			rightString.append(" ,");
			i++;
		}
		rightString.deleteCharAt(rightString.length() - 1).append("] (").append(count).append(")");


		int right = 0;
		i = 0;
		for(Character c: path.toCharArray()) {
			if(key.containsKey(c) && targetKey.getChar(sourceAlphabet.getIndex(c)) == key.get(c)) {
				right++;
			} else {
				break;
			}
			i++;
		}

		StringBuilder pathDecrypted = new StringBuilder();
		for(Character c: path.toCharArray()) {
			pathDecrypted.append(key.get(c));
		}

		out.append("source alphabet : ").append(alph).append(System.lineSeparator());
		out.append("target          : ").append(target).append(System.lineSeparator());
		out.append("guess           : ").append(real).append(System.lineSeparator());
		out.append("correct         : ").append(rightString).append(System.lineSeparator());
		out.append("path            : ").append(path).append(System.lineSeparator());
		out.append("path decrypted  : ").append(pathDecrypted).append(System.lineSeparator());

		out.append("iterations      : ").append(iterarionCount).append(System.lineSeparator());
		out.append("recursions      : ").append(recursionCount).append(System.lineSeparator());
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

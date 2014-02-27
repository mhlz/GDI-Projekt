package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.*;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipherImpl;
import de.tudarmstadt.gdi1.project.exception.NoLetterToCheckFoundException;

import java.util.*;

/**
 * Created by Hannes on 2/25/14.
 *
 * @author Hannes Güdelhöfer
 */
public class MonoalphabeticCribCryptanalysisImpl implements MonoalphabeticCribCryptanalysis, BacktrackingAnalysis {

	/**
	 * saving the key so getState can work with it
	 */
	Map<Character, Character> key;

	/**
	 * savin a distribution vom the cipher text so we don't need to aclulatet it every iteration
	 * This saves a lot of time
	 */
	Distribution cipherDistribution;

	/**
	 * saving the path so getState can work with it
	 */
	String path;

	/**
	 * how many iterations are made
	 */
	int iterarionCount;

	/**
	 * counts how many recursion have been made
	 */
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

		// we check if this path is worth computing
		if(!isPromisingPath(alphabet, ciphertext, key, distribution, dictionary, cribs)) {
			return null;
		}

		//if we have a full key, we need to check if we creacked the cipher
		if(alphabet.size() == key.values().size()) {

			// so we need to create the targetAlphabet
			ArrayList<Character> tmpList = new ArrayList<Character>();
			for(Character c : alphabet) {
				tmpList.add(key.get(c));
			}

			// we check if the key we reconstructed id the right key
			if(validateDecryptionOracle.isCorrect(new MonoalphabeticCipherImpl(alphabet, new AlphabetImpl(tmpList)).decrypt(ciphertext))) {
				return key; // wuhu it was the right key!
			} else {
				return null; // we return null, because it wasn't the right key, so we can't return a valid key
			}
		}

		// get the next character we need to exchange
		//Character nextCharacter = getNextSourceChar(key, alphabet, distribution, dictionary, cribs);
		Character nextCharacter = path.charAt(recursionCount);

		// we get all potential assignments foe the next character
		Collection<Character> potentialAssignments = getPotentialAssignments(nextCharacter, key, ciphertext, alphabet, distribution, dictionary);

		// and then we try each assignment
		for(Character potentialReplacement : potentialAssignments) {

			iterarionCount++;

			// create the next key
			key.put(nextCharacter, potentialReplacement);

			// save some things so getState can display stuff
			this.key = key;

			// recursive call for the next character
			recursionCount++;
			Map<Character, Character> tmp = reconstructKey(new HashMap<Character, Character>(key), ciphertext, alphabet, distribution, dictionary, cribs, validateDecryptionOracle);
			recursionCount--;

			// if we got something != null we are finished and return what we got
			if(tmp != null) {
				return tmp;
			}
		}
		// we ran out of characters to try, so we return null, to state, that we haven't found anything
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

		// we get the frequenzy(or better the rank of the targetCharacter) of the targetCharacter via the given aplhabet
		// and get the charcters that have that frequenzy in the cipheralphabet, via the cipherDistribution, we calculated
		// at the very beginning of the attack (so we save time)
		int characterRank = 1;
		int i = 1;
		if(cipherDistribution != null) {
			// getting the rank of the targetCharacter
			for(String str : distribution.getSorted(1)) {
				if(str.charAt(0) == targetCharacter) {
					characterRank = i;
					break;
				}
				i++;
			}

			// now we add the perfect rank match and everything that is 2 above or 2 below that
			for(int error = 0; error < 3; error++) {
				if(characterRank - error > 0) {
					// the one that is below
					char c = cipherDistribution.getByRank(1, characterRank - error).charAt(0);
					if(!ret.contains(c)) {
						ret.add(c);
					}
				}
				if(characterRank + error <= alphabet.size()) {
					// and the one above
					char c = cipherDistribution.getByRank(1, characterRank + error).charAt(0);
					if(!ret.contains(c)) {
						ret.add(c);
					}
				}
			}
		}

		// now we add the rest of the alphabet, just in case we missed any letters
		for(Character c : alphabet) {
			if(!key.values().contains(c)) {
				if(!ret.contains(c)) {
					ret.add(c);
				}
			}
		}

		// we now remove every letter that allready is in the key ( so we don't try to match one letter twice)
		ret.removeAll(key.values());

		// finaly we remove every letter that is twice in our return list, just to make sure that no letter is in there twice
		// otherwise we would hit an error in Alphabet eventually
		ArrayList<Character> ret2 = new ArrayList<Character>();
		for(Character c : ret) {
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

		// first we try to match the letter that has the highest frequenzy, because this letter is easily matched and gived us therefore a huge advantage and improvement
		if(!key.containsKey(distribution.getByRank(1, 1).charAt(0))) {
			String tmp = distribution.getByRank(1, 1);
			return tmp.charAt(0);
		}

		// if we have cribs, we then try to match the characters in the crib
		// we do this, because we then can test if the the path is a promising one, because the cribs should be in the plaintext very early on
		if(cribs != null) {
			for(String word : cribs) {
				for(Character c : word.toCharArray()) {
					if(!key.containsKey(c)) {
						return c;
					}
				}
			}
		}

		// and then we fill in the rest of the alphabet, sorted after the frequenzy, because its's much easier, to match high frequenzy letters,
		// that low frequenzy letters
		for(int i = 0; i <= distribution.getAlphabet().size(); i++) {
			if(!key.containsKey(distribution.getByRank(1, i + 1).charAt(0))) {
				String tmp = distribution.getByRank(1, i + 1);
				return tmp.charAt(0);
			}
		}

		//if we haven't found any letter, that we should check next, something is off and we return null
		throw new NoLetterToCheckFoundException("there aren't any letters left to check!");
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

		// if we have no cribs, we can't test if this is a good path or not, so we return true, so the backtracking still works right
		if(cribs.size() == 0) {
			return true;
		}


		// we make a temporary alphabet, to check if a crib can be decrypt
		Alphabet tempAlph = new AlphabetImpl(key.keySet());
		// we have to make an instance of a nMonoalphabeticCipher, so we can decrypt the ciphertext
		MonoalphabeticCipherImpl cipher = new MonoalphabeticCipherImpl(alphabet, createCompleteAlphabetFromKey(alphabet, key));
		// the plaintext, as far as we can translate it
		String plaintext = cipher.decrypt(ciphertext);

		// we go through all cribs and check if they can be decrypted
		for(String word : cribs) {
			// we all first characters, (partial crib and full crib match
			for(int i = 1; i <= word.length(); i++) {
				String tmp = word.substring(0, i);
				if(tempAlph.allows(tmp)) {
					// so we can decrypt this part of the word, now we need to check if it's somewhere in the plaintext
					if(!plaintext.contains(tmp)) {
						// the part of the word isn't in the plain text, but it should be, so this Path isn't promising anymore
						return false;
					}
				} else {
					// as this part of the word, can't be decrypted yet, any part that contains this part, can't be either, so we can just go to th next word.
					break; //i = word.length() + 1;
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
	 * creates a complete alphabet filled uo, so it can be used for a cipher
	 *
	 * @param alphabet the alphabet that should complete the key
	 * @param key      the key that will be used to build the alphabet
	 * @return a complete alphabet that represents the cipher from the key
	 */
	private Alphabet createCompleteAlphabetFromKey(Alphabet alphabet, Map<Character, Character> key) {
		// first we create an ArrayList, that contains the replaced characters for the source alphabet, and for every character, that can't be replaced it puts in a '-1'
		ArrayList<Character> targetTmp = new ArrayList<Character>();
		for(Character c : alphabet) {
			if(key.containsKey(c)) {
				targetTmp.add(key.get(c));
			} else {
				targetTmp.add((char) -1);
			}
		}

		// then we compute the rest of the alphabet that isn't in the replacing Alphabet
		char[] restTarget = alphabet.asCharArray();

		LinkedList<Character> restToAdd = new LinkedList<Character>();
		for(Character c : restTarget) {
			if(!targetTmp.contains(c)) {
				restToAdd.add(c);
			}
		}

		// then we fill in the alphabet wirh the missing letters, in order (so we have a working alphabet)
		ArrayList<Character> target = new ArrayList<Character>();

		for(Character c : targetTmp) {
			if(c == (char) -1) {
				target.add(restToAdd.poll());
			} else {
				target.add(c);
			}
		}
		// creatinf the alphabet and returning it
		return new AlphabetImpl(target);
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

		// initiating all the class variables, and computing the distribution of the ciphertext
		key = new HashMap<Character, Character>();
		iterarionCount = 0;
		cipherDistribution = new DistributionImpl(dictionary.getAlphabet(), ciphertext);
		path = "";
		recursionCount = 0;

		// we calculate the path, so we haven't to do this very iteration step
		for(int i = 0; i < distribution.getAlphabet().size(); i++) {
			Character c = getNextSourceChar(key, dictionary.getAlphabet(), distribution, dictionary, cribs);
			key.put(c, c);
			path += c;
		}
		// we need to clear the key, because we only used it so we can calculate the path
		key.clear();

		// starting the recursion
		key = reconstructKey(key, ciphertext, distribution.getAlphabet(), distribution, dictionary, cribs, validateDecryptionOracle);

		// we sort the key ( with key.get(c)) , so it actually represents a valid key.
		char[] ret = new char[key.values().size()];
		int i = 0;
		for(Character c : distribution.getAlphabet()) {
			ret[i] = key.get(c);
			i++;
		}


		return ret;
	}

	/**
	 * Returns a description of the current state of the algorithm
	 *
	 * @param sourceAlphabet the source alphabet of the cipher
	 * @param targetKey the target key, that this class should compute
	 * @return a description of the current state.
	 */
	@Override
	public String getState(Alphabet sourceAlphabet, Alphabet targetKey) {

		// handling that we may have not startet the recursion yet
		if(key == null || key.size() == 0) {
			return "no partial key found (probably not started yet)";
		}

		// copying the key, so we donÄt encounter tasks issues
		Map<Character, Character> key = new TreeMap<Character, Character>(this.key);


		// creating a new Stringbuilder for the return string
		StringBuilder out = new StringBuilder();

		// creating a StringBuilder, that holds the sourceAlhoabet
		StringBuilder alph = new StringBuilder("[");
		for(Character c : sourceAlphabet) {
			alph.append("'").append(c).append("',");
		}
		alph.deleteCharAt(alph.length() - 1).append("]");

		// creating a StringBuilder, that holds the target key
		StringBuilder target = new StringBuilder("[");
		for(Character c : targetKey) {
			target.append("'").append(c).append("',");
		}
		target.deleteCharAt(target.length() - 1).append("]");

		// creating a StringBuilder, that holds the current guess
		StringBuilder real = new StringBuilder("[");
		for(Character c : sourceAlphabet) {
			real.append("'");
			if(key.containsKey(c)) {
				real.append(key.get(c));
			} else {
				real.append(" ");

			}
			real.append("',");
		}
		real.deleteCharAt(real.length() - 1).append("]");

		// creating a StringBuilder, that holds if the current and coutning the right letters
		StringBuilder rightString = new StringBuilder("[");
		int count = 0;
		int i = 0;
		for(Character c : sourceAlphabet) {
			rightString.append(" ");
			if(key.containsKey(c) && key.get(c) != null && key.get(c).equals(targetKey.getChar(i))) {
				rightString.append("1");
				count++;
			} else {
				rightString.append("0");
			}
			rightString.append(" ,");
			i++;
		}
		rightString.deleteCharAt(rightString.length() - 1).append("] (").append(count).append(")");


		// counting the right letters in a row, starting with the first one
		int right = 0;
		i = 0;
		for(Character c : path.toCharArray()) {
			if(key.containsKey(c) && targetKey.getChar(sourceAlphabet.getIndex(c)) == key.get(c)) {
				right++;
			} else {
				break;
			}
			i++;
		}

		// decrypting the path
		StringBuilder pathDecrypted = new StringBuilder();
		for(Character c : path.toCharArray()) {
			if(key.get(c) == null) {
				pathDecrypted.append(' ');
			} else {
				pathDecrypted.append(key.get(c));
			}
		}


		// appending all those computed StringBuilders to the out one and then returning it.
		out.append("source alphabet : ").append(alph).append(System.lineSeparator());
		out.append("target          : ").append(target).append(System.lineSeparator());
		out.append("guess           : ").append(real).append(System.lineSeparator());
		out.append("correct         : ").append(rightString).append(System.lineSeparator());
		out.append("path            : ").append(path).append(System.lineSeparator());
		out.append("path decrypted  : ").append(pathDecrypted).append(System.lineSeparator());

		out.append("iterations      : ").append(iterarionCount).append(System.lineSeparator());
		out.append("characters right: ").append(right).append(System.lineSeparator());
		return out.toString();

	}

}

package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import com.sun.deploy.util.StringUtils;
import de.tudarmstadt.gdi1.project.alphabet.*;
import de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipherImpl;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Mischa Holz
 */
public class MonoalphabeticKnownCiphertextCryptanalysisImpl implements MonoalphabeticKnownCiphertextCryptanalysis, GeneticAnalysis {

	protected List<Individual> currentGeneration;
	protected int stableGenerations;
	protected int numberOfGenerations;
	protected String cipherText;
	protected Dictionary dictionary;

	protected DistributionImpl cipherDistribution;
	protected double averageWordLength;

	public static int POPULATION_SIZE = 20;
	public static int INIT_POPULATION_SIZE = 10;
	public static int FITTEST_SIZE = 10;
	public static int STABLE_GENERATIONS = 5000;

	public MonoalphabeticKnownCiphertextCryptanalysisImpl() {
		currentGeneration = new ArrayList<Individual>();
		stableGenerations = 0;
		numberOfGenerations = 0;
		cipherText = "";
		dictionary = null;
		averageWordLength = 0;
		cipherDistribution = null;
	}

	/**
	 * Attack to determine the used key based on a given ciphertext and a given
	 * distribution on the alphabet.
	 *
	 * @param ciphertext   the ciphertext
	 * @param distribution the distribution
	 * @return the key, a part of the key, or null
	 */
	@Override
	public Object knownCiphertextAttack(String ciphertext, Distribution distribution) {
		return null;
	}

	/**
	 * Attack to determine the used key based on a ciphertext and a given
	 * dictionary.
	 *
	 * @param ciphertext the ciphertext
	 * @param dictionary the dictionary
	 * @return the key, a part of the key, or null
	 */
	@Override
	public Object knownCiphertextAttack(String ciphertext, Dictionary dictionary) {
		return null;
	}

	/**
	 * Attack to determine the used key based on a ciphertext, distribution and
	 * a dictionary.
	 *
	 * @param ciphertext
	 * @param distribution
	 * @param dictionary
	 * @return
	 */
	@Override
	public char[] knownCiphertextAttack(String ciphertext, Distribution distribution, Dictionary dictionary) {
		return new char[0];
	}

	/**
	 * Attack to determine the used key based on a ciphertext, distribution and
	 * a dictionary. In addition an oracle is provided that allows to verify
	 * whether a decryption is the correct one.
	 *
	 * @param ciphertext
	 * @param distribution
	 * @param dictionary
	 * @param validateDecryptionOracle
	 * @return
	 */
	@Override
	public char[] knownCiphertextAttack(String ciphertext, Distribution distribution, Dictionary dictionary, ValidateDecryptionOracle validateDecryptionOracle) {
		currentGeneration = prepareInitialGeneration(ciphertext, distribution.getAlphabet(), distribution, INIT_POPULATION_SIZE);
		stableGenerations = 0;
		numberOfGenerations = 0;
		averageWordLength = 0;
		cipherDistribution = null;

		Individual lastBest = getBestIndividual();
		Random rnd = new Random();
		this.cipherText = ciphertext;

		while(stableGenerations < STABLE_GENERATIONS) {
			MonoalphabeticCipherImpl crypto = new MonoalphabeticCipherImpl(distribution.getAlphabet(), getBestIndividual().getAlphabet());
			if(validateDecryptionOracle.isCorrect(crypto.decrypt(ciphertext))) {
				break;
			}
			if(!lastBest.getAlphabet().equals(getBestIndividual().getAlphabet())) {
				stableGenerations = 0;
			}

			lastBest = getBestIndividual();
			List<Individual> survivors = computeSurvivors(ciphertext, distribution.getAlphabet(), currentGeneration, distribution, dictionary, FITTEST_SIZE);
			currentGeneration = generateNextGeneration(survivors, POPULATION_SIZE, rnd, distribution.getAlphabet(), distribution, dictionary);
			currentGeneration.add(lastBest);
			Collections.sort(currentGeneration, new IndividualComparator());

			stableGenerations++;
			numberOfGenerations++;
		}

		return getBestIndividual().getAlphabet().asCharArray();
	}

	public Individual getBestIndividual() {
		Collections.sort(currentGeneration, new IndividualComparator());
		return currentGeneration.get(0);
	}

	/**
	 * Returns a description of the current state of the algorithm
	 *
	 * @param sourceAlphabet
	 * @param targetKey
	 * @return a description of the current state
	 */
	@Override
	public String getState(Alphabet sourceAlphabet, Alphabet targetKey) {
		if(currentGeneration.isEmpty()) {
			return "Not started";
		}
		int correct = 0;
		String correctString = "[";
		for(int i = 0; i < targetKey.size(); i++) {
			if(targetKey.getChar(i) == getBestIndividual().getAlphabet().getChar(i)) {
				correct++;
				correctString += "x";
			} else {
				correctString += "o";
			}
		}
		correctString += "]";
		return  "target      : " + targetKey + "\n" +
				"best guess  : " + getBestIndividual().getAlphabet() + "\n" +
				"correct     : " + correctString + " (" + correct + ")\n" +
				"best fitness: " + getBestIndividual().getFitness() + "\n" +
				"stable for  : " + stableGenerations + "\n" +
				"generation  : " + numberOfGenerations + "\n";
	}

	/**
	 * Constructs the initial generation of size populationSize.
	 *
	 * @param ciphertext     the ciphertext
	 * @param alphabet       the alphabet
	 * @param distribution   the distribution
	 * @param populationSize the population
	 * @return
	 */
	@Override
	public List<Individual> prepareInitialGeneration(String ciphertext, Alphabet alphabet, Distribution distribution, int populationSize) {
		ArrayList<Individual> ret = new ArrayList<Individual>();
		DistributionImpl cypherDist = new DistributionImpl(alphabet, ciphertext, 1);
		DistributionImpl clearDist = (DistributionImpl) distribution;
		Random rnd = new Random();

		for(int i = 0; i < populationSize; i++) {
			String guess = "";
			for(Character c : alphabet) {
				String cs = "" + c;
				int rank = (int)((float)clearDist.getRank(cs) + ((rnd.nextBoolean()) ? 1f : -1f) * ((float)i / (float)populationSize) * rnd.nextDouble() * 10);
				if(rank == 0) {
					guess += " ";
					continue;
				}
				String cypherGram = cypherDist.getByRank(1, rank);
				if(cypherGram == null || guess.contains(cypherGram)) {
					guess += " ";
					continue;
				}
				guess += cypherDist.getByRank(1, rank);
			}

			ArrayList<Character> candidates = new ArrayList<Character>();
			for(Character alphabetChar : alphabet) {
				String alphabetCharString = "" + alphabetChar;
				if(!guess.contains(alphabetCharString)) {
					candidates.add(alphabetChar);
				}
			}
			while(guess.contains(" ")) {
				int index = rnd.nextInt(candidates.size());
				String replaceString = "" + candidates.get(index);
				candidates.remove(index);
				guess = guess.replaceFirst(" ", replaceString);
			}

			IndividualImpl newInd = new IndividualImpl(new AlphabetImpl(guess));
			if(this.dictionary != null) {
				computeFitness(newInd, cipherText, alphabet, distribution, this.dictionary);
			}
			ret.add(newInd);
		}

		return ret;
	}

	/**
	 * Given the list of survivors and a target size, this method computes the
	 * next generation.
	 *
	 * @param survivors      the list of survivors
	 * @param populationSize the size of the next generation
	 * @param random         an instance of {@link java.util.Random} for generating randomness
	 * @param alphabet       the alphabet
	 * @param distribution   the distribution
	 * @param dictionary     the dictionary
	 * @return
	 */
	@Override
	public List<Individual> generateNextGeneration(List<Individual> survivors, int populationSize, Random random, Alphabet alphabet, Distribution distribution, Dictionary dictionary) {
		ArrayList<Individual> ret = new ArrayList<Individual>();

		ArrayList<Individual> biasedSurvivor = new ArrayList<Individual>();
		for(int i = 0; i < survivors.size(); i++) {
			int amount = (int)(-(10f / (float)survivors.size()) * (float)i + 10f);
			for(int j = 0; j < amount; j++) {
				biasedSurvivor.add(survivors.get(i));
			}
		}

		for(int i = 0; i < populationSize; i++) {
			int index = random.nextInt(biasedSurvivor.size());
			Individual ind = biasedSurvivor.get(index);
			String newAlphabet = new String(ind.getAlphabet().asCharArray());

			int switchAmount = random.nextInt(5);
			for(int j = 0; j < switchAmount; j++) {
				int index1 = random.nextInt(newAlphabet.length());
				int index2 = random.nextInt(newAlphabet.length());
				char[] cAlphabet = newAlphabet.toCharArray();
				char temp = cAlphabet[index1];
				cAlphabet[index1] = cAlphabet[index2];
				cAlphabet[index2] = temp;
				newAlphabet = new String(cAlphabet);
			}

			IndividualImpl newInd = new IndividualImpl(new AlphabetImpl(newAlphabet));
			if(!cipherText.equals("")) {
				computeFitness(newInd, cipherText, alphabet, distribution, dictionary);
			}
			ret.add(newInd);
		}

		return ret;
	}

	/**
	 * Given the current generation this method computes the list of survivors.
	 *
	 * @param ciphertext    the ciphertext
	 * @param alphabet      the alphabet
	 * @param generation    the current generation
	 * @param distribution  the distribution
	 * @param dictionary    the dictionary
	 * @param nrOfSurvivors the number of survivors
	 * @return a list containing nrOfSurvivors many members from the generation
	 */
	@Override
	public List<Individual> computeSurvivors(String ciphertext, Alphabet alphabet, List<Individual> generation, Distribution distribution, Dictionary dictionary, int nrOfSurvivors) {
		ArrayList<Individual> ret = new ArrayList<Individual>();
		for(Individual ind : generation) {
			IndividualImpl newInd = new IndividualImpl(ind.getAlphabet());
			computeFitness(newInd, ciphertext, alphabet, distribution, dictionary);
			ret.add(newInd);
		}

		Collections.sort(ret, new IndividualComparator());
		return ret.subList(0, (nrOfSurvivors >= ret.size()) ? ret.size() : nrOfSurvivors);
	}

	/**
	 * Given an individual this method computes its fitness.
	 *
	 * @param individual   the individual
	 * @param ciphertext   the ciphertext
	 * @param alphabet     the alphabet
	 * @param distribution the distribution
	 * @param dictionary   the dictionary
	 * @return
	 */
	@Override
	public double computeFitness(Individual individual, String ciphertext, Alphabet alphabet, Distribution distribution, Dictionary dictionary) {
		MonoalphabeticCipherImpl crypto = new MonoalphabeticCipherImpl(alphabet, individual.getAlphabet());

		int length = 1;
		while(true) {
			List<String> grams = distribution.getSorted(length);
			if(grams == null) {
				length--;
				break;
			}
			length++;
		}

		if(this.cipherDistribution == null) {
			cipherDistribution = new DistributionImpl(alphabet, ciphertext, length);
		}
		double fitness = 0;
		double factor = 5;

		if(this.averageWordLength == 0) {
			for(String word : dictionary) {
				this.averageWordLength += word.length();
			}
			this.averageWordLength /= dictionary.size();
		}

		for(String word : dictionary) {
			String encryptedWord = crypto.encrypt(word);
			if(ciphertext.contains(encryptedWord)) {
				fitness = fitness + factor * ((double)word.length() / averageWordLength);
			}
		}
		
//		This loop takes a really long time without doing much. Most tests were faster with this commented out.
//		for(int i = 1; i <= length; i++) {
//			List<String> grams = distribution.getSorted(length);
//			int currentRank = 1;
//			for(String gram : grams) {
//				int rank = cipherDistribution.getRank(crypto.encrypt(gram));
//				if(rank == currentRank) {
//					fitness = fitness + (double)length;
//				}
//
//
//				currentRank++;
//			}
//		}

		if(individual instanceof IndividualImpl) {
			((IndividualImpl) individual).setFitness(fitness);
		}

		return fitness;
	}
}

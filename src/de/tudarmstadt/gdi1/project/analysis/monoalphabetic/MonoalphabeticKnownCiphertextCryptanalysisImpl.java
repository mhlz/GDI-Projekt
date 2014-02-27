package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.*;
import de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipherImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Reconstruct the key of a monoalphabetic cipher using a genetic algorithm
 *
 * @author Mischa Holz
 */
public class MonoalphabeticKnownCiphertextCryptanalysisImpl implements MonoalphabeticKnownCiphertextCryptanalysis, GeneticAnalysis {

	/**
	 * Every individual who is alive
	 */
	protected List<Individual> currentGeneration;

	/**
	 * Counts the generations in which the best individual hasn't changed
	 */
	protected int stableGenerations;

	/**
	 * Counts the generations it took to calculate the key so far
	 */
	protected int numberOfGenerations;

	/**
	 * Saves the cipher text since it isn't available everywhere
	 */
	protected String cipherText;

	/**
	 * Saves the dictionary since it isn't available everywhere
	 */
	protected Dictionary dictionary;

	/**
	 * Saves the average word length of the dictionary to prevent having to calculate it too often
	 */
	protected double averageWordLength;

	/**
	 * How many new individuals will be generated for the next generation
	 */
	public static int POPULATION_SIZE = 10;

	/**
	 * How many individuals will be generated at the beginning
	 */
	public static int INIT_POPULATION_SIZE = 10;

	/**
	 * How many individuals reproduce
	 */
	public static int FITTEST_SIZE = 3;

	/**
	 * if the best individual hasn't change in so many generations, just give up
	 */
	public static int STABLE_GENERATIONS = 5000;

	/**
	 * This is the maximum amount of letter switches that will happen in one individual when the next generation
	 * is calculated
	 */
	public static int SWITCH_AMOUNT = 5;

	/**
	 * Likelihood of the best survivor being chosen as source for an individual of the next generation in percent.
	 * E.g. 10 equals 10% likelihood
	 */
	public static float BEST_SURVIVOR_PERCENT = 10f;

	/**
	 * Default constructor that initializes some values in the class
	 */
	public MonoalphabeticKnownCiphertextCryptanalysisImpl() {
		currentGeneration = new ArrayList<Individual>();
		stableGenerations = 0;
		numberOfGenerations = 0;
		cipherText = "";
		dictionary = null;
		averageWordLength = 0;
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
		// initialize all variables needed to calculate the key
		currentGeneration = prepareInitialGeneration(ciphertext, distribution.getAlphabet(), distribution, INIT_POPULATION_SIZE);
		stableGenerations = 0;
		numberOfGenerations = 0;
		averageWordLength = 0;
		Individual lastBest = getBestIndividual();
		Random rnd = new Random();
		this.cipherText = ciphertext;
		this.dictionary = null;

		// start the loop
		while(stableGenerations < STABLE_GENERATIONS) {
			// ask the oracle to see if the current best individual has a right key
			MonoalphabeticCipherImpl crypto = new MonoalphabeticCipherImpl(distribution.getAlphabet(), getBestIndividual().getAlphabet());
			if(validateDecryptionOracle.isCorrect(crypto.decrypt(ciphertext))) {
				break;
			}

			// reset the counter for stable generations, in case the best individual has changed
			if(!lastBest.getAlphabet().equals(getBestIndividual().getAlphabet())) {
				stableGenerations = 0;
			}

			// calculate the next generation
			// save the current best individual
			lastBest = getBestIndividual();
			// get a list of the survivors
			List<Individual> survivors = computeSurvivors(ciphertext, distribution.getAlphabet(), currentGeneration, distribution, dictionary, FITTEST_SIZE);
			// create a new population from the survivors
			currentGeneration = generateNextGeneration(survivors, POPULATION_SIZE, rnd, distribution.getAlphabet(), distribution, dictionary);
			// add the survivors of the last generation to this generation
			currentGeneration.addAll(survivors);
			// sort them according to their fitness
			Collections.sort(currentGeneration, new IndividualComparator());

			// increment the counters
			stableGenerations++;
			numberOfGenerations++;
		}

		// when we're here, we either decrypted the text correctly or didn't make meaningful progress for a while
		// return the currently best approximation of the key
		return getBestIndividual().getAlphabet().asCharArray();
	}

	/**
	 * Gets the best individual of the current generation
	 *
	 * @return Individual with the highest fitness
	 */
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
		return "target      : " + targetKey + "\n" +
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
	 * @return List with initial individuals
	 */
	@Override
	public List<Individual> prepareInitialGeneration(String ciphertext, Alphabet alphabet, Distribution distribution, int populationSize) {
		// initialize the return array and the variables needed to make good guesses
		ArrayList<Individual> ret = new ArrayList<Individual>();
		DistributionImpl cipherDist = new DistributionImpl(alphabet, ciphertext, 1);
		DistributionImpl clearDist = (DistributionImpl) distribution;
		Random rnd = new Random();

		for(int i = 0; i < populationSize; i++) {
			// initialize the string that will hold the current alphabet
			String guess = "";
			for(Character c : alphabet) {
				// go through every character of the alphabet
				String cs = "" + c; // convert to string
				// get the rank of the character in the clear text distribution + or - a random value between 0 and 10
				// this value is biased towards 0 and gets progressively larger for bigger population sizes to
				// get good guesses in the beginning
				int rank = (int) ((float) clearDist.getRank(cs) + ((rnd.nextBoolean()) ? 1f : -1f) * ((float) i / (float) populationSize) * rnd.nextDouble() * 10);
				if(rank == 0) {
					guess += " "; // save a space in case the letter doesn't appear in the distribution
					continue;
				}
				// get the letter with the same rank in the cipher
				// this should approximately be the corresponding letter since they should both occur equally often
				String cypherGram = cipherDist.getByRank(1, rank);
				// in case the letter is already part of the alphabet or there's no letter with that rank, save a space
				if(cypherGram == null || guess.contains(cypherGram)) {
					guess += " ";
					continue;
				}
				// add the letter to the current guess
				guess += cypherGram;
			}

			// at this point guess is mostly filled with letters, but some spaces are still in there
			// make a list with every character that occurs in the alphabet but not in guess
			ArrayList<Character> candidates = new ArrayList<Character>();
			for(Character alphabetChar : alphabet) {
				String alphabetCharString = "" + alphabetChar;
				if(!guess.contains(alphabetCharString)) {
					candidates.add(alphabetChar);
				}
			}
			// as long as guess still has empty spaces to fill add a random letter from the candidates
			// this will fill the string up until it has as many letters as alphabet
			while(guess.contains(" ")) {
				int index = rnd.nextInt(candidates.size());
				String replaceString = "" + candidates.get(index);
				candidates.remove(index);
				guess = guess.replaceFirst(" ", replaceString);
			}

			// create a new individual and add it to the return value
			IndividualImpl newInd = new IndividualImpl(new AlphabetImpl(guess));
			ret.add(newInd);
		}

		// return the created list
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
		// initialize the return list
		ArrayList<Individual> ret = new ArrayList<Individual>();

		// create an arraylist which contains the better individuals more often, thus making it more likely
		// that they get chosen
		// this is based on a linear scale in such a way that the best individual will have a chance of 10% of being
		// chosen as source for the next alphabet
		ArrayList<Individual> biasedSurvivor = new ArrayList<Individual>();
		for(int i = 0; i < survivors.size(); i++) {
			int amount = (int) (-(BEST_SURVIVOR_PERCENT / (float) survivors.size()) * (float) i + BEST_SURVIVOR_PERCENT);
			for(int j = 0; j < amount; j++) {
				biasedSurvivor.add(survivors.get(i));
			}
		}

		// create new individuals
		for(int i = 0; i < populationSize; i++) {
			// choose a random survivor from the biased list
			int index = random.nextInt(biasedSurvivor.size());
			Individual ind = biasedSurvivor.get(index);
			// convert the alphabet into a string
			String newAlphabet = new String(ind.getAlphabet().asCharArray());

			// make a random amount of character swaps
			int switchAmount = random.nextInt(SWITCH_AMOUNT);
			for(int j = 0; j < switchAmount; j++) {
				// choose two letters that are being swapped
				int index1 = random.nextInt(newAlphabet.length());
				int index2 = random.nextInt(newAlphabet.length());
				// convert to char array for easier handling
				char[] cAlphabet = newAlphabet.toCharArray();
				// save old character
				char temp = cAlphabet[index1];
				// assign new character to old position
				cAlphabet[index1] = cAlphabet[index2];
				// assign old character to new position
				cAlphabet[index2] = temp;
				// create a string
				newAlphabet = new String(cAlphabet);
			}

			// create a new individual and calculate its fitness if possible
			IndividualImpl newInd = new IndividualImpl(new AlphabetImpl(newAlphabet));
			if(!cipherText.equals("")) {
				computeFitness(newInd, cipherText, alphabet, distribution, dictionary);
			}
			// add it to the list of the next generation
			ret.add(newInd);
		}

		// return list
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
		// initialize return arraylist
		ArrayList<Individual> ret = new ArrayList<Individual>();

		// calculate the fitness for every individual and add them to the return array
		for(Individual ind : generation) {
			computeFitness(ind, ciphertext, alphabet, distribution, dictionary);
			ret.add(ind);
		}

		// sort the individuals using the comparator
		Collections.sort(ret, new IndividualComparator());
		// return a sublist, starting from the best to either nrOfSurvivors or ret.size(), whichever is smaller
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
		// create a cipher to test
		MonoalphabeticCipherImpl crypto = new MonoalphabeticCipherImpl(alphabet, individual.getAlphabet());

		// initialize values
		double fitness = 0;

		// calculate the average word length if this hasn't happened yet
		if(this.averageWordLength == 0) {
			for(String word : dictionary) {
				this.averageWordLength += word.length();
			}
			this.averageWordLength /= dictionary.size();
		}

		// go through every word in the dictionary
		for(String word : dictionary) {
			// encrypt the word using the individual's key
			String encryptedWord = crypto.encrypt(word);
			// check if the encrypted word appears in the cipher text
			if(ciphertext.contains(encryptedWord)) {
				// if it does add a weighted value to the fitness.
				// longer words award more points than shorter ones based on the average length of the words in the
				// dictionary
				fitness = fitness + ((double) word.length() / averageWordLength);
			}
		}

		// if this individual uses our implementation set its fitness value too
		if(individual instanceof IndividualImpl) {
			((IndividualImpl) individual).setFitness(fitness);
		}

		// return fitness
		return fitness;
	}
}

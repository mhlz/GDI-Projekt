package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;

/**
 * Represents one individual for the genetic algorithm
 *
 */
public class IndividualImpl implements Individual {

	/**
	 * Key of the individual
	 */
	protected Alphabet alphabet;

	/**
	 * Fitness value
	 */
	protected double fitness;

	/**
	 * Constructor using only an alphabet. Set fitness later
	 *
	 * @param alphabet key of this individual
	 */
	public IndividualImpl(Alphabet alphabet) {
		this.alphabet = alphabet;
		this.fitness = 0;
	}

	/**
	 * Constructor using alphabet and fitness
	 *
	 * @param alphabet key of this individual
	 * @param fitness  fitness of this individual
	 */
	public IndividualImpl(Alphabet alphabet, double fitness) {
		this.alphabet = alphabet;
		this.fitness = fitness;
	}

	/**
	 * Setter for the key
	 *
	 * @param alphabet key of this individual
	 */
	public void setAlphabet(Alphabet alphabet) {
		this.alphabet = alphabet;
	}

	/**
	 * Setter for the fitness
	 *
	 * @param fitness fitness of this individual
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * @return this individual's alphabet
	 */
	@Override
	public Alphabet getAlphabet() {
		return alphabet;
	}

	/**
	 * @return the individual's fitness
	 */
	@Override
	public double getFitness() {
		return fitness;
	}
}

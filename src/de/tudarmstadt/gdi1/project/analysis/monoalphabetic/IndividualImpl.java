package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;

/**
 * @author Mischa Holz
 */
public class IndividualImpl implements Individual {

	protected Alphabet alphabet;
	protected double fitness;

	public IndividualImpl(Alphabet alphabet) {
		this.alphabet = alphabet;
		this.fitness = 0;
	}

	public IndividualImpl(Alphabet alphabet, double fitness) {
		this.alphabet = alphabet;
		this.fitness = fitness;
	}

	public void setAlphabet(Alphabet alphabet) {
		this.alphabet = alphabet;
	}

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

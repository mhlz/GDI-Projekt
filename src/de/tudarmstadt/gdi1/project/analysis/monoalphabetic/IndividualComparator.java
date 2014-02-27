package de.tudarmstadt.gdi1.project.analysis.monoalphabetic;

import java.util.Comparator;

/**
 * @author Mischa Holz
 */
public class IndividualComparator implements Comparator<Individual> {

	/**
	 * Compares two individuals based on their fitness
	 *
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the
	 * first argument is less than, equal to, or greater than the
	 * second.
	 * @throws NullPointerException if an argument is null and this
	 *                              comparator does not permit null arguments
	 * @throws ClassCastException   if the arguments' types prevent them from
	 *                              being compared by this comparator.
	 */
	@Override
	public int compare(Individual o1, Individual o2) {
		if (o1.getAlphabet().equals(o2.getAlphabet())) {
			return 0;
		} else {
			return Double.compare(o2.getFitness(), o1.getFitness());
		}
	}
}

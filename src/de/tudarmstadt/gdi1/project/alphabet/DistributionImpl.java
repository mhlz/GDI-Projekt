package de.tudarmstadt.gdi1.project.alphabet;

import de.tudarmstadt.gdi1.project.utils.Utils;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.*;

/**
 * Calculates the distribution of letters and letter sequences
 *
 */
public class DistributionImpl implements Distribution {

	/**
	 * Alphabet that was used for this distribution
	 */
	protected Alphabet src;

	/**
	 * Saves the sequences sorted by their length
	 */
	protected TreeMap<Integer, ArrayList<GramFrequencyPair>> weightedGrams;

	/**
	 * This class is a pair of a frequency and the letter sequence.
	 * Can't really use a hashmap here since we have to be able to look this up in both directions
	 */
	protected class GramFrequencyPair {
		public double freq;
		public String gram;

		/**
		 * constructs a pair
		 *
		 * @param gram the string
		 * @param freq the frequency
		 */
		public GramFrequencyPair(String gram, double freq) {
			this.freq = freq;
			this.gram = gram;
		}

		/**
		 * gives a readable output
		 *
		 * @return gram <=> freq
		 */
		public String toString() {
			return gram + " <=> " + freq;
		}
	}

	/**
	 * Compare two frequency/sequence pairs to find out which one is more frequent in the text
	 * In case the frequency is the same, use the natural order
	 */
	protected class FrequencyComparator implements Comparator<GramFrequencyPair> {

		/**
		 * compares to GramFrequencyPairs
		 *
		 * @param o1 the first to compare
		 * @param o2 the second to compare
		 * @return -1 if the first is more frequent, 0 if they are equal frequent, 1 if the second is more frequent
		 */
		@Override
		public int compare(GramFrequencyPair o1, GramFrequencyPair o2) {
			if(o1.freq > o2.freq) {
				return -1;
			} else if(o1.freq == o2.freq) {
				return o1.gram.compareTo(o2.gram);
			} else {
				return 1;
			}
		}
	}

	/**
	 * This constructor calculates frequencies for sequences with the length 1 (single letter).
	 * This is equivalent of using the other constructor with ngramsize = 1
	 *
	 * @param source Source alphabet
	 * @param text   Source text
	 */
	public DistributionImpl(Alphabet source, String text) {
		this(source, text, 1);
	}

	/**
	 * This constructor calculates frequencies for sequences with a length of up to (and including) ngramsize
	 *
	 * @param source    Source alphabet
	 * @param text      Source text
	 * @param ngramsize The size of the sequences for which the frequencies should be calculated
	 */
	public DistributionImpl(Alphabet source, String text, int ngramsize) {
		// normalize the text first
		text = source.normalize(text);
		// initialize the map and save the source alphabet
		weightedGrams = new TreeMap<Integer, ArrayList<GramFrequencyPair>>();
		src = source;

		// create an array containing all the sizes that we need.
		// this is used to call utils.ngramize in a bit
		int[] sizes = new int[ngramsize];
		for(int i = 0; i < ngramsize; i++) {
			sizes[i] = i + 1;
		}
		Utils utils = new UtilsImpl();
		// split the text into sequences until the given length
		Map<Integer, List<String>> grams = utils.ngramize(text, sizes);

		// for every length in the array
		for(int length : sizes) {
			// create a hashmap to save absolute frequencies
			HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
			// go through all grams of the current length
			for(String gram : grams.get(length)) {
				// in case the sequence isn't in the map yet, put it in with an absolute frequency of 1
				// otherwise increment the frequency
				if(frequencies.get(gram) == null) {
					frequencies.put(gram, 1);
				} else {
					frequencies.put(gram, frequencies.get(gram) + 1);
				}
			}
			// create an arraylist of pairs, to save the pairs of frequencies and sequences
			ArrayList<GramFrequencyPair> sortedList = new ArrayList<GramFrequencyPair>();
			// calculate the relative frequency and save it in a pair
			// add the list to the pair
			for(String gram : frequencies.keySet()) {
				double freq = (double) frequencies.get(gram) / (double) grams.get(length).size();
				GramFrequencyPair pair = new GramFrequencyPair(gram, freq);
				sortedList.add(pair);
			}
			// sort the list using the comparator and save it in the map
			Collections.sort(sortedList, new FrequencyComparator());
			weightedGrams.put(length, sortedList);
		}
	}

	/**
	 * retrieve all the ngrams of the given length from all the learned strings,
	 * sorted by their frequency
	 *
	 * @param length the ngram length, so 1 means only a character 2 stands for
	 *               bigrams and so on.
	 * @return a descending sorted list that contains all the ngrams sorted by
	 * their frequency
	 */
	@Override
	public List<String> getSorted(int length) {
		// get all grams of a certain length and add the strings of the pairs to a list
		// return the list
		ArrayList<String> ret = new ArrayList<String>();
		if(weightedGrams.get(length) == null) {
			return null;
		}
		for(GramFrequencyPair pair : weightedGrams.get(length)) {
			ret.add(pair.gram);
		}
		return ret;
	}

	/**
	 * Gets the frequency to a given key. If the key is longer than the created
	 * ngrams or if the key was never seen the frequency is 0.
	 *
	 * @param key the character, bigram, trigram,... we want the frequency for
	 * @return the frequency of the given character, bigram, trigram,... in all
	 * the learned texts
	 */
	@Override
	public double getFrequency(String key) {
		// go through all sequences with the length of the key
		// if the string of the pair is equal to the key return its frequency
		for(GramFrequencyPair pair : weightedGrams.get(key.length())) {
			if(pair.gram.equals(key)) {
				return pair.freq;
			}
		}
		return 0;
	}

	/**
	 * Return the rank of a given key. 1 is the highest possible rank. If the key can't be found, returns 0
	 *
	 * @param key Letter sequence that the rank is searched for
	 * @return Rank of that letter sequence
	 */
	public int getRank(String key) {
		// 1 is the highest possible rank
		// go through the sorted list of sequences and return the rank of the one that matches the key
		// return 0 if
		int i = 1;
		for(GramFrequencyPair pair : weightedGrams.get(key.length())) {
			if(pair.gram.equals(key)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	/**
	 * @return the alphabet of the distribution
	 */
	@Override
	public Alphabet getAlphabet() {
		return src;
	}

	/**
	 * retrieves the string with its learned frequency from the distribution, by
	 * its size and frequency rank.
	 *
	 * @param length the size of the ngram
	 * @param rank   the rank where we want to look at (1 = highest rank)
	 * @return the ngram of the given size that is on the given rank in its
	 * distribution or null if the ngramsize is bigger than the maximum
	 * learned ngram size or the rank is higher than the number of
	 * learned ngrams
	 */
	@Override
	public String getByRank(int length, int rank) {
		// go through the sorted list of sequences and return the string of the sequence that matches
		// the given rank
		// return null if the rank is not valid
		int i = 1;
		for(GramFrequencyPair pair : weightedGrams.get(length)) {
			if(i == rank) {
				return pair.gram;
			}
			i++;
		}
		return null;
	}
}

package de.tudarmstadt.gdi1.project.alphabet;

import de.tudarmstadt.gdi1.project.utils.Utils;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.*;

/**
 * Calculates the distribution of small letter grams
 *
 * @author Mischa Holz
 */
public class DistributionImpl implements Distribution {

	/**
	 * Source alphabet
	 */
	protected Alphabet src;

	/**
	 * Map which saves the grams according to their length
	 */
	protected TreeMap<Integer, ArrayList<GramFrequencyPair>> weightedGrams;

	/**
	 * This class represents a gram frequency pair. This is used in the map to save a string and its frequency together
	 */
	protected class GramFrequencyPair {
		public double freq;
		public String gram;

		/**
		 * Default constructor
		 * @param gram String
		 * @param freq Frequency of occurence of the string
		 */
		public GramFrequencyPair(String gram, double freq) {
			this.freq = freq;
			this.gram = gram;
		}

		/**
		 * Returns a formatted string representing this pair
		 * @return
		 */
		public String toString() {
			return gram + " <=> " + freq;
		}
	}

	/**
	 * Compares two GramFrequencyPairs according to their frequency and equalness
	 */
	protected class FrequencyComparator implements Comparator<GramFrequencyPair> {
		/**
		 * Compares two GramFrequencyPairs according to their frequency and equalness
		 * @param o1 GramFreqPair1
		 * @param o2 GramFreqPair12
		 * @return -1 if o1 is more frequent than o2, 0 if the strings are the same and 1 otherwise
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
	 * Default constructor which only calculates the frequencies of single letters
	 * @param source Source alphabet
	 * @param text A text containing lots of letters
	 */
	public DistributionImpl(Alphabet source, String text) {
		this(source, text, 1);
	}

	/**
	 * Constructor which calculates the frequencies of grams with lengths <= gramsize
	 * @param source Source alphabet
	 * @param text A text containing lots of letters
	 * @param ngramsize length of the grams
	 */
	public DistributionImpl(Alphabet source, String text, int ngramsize) {
		// initialize properties
		text = source.normalize(text); // normalize the text
		weightedGrams = new TreeMap<Integer, ArrayList<GramFrequencyPair>>();
		src = source;

		// create an array containing the sizes wanted
		// e.g. if ngramsize is 3 this will create an array { 1, 2, 3 }
		int[] sizes = new int[ngramsize];
		for(int i = 1; i <= ngramsize; i++) {
			sizes[i - 1] = i;
		}

		// create a utils object and use it to split the text into ngrams
		Utils utils = new UtilsImpl();
		Map<Integer, List<String>> grams = utils.ngramize(text, sizes);
		// calculate the frequency of the ngrams of every length separately
		for(int length : sizes) {
			// create a hashmap which saves every string and its absolute frequency in the text
			HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
			// go through all grams of the current length
			for(String gram : grams.get(length)) {
				// if the gram isn't in the hashmap yet, put it in and set its frequency to 1
				// otherwise increment its frequency
				if(frequencies.get(gram) == null) {
					frequencies.put(gram, 1);
				} else {
					frequencies.put(gram, frequencies.get(gram) + 1);
				}
			}

			// create an arraylist of gramfrequencypairs which will be used for sorting
			ArrayList<GramFrequencyPair> sortedList = new ArrayList<GramFrequencyPair>();
			// go through every gram and calculate the relative frequency
			for(String gram : frequencies.keySet()) {
				double freq = (double) frequencies.get(gram) / (double) grams.get(length).size();
				// put the pair in the arraylist
				GramFrequencyPair pair = new GramFrequencyPair(gram, freq);
				sortedList.add(pair);
			}
			// sort the list using the comparator
			Collections.sort(sortedList, new FrequencyComparator());
			// put the list in the map of weightedGrams
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
		for(GramFrequencyPair pair : weightedGrams.get(key.length())) {
			if(pair.gram.equals(key)) {
				return pair.freq;
			}
		}
		return 0;
	}

	/**
	 * Gets the rank of a given key
	 * @param key the character, bigram, trigram,... we want the rank for
	 * @return the rank of the given character, bigram, trigram,... The highest rank is 1 and 0 means that this key doesn't exist
	 */
	public int getRank(String key) {
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

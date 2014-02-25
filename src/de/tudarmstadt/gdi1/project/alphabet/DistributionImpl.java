package de.tudarmstadt.gdi1.project.alphabet;

import de.tudarmstadt.gdi1.project.utils.Utils;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;

import java.util.*;

/**
 * @author Mischa Holz
 */
public class DistributionImpl implements Distribution {

	protected Alphabet src;

	protected TreeMap<Integer, ArrayList<GramFrequencyPair>> weightedGrams;

	protected class GramFrequencyPair {
		public double freq;
		public String gram;

		public GramFrequencyPair(String gram, double freq) {
			this.freq = freq;
			this.gram = gram;
		}

		public String toString() {
			return gram + " <=> " + freq;
		}
	}

	protected class FrequencyComparator implements Comparator<GramFrequencyPair> {
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

	public DistributionImpl(Alphabet source, String text) {
		this(source, text, 1);
	}

	public DistributionImpl(Alphabet source, String text, int ngramsize) {
		text = source.normalize(text);
		weightedGrams = new TreeMap<Integer, ArrayList<GramFrequencyPair>>();
		src = source;

		int[] sizes = new int[ngramsize];
		for(int i = 1; i <= ngramsize; i++) {
			sizes[i - 1] = i;
		}
		Utils utils = new UtilsImpl();
		Map<Integer, List<String>> grams = utils.ngramize(text, sizes);
		for(int length : sizes) {
			HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
            if(length == 1) {
                for(Character c: source) {
                    frequencies.put(c.toString(), 0);
                }
            }
			// go through all grams of the current length
			for(String gram : grams.get(length)) {
				if(frequencies.get(gram) == null) {
					frequencies.put(gram, 1);
				} else {
					frequencies.put(gram, frequencies.get(gram) + 1);
				}
			}
			ArrayList<GramFrequencyPair> sortedMap = new ArrayList<GramFrequencyPair>();
			for(String gram : frequencies.keySet()) {
				double freq = (double) frequencies.get(gram) / (double) grams.get(length).size();
				GramFrequencyPair pair = new GramFrequencyPair(gram, freq);
				sortedMap.add(pair);
			}
			Collections.sort(sortedMap, new FrequencyComparator());
			weightedGrams.put(length, sortedMap);
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

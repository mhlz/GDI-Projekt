package de.tudarmstadt.gdi1.project.utils;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mischa Holz
 */
public class UtilsImpl implements Utils {
	/**
	 * transforms a text into a pretty printable format. This means that the
	 * text has in every line 6 space separated blocks with 10 characters each.
	 * <p/>
	 * So
	 * <p/>
	 * <pre>
	 * loremipsumdolorsitametconsecteturadipiscingelitvivamusquismassaestnuncelitelitdictumvelligulaiddapibuspretiumrisuscrasineuismodnisinu
	 * ncpharetradiamelitiaculishendreritnisitincidunteunullamfeugiatfermentumantequissuscipitestvehiculasitametnuncnonvehiculaenimduisatlib
	 * eroquisduidapibusfermentumuteuduinullamrutrumgravidadolorvelullamcorperleofermentumeuinliberovelitaccumsanvelpulvinarnecsagittisetmet
	 * usnullaanequevitaesemmalesuadaplaceratutegestasmetus
	 * </pre>
	 * <p/>
	 * will be
	 * <p/>
	 * <pre>
	 * ssaestnunc elitelitdi ctumvellig ulaiddapib uspretiumr isuscrasin
	 * euismodnis inuncphare tradiameli tiaculishe ndreritnis itincidunt
	 * eunullamfe ugiatferme ntumantequ issuscipit estvehicul asitametnu
	 * ncnonvehic ulaenimdui satliberoq uisduidapi busferment umuteuduin
	 * ullamrutru mgravidado lorvelulla mcorperleo fermentume uinliberov
	 * elitaccums anvelpulvi narnecsagi ttisetmetu snullaaneq uevitaesem
	 * malesuadap laceratute gestasmetu s
	 * </pre>
	 *
	 * @param ciphertext the text that should be pretty formated
	 * @return the pretty formatted text
	 */
	@Override
	public String toDisplay(String ciphertext) {
		return null;
	}

	/**
	 * Divides a string into ngrams of the given lengths
	 *
	 * @param text    the text that should be devided
	 * @param lengths the lengths of the ngrams we need
	 * @return lists that contain all the ngrams of a fixed size. The lists are
	 * maped to their ngram size in the result map.
	 */
	@Override
	public Map<Integer, List<String>> ngramize(String text, int... lengths) {
		HashMap<Integer, List<String>> ret = new HashMap<Integer, List<String>>();
		for(int length : lengths) {
			ArrayList<String> grams = new ArrayList<String>();
			for(int i = 0; i < text.length(); i++) {
				String gram = "";
				for(int j = 0; j < length && j + i < text.length(); j++) {
					gram += text.charAt(i + j);
				}
				if(gram.length() == length) {
					grams.add(gram);
				}
			}
			ret.put(length, grams);
		}
		return ret;
	}

	/**
	 * Returns the given alphabet shifted by pos positions to the left.
	 *
	 * @param alphabet the alphabet
	 * @param shift    the number of positions to shift
	 * @return the new shifted alphabet
	 */
	@Override
	public Alphabet shiftAlphabet(Alphabet alphabet, int shift) {
		return null;
	}

	/**
	 * Returns the given alphabet in reverse order (a,b,c,...,x,y,z) ->
	 * (z,y,x,...,c,b,a).
	 *
	 * @param alphabet the alphabet
	 * @return a new alphabet with the same characters but in reverse order
	 */
	@Override
	public Alphabet reverseAlphabet(Alphabet alphabet) {
		return null;
	}

	/**
	 * Checks if the given alphabets contain the same characters. This means
	 * they are a permutation of each other.
	 *
	 * @param alphabet1 the first alphabet
	 * @param alphabet2 the second alphabet
	 * @return if the alphabets are a permutation of each other
	 */
	@Override
	public boolean containsSameCharacters(Alphabet alphabet1, Alphabet alphabet2) {
		return false;
	}

	/**
	 * Given an alphabet, the method returns a new alphabet with characters
	 * randomly shuffled.
	 *
	 * @param alphabet the source alphabet
	 * @return a new alphabet containing the same characters as the source
	 * alphabet but in a random order.
	 */
	@Override
	public Alphabet randomizeAlphabet(Alphabet alphabet) {
		return null;
	}
}

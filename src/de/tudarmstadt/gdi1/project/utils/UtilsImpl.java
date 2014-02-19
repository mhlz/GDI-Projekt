package de.tudarmstadt.gdi1.project.utils;

import com.sun.deploy.util.StringUtils;
import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;

import java.security.SecureRandom;
import java.util.*;

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
        ArrayList<String> retList = new ArrayList<String>();

        //splitting up the text every 10th caracter
        while (ciphertext.length() > 10 ) {
            retList.add(ciphertext.substring(0, 10));
            ciphertext = ciphertext.substring(10,ciphertext.length());
        }
        retList.add(ciphertext);


        //joining all those 10 character long Strings bacl together with " " and a  System.lineSeparator() every 6th one
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < retList.size(); i++) {
            ret.append(retList.get(i));
            ret.append(" ");
            if((i + 1)% 6 == 0 && i < retList.size() - 1) {
                ret.append(System.lineSeparator());
            }
        }
        //deleting the last " "
        ret.deleteCharAt(ret.length() - 1);

        return ret.toString();
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

		// don't shift more than the alphabet is long
        shift = shift % alphabet.size();
		// in case the result was negative, bring it back to positives again by adding the size
        if(shift < 0) {
            shift += alphabet.size();
        }

		// get the alphabet as char array and shift it
        char[] tempArray = alphabet.asCharArray();
        Character[] destArray = new Character[tempArray.length];

        for(int i = shift; i < tempArray.length; i++){
            destArray[i - shift] = tempArray[i];
        }
        for(int i = 0; i < shift; i++){
            destArray[tempArray.length - shift + i] = tempArray[i];
        }
        return new AlphabetImpl(destArray);
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
		// reserve memory for the alphabet
        char[] temp = alphabet.asCharArray();
        Character[] ret = new Character[temp.length];
		// reverse it
        for(int i = 0; i < temp.length; i++) {
            ret[ret.length - i - 1] = temp[i];
        }
        return new AlphabetImpl(ret);
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
        if(alphabet1.size() != alphabet2.size()) {
            return false; // because the two alphabets aren't the same size
        }
        for(Character c:alphabet1.asCharArray()){
            if(!alphabet2.contains(c)) {
                return false; // one of the characters is missing in alphabet 2
            }
        }
        // we only have to check if all characters of alphabet 1 are in alphabet 2, because we allready now that they
        // are the same size, so if any character is in alphabet 2, that is not in alphabet 1 another character of
        // alphabet 1 must be missing in alphabet 2

        return true;
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
        SecureRandom rand = new SecureRandom();

        //converting the chars in the Alphabet to an ArrayList
        ArrayList<Character> chars = new ArrayList<Character>();
        for(Character c: alphabet.asCharArray()) {
            chars.add(c);
        }

        Character[] ret = new Character[alphabet.size()];

        //randomizing the alphabet using SecureRandom
        for(int i = 0; i < ret.length; i++) {
            ret[i] = chars.get((int) (chars.size() * rand.nextDouble()));
            chars.remove(ret[i]);
        }

        return new AlphabetImpl(ret);
	}

	/**
	 * Convert an array of chars to an array of Characters (since autoboxing isn't a thing for arrays)
	 * @param charArray char array to convert
	 * @return An array of Characters with the same content as the char array
	 */
	public Character[] toCharacterArray(char[] charArray) {
		Character[] ret = new Character[charArray.length];
		for(int i = 0; i < charArray.length; i++) {
			ret[i] = charArray[i];
		}

		return ret;
	}

	/**
	 * Convert an array of Characters to an array of chars (since autoboxing isn't a thing for arrays)
	 * @param characterArray Character array to convert
	 * @return An array of chars with the same content as the Character array
	 */
	public char[] toCharArray(Character[] characterArray) {
		char[] ret = new char[characterArray.length];
		for(int i = 0; i < characterArray.length; i++) {
			ret[i] = characterArray[i];
		}

		return ret;
	}
}

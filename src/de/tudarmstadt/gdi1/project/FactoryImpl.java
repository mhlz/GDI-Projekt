package de.tudarmstadt.gdi1.project;

import de.tudarmstadt.gdi1.project.alphabet.*;
import de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle;
import de.tudarmstadt.gdi1.project.analysis.caeser.CaesarCryptanalysis;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.Individual;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticCpaNpaCryptanalysis;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticCribCryptanalysis;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticKnownCiphertextCryptanalysis;
import de.tudarmstadt.gdi1.project.analysis.vigenere.VigenereCryptanalysis;
import de.tudarmstadt.gdi1.project.cipher.enigma.Enigma;
import de.tudarmstadt.gdi1.project.cipher.enigma.PinBoard;
import de.tudarmstadt.gdi1.project.cipher.enigma.ReverseRotor;
import de.tudarmstadt.gdi1.project.cipher.enigma.Rotor;
import de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipher;
import de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipherImpl;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.Caesar;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.KeywordMonoalphabeticCipher;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipher;
import de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic.PolyalphabeticCipher;
import de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic.Vigenere;
import de.tudarmstadt.gdi1.project.utils.Utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by reckter on 2/17/14.
 */
public class FactoryImpl implements Factory {
    /**
     * Constructs a {@link de.tudarmstadt.gdi1.project.alphabet.Distribution} from the given text for all ngrams of
     * size 1 to ngramsize. Only characters available in the alphabet should be
     * taken into consideration {@link de.tudarmstadt.gdi1.project.alphabet.Alphabet#normalize(String)}.
     *
     * @param source    the alphabet
     * @param text      the text to base the distribution on
     * @param ngramsize the maximum n-gram size
     * @return a distribution object
     */
    @Override
    public Distribution getDistributionInstance(Alphabet source, String text, int ngramsize) {
        return null;
    }

    /**
     * Constructs an {@link de.tudarmstadt.gdi1.project.alphabet.Alphabet} based on the (ordered) collection of
     * characters
     *
     * @param characters an ordered collection of characters
     * @return an alphabet based on the given collection of characters
     */
    @Override
    public Alphabet getAlphabetInstance(Collection<Character> characters) {
        return new AlphabetImpl(characters);
    }

    /**
     * Loads all valid words from the input into the dictionary. <br \>
     * A word is the character sequence that stands between a space and/or one
     * of the following characters: ',' '!' '?' '.'<br \>
     * A word is valid if it contains only characters that are part of the given
     * alphabet.
     *
     * @param alphabet the source alphabet
     * @param text
     */
    @Override
    public Dictionary getDictionaryInstance(Alphabet alphabet, String text) {
        return new DictionaryImpl(text, alphabet);
    }

    /**
     * Constructs a {@link de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipher} mapping from a source alphabet
     * to a target alphabet.
     *
     * @param source the source alphabet
     * @param dest   the destination (target) alphabet
     * @return
     */
    @Override
    public MonoalphabeticCipher getMonoalphabeticCipherInstance(Alphabet source, Alphabet dest) {
        return null;
    }

    /**
     * Constructs a Caesar cipher over the given alphabet and with a shift
     * specified by key.
     *
     * @param key      the shift
     * @param alphabet the alphabet
     * @return
     */
    @Override
    public Caesar getCaesarInstance(int key, Alphabet alphabet) {
        return null;
    }

    /**
     * Constructs a {@link de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.KeywordMonoalphabeticCipher} over the given alphabet
     * and with the given keyword
     *
     * @param key      the keyword
     * @param alphabet the alphabet
     * @return
     */
    @Override
    public KeywordMonoalphabeticCipher getKeywordMonoalphabeticCipherInstance(String key, Alphabet alphabet) {
        return null;
    }

    /**
     * Constructs a generic {@link de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic.PolyalphabeticCipher} from one source
     * alphabet and at least a single target alphabet.
     *
     * @param source the source alphabet
     * @param dest   an arary (vararg) of target alphabets
     * @return
     */
    @Override
    public PolyalphabeticCipher getPolyalphabeticCipherInstance(Alphabet source, Alphabet... dest) {
        return null;
    }

    /**
     * Constructs a {@link de.tudarmstadt.gdi1.project.cipher.substitution.polyalphabetic.Vigenere} Ciphere for a given key and alphabet
     *
     * @param key      the key
     * @param alphabet the alphabet
     * @return
     */
    @Override
    public Vigenere getVigenereCipherInstance(String key, Alphabet alphabet) {
        return null;
    }

    /**
     * Returns an isntance of a {@link de.tudarmstadt.gdi1.project.analysis.caeser.CaesarCryptanalysis}.
     *
     * @return
     */
    @Override
    public CaesarCryptanalysis getCaesarCryptanalysisInstance() {
        return null;
    }

    /**
     * Returns an instance of {@link de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticCpaNpaCryptanalysis}.
     *
     * @return
     */
    @Override
    public MonoalphabeticCpaNpaCryptanalysis getMonoalphabeticCpaNpaCryptanalysis() {
        return null;
    }

    /**
     * Returns an instance of {@link de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticCribCryptanalysis}.
     *
     * @return
     */
    @Override
    public MonoalphabeticCribCryptanalysis getMonoalphabeticCribCryptanalysisInstance() {
        return null;
    }

    /**
     * returns an instance of {@link de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticKnownCiphertextCryptanalysis}
     * .
     *
     * @return
     */
    @Override
    public MonoalphabeticKnownCiphertextCryptanalysis getMonoalphabeticKnownCiphertextCryptanalysisInstance() {
        return null;
    }

    /**
     * returns an instance of {@link de.tudarmstadt.gdi1.project.analysis.vigenere.VigenereCryptanalysis}
     *
     * @return
     */
    @Override
    public VigenereCryptanalysis getVigenereCryptanalysisInstance() {
        return null;
    }

    /**
     * returns an instance of {@link de.tudarmstadt.gdi1.project.utils.Utils}.
     *
     * @return
     */
    @Override
    public Utils getUtilsInstance() {
        return null;
    }

    /**
     * Constructs an {@link de.tudarmstadt.gdi1.project.cipher.enigma.Enigma} with the given rotors, pinboard and a
     * {@link de.tudarmstadt.gdi1.project.cipher.enigma.ReverseRotor}.
     *
     * @param rotors       The (ordered) list of rotors
     * @param pinboard     the pinboard
     * @param reverseRotor the reverse rotor
     * @return
     */
    @Override
    public Enigma getEnigmaInstance(List<Rotor> rotors, PinBoard pinboard, ReverseRotor reverseRotor) {
        return null;
    }

    /**
     * Constructs a {@link de.tudarmstadt.gdi1.project.cipher.enigma.PinBoard} from a source alphabet and a destiniation
     * alphabet
     *
     * @param source      the input alphabet
     * @param destination the mapping of the output
     * @return
     */
    @Override
    public PinBoard getPinBoardInstance(Alphabet source, Alphabet destination) {
        return null;
    }

    /**
     * Constructs a rotor from an two alphabets (ingoing and exit) and a
     * position.
     *
     * @param entryAlph
     * @param exitAlph
     * @param startPosition
     * @return
     */
    @Override
    public Rotor getRotorInstance(Alphabet entryAlph, Alphabet exitAlph, int startPosition) {
        return null;
    }

    /**
     * Constructs a reverse rotor from two alphabets (ingoing and exit).
     *
     * @param entryAlph
     * @param exitAlph
     * @return
     */
    @Override
    public ReverseRotor getReverseRotatorInstance(Alphabet entryAlph, Alphabet exitAlph) {
        return null;
    }

    /**
     * @return The class implementing {@link de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipher}
     */
    @Override
    public Class<? extends SubstitutionCipher> getAbstractSubstitutionCipherClass() {
        return SubstitutionCipherImpl.class;
    }

    /**
     * Constructs a {@link de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle} from a distribution and a
     * dictionary
     *
     * @param distribution
     * @param dictionary
     * @return
     */
    @Override
    public ValidateDecryptionOracle getValidateDecryptionOracle(Distribution distribution, Dictionary dictionary) {
        return null;
    }

    /**
     * Constructs an {@link de.tudarmstadt.gdi1.project.analysis.monoalphabetic.Individual}
     *
     * @param alphabet
     * @param fitness
     * @return
     */
    @Override
    public Individual getIndividualInstance(Alphabet alphabet, double fitness) {
        return null;
    }
}

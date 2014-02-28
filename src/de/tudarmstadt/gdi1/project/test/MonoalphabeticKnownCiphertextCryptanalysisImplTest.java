package de.tudarmstadt.gdi1.project.test;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;
import de.tudarmstadt.gdi1.project.alphabet.Dictionary;
import de.tudarmstadt.gdi1.project.alphabet.Distribution;
import de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.GeneticAnalysis;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.Individual;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticKnownCiphertextCryptanalysis;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.MonoalphabeticCipher;
import de.tudarmstadt.gdi1.project.utils.UtilsImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MonoalphabeticKnownCiphertextCryptanalysisImplTest {

	@BeforeClass
	public static void initTestCore() {
		TemplateTestCore.FACTORYPATH = "de.tudarmstadt.gdi1.project.FactoryImpl";
	}

	@Test
	public void implementsGenetic() {
		MonoalphabeticKnownCiphertextCryptanalysis ca = TemplateTestCore.getFactory()
				.getMonoalphabeticKnownCiphertextCryptanalysisInstance();
		Assert.assertTrue(ca instanceof GeneticAnalysis);
	}

	@Test
	public void stateImplemented() {
		Alphabet source = TemplateTestUtils.getDefaultAlphabet();
		Alphabet target = TemplateTestUtils.getMixedDefaultAlphabet();

		MonoalphabeticKnownCiphertextCryptanalysis ca = TemplateTestCore.getFactory()
				.getMonoalphabeticKnownCiphertextCryptanalysisInstance();
		Assert.assertFalse("".equals(ca.getState(source, target)));
		Assert.assertFalse(null == ca.getState(source, target));
	}

	@Test
	public void testInitialGeneration() {
		Alphabet source = TemplateTestUtils.getDefaultAlphabet();
		Alphabet target = TemplateTestUtils.getMixedDefaultAlphabet();

		/* generate distribution */
		Distribution distribution = TemplateTestCore.getFactory().getDistributionInstance(source, TemplateTestUtils.ALICE, 3);

		/* get ciphertext */
		String plaintext = source.normalize(TemplateTestUtils.ALICE_PLAIN);
		MonoalphabeticCipher cipher = TemplateTestCore.getFactory().getMonoalphabeticCipherInstance(source, target);
		String ciphertext = cipher.encrypt(plaintext);

		MonoalphabeticKnownCiphertextCryptanalysis ca = TemplateTestCore.getFactory()
				.getMonoalphabeticKnownCiphertextCryptanalysisInstance();

		GeneticAnalysis ga = (GeneticAnalysis) ca;

		List<Individual> generation = ga.prepareInitialGeneration(ciphertext, source, distribution, 15);
		Assert.assertEquals(15, generation.size());
		Assert.assertTrue(generation.get(5) instanceof Individual);
	}

	@Test
	public void testSurvivors() {
		Alphabet source = TemplateTestUtils.getDefaultAlphabet();
		Alphabet target = TemplateTestUtils.getMixedDefaultAlphabet();

		/* generate distribution */
		Distribution distribution = TemplateTestCore.getFactory().getDistributionInstance(source, TemplateTestUtils.ALICE, 3);
		Dictionary dictionary = TemplateTestCore.getFactory().getDictionaryInstance(source, TemplateTestUtils.ALICE);

		/* get ciphertext */
		String plaintext = source.normalize(TemplateTestUtils.ALICE_PLAIN);
		MonoalphabeticCipher cipher = TemplateTestCore.getFactory().getMonoalphabeticCipherInstance(source, target);
		String ciphertext = cipher.encrypt(plaintext);

		MonoalphabeticKnownCiphertextCryptanalysis ca = TemplateTestCore.getFactory()
				.getMonoalphabeticKnownCiphertextCryptanalysisInstance();

		GeneticAnalysis ga = (GeneticAnalysis) ca;

		List<Individual> generation = ga.prepareInitialGeneration(ciphertext, source, distribution, 15);
		List<Individual> survivors = ga.computeSurvivors(ciphertext, source, generation, distribution, dictionary, 3);
		Assert.assertEquals(3, survivors.size());
	}

	@Test
	public void testFitness() {
		Alphabet source = TemplateTestUtils.getDefaultAlphabet();
		Alphabet target = new UtilsImpl().randomizeAlphabet(source);

		/* generate distribution */
		Distribution distribution = TemplateTestCore.getFactory().getDistributionInstance(source, TemplateTestUtils.ALICE, 3);
		Dictionary dictionary = TemplateTestCore.getFactory().getDictionaryInstance(source, TemplateTestUtils.ALICE);

		/* get ciphertext */
		String plaintext = source.normalize(TemplateTestUtils.ALICE_PLAIN);
		MonoalphabeticCipher cipher = TemplateTestCore.getFactory().getMonoalphabeticCipherInstance(source, target);
		String ciphertext = cipher.encrypt(plaintext);

		MonoalphabeticKnownCiphertextCryptanalysis ca = TemplateTestCore.getFactory()
				.getMonoalphabeticKnownCiphertextCryptanalysisInstance();

		GeneticAnalysis ga = (GeneticAnalysis) ca;

		List<Individual> generation = ga.prepareInitialGeneration(ciphertext, source, distribution, 15);
		double fitness = ga.computeFitness(generation.get(5), ciphertext, source, distribution, dictionary);

		Assert.assertTrue(fitness >= 0);
	}

	@Test
	public void testFullAnalysis() throws IOException, InterruptedException, ExecutionException {
		Alphabet source = TemplateTestUtils.getDefaultAlphabet();
		Alphabet target = new UtilsImpl().randomizeAlphabet(source);

		/* generate distribution */
		final Distribution distribution = TemplateTestCore.getFactory().getDistributionInstance(source, TemplateTestUtils.ALICE, 3);
		final Dictionary dictionary = TemplateTestCore.getFactory().getDictionaryInstance(source, TemplateTestUtils.ALICE);

		/* get ciphertext */
		final String plaintext = source.normalize(TemplateTestUtils.ALICE_PLAIN);
		MonoalphabeticCipher cipher = TemplateTestCore.getFactory().getMonoalphabeticCipherInstance(source, target);
		final String ciphertext = cipher.encrypt(plaintext);

		final MonoalphabeticKnownCiphertextCryptanalysis ca = TemplateTestCore.getFactory()
				.getMonoalphabeticKnownCiphertextCryptanalysisInstance();

		/* run break in thread */
		Callable<char[]> task = new Callable<char[]>() {

			@Override
			public char[] call() throws Exception {
				char[] reconstructedKey = ca.knownCiphertextAttack(ciphertext, distribution, dictionary
						, new ValidateDecryptionOracle() {
					@Override
					public boolean isCorrect(String p) {
						return plaintext.equals(p);
					}
				}
				);
				return reconstructedKey;
			}
		};
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<char[]> future = service.submit(task);

		long t = System.currentTimeMillis();
		while(!future.isDone()) {
			Thread.sleep(5000);
			System.out.println(ca.getState(distribution.getAlphabet(), target));
		}

		char[] reconstructedKey = future.get();

		System.out.println("-- reconstruction finished in " + (System.currentTimeMillis() - t) + "ms");
		System.out.println(reconstructedKey);
		cipher = TemplateTestCore.getFactory().getMonoalphabeticCipherInstance(source, TemplateTestUtils.getAlphabetFrom(reconstructedKey));
		String plaintextPrime = cipher.decrypt(ciphertext);

		if(!plaintextPrime.equals(plaintext)) {
			Assert.assertEquals(0, TemplateTestUtils.countDifferences(target.asCharArray(), reconstructedKey));
		}
	}

	@Test
	public void testEveryLength() throws InterruptedException, ExecutionException {
		return;
		/*
		for(int i = 10; i <= 26; i++) {
			Thread.sleep(1000);
			System.out.println();
			System.out.println(" ****************** TESTING LENGTH ********************");
			System.out.println(" ******************       " + i + "       ********************");
			System.out.println();
			testLength(i);

		}
		*/
	}


	public void testLength(int length) throws InterruptedException, ExecutionException {/* this can run for a while */

		Alphabet sourcetmp = new UtilsImpl().randomizeAlphabet(TemplateTestUtils.getDefaultAlphabet());
		ArrayList<Character> tmp = new ArrayList<Character>();

		int i = 0;
		for(char c : sourcetmp.asCharArray()) {
			if(i >= length) {
				break;
			}
			tmp.add(c);
			i++;
		}

		Alphabet source = new AlphabetImpl(tmp);


		//final Alphabet key = new UtilsImpl().randomizeAlphabet(TemplateTestUtils.getDefaultAlphabet());
		final Alphabet key = new UtilsImpl().randomizeAlphabet(source);

		final Dictionary dictionary = TemplateTestCore.getFactory().getDictionaryInstance(source, TemplateTestUtils.ALICE);
		final Distribution distribution = TemplateTestCore.getFactory().getDistributionInstance(source, TemplateTestUtils.ALICE, 3);


		final MonoalphabeticKnownCiphertextCryptanalysis ca = TemplateTestCore.getFactory().getMonoalphabeticKnownCiphertextCryptanalysisInstance();

		MonoalphabeticCipher cipher = TemplateTestCore.getFactory().getMonoalphabeticCipherInstance(source, key);
		final String plaintext = source.normalize(TemplateTestUtils.ALICE_PLAIN);
		final String ciphertext = cipher.encrypt(plaintext);

		final List<String> cribs = new ArrayList<String>();
		cribs.add(source.normalize("alice"));
		cribs.add(source.normalize("reizendsten"));
		cribs.add(source.normalize("maeuseloch"));
		cribs.add(source.normalize("zusammenschieben"));

	/* run break in thread */
		Callable<char[]> task = new Callable<char[]>() {

			@Override
			public char[] call() throws Exception {
				char[] reconstructedKey = ca.knownCiphertextAttack(ciphertext, distribution, dictionary
						, new ValidateDecryptionOracle() {
					@Override
					public boolean isCorrect(String p) {
						return plaintext.equals(p);
					}
				}
				);
				return reconstructedKey;
			}
		};
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<char[]> future = service.submit(task);

		long t = System.currentTimeMillis();
		long time2 = System.currentTimeMillis();

		while(!future.isDone()) {
			//Thread.sleep(10);
			if(System.currentTimeMillis() - time2 >= 5000) {
				time2 = System.currentTimeMillis();
				System.out.println(ca.getState(distribution.getAlphabet(), key));
			}
		}

		System.out.println(ca.getState(distribution.getAlphabet(), key));
		char[] reconstructedKey = future.get();

		System.out.println("-- reconstruction finished in " + (System.currentTimeMillis() - t) + "ms");
		System.out.println(reconstructedKey);
		Thread.sleep(10);

		cipher = TemplateTestCore.getFactory().getMonoalphabeticCipherInstance(source, TemplateTestUtils.getAlphabetFrom(reconstructedKey));
		String plaintextPrime = cipher.decrypt(ciphertext);

		Assert.assertEquals("plaintext doesn't match", plaintext, plaintextPrime);
		if(!plaintextPrime.equals(plaintext)) {
			Assert.assertEquals(0, TemplateTestUtils.countDifferences(key.asCharArray(), reconstructedKey));
		}
	}
}

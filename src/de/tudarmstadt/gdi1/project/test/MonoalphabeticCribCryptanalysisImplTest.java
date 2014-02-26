package de.tudarmstadt.gdi1.project.test;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.alphabet.AlphabetImpl;
import de.tudarmstadt.gdi1.project.alphabet.DictionaryImpl;
import de.tudarmstadt.gdi1.project.alphabet.DistributionImpl;
import de.tudarmstadt.gdi1.project.analysis.ValidateDecryptionOracle;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticCribCryptanalysisImpl;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.KeywordMonoalphabeticCipher;
import de.tudarmstadt.gdi1.project.cipher.substitution.monoalphabetic.KeywordMonoalphabeticCipherImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Hannes on 2/25/14.
 *
 * @author Hannes Güdelhöfer
 */
public class MonoalphabeticCribCryptanalysisImplTest {

    MonoalphabeticCribCryptanalysisImpl mono;
    Alphabet alph;
    ValidateDecryptionOracle oracle;
    String plaintext;
    String plaintextWithSpaced;

    List<String> crips;


    @Before
    public void setUp() throws Exception {

        crips = new ArrayList<String>();

        plaintextWithSpaced = "abefghi jibdacg hei defig adbe jdabc";
        crips.add(plaintextWithSpaced.split(" ")[1]);
        crips.add(plaintextWithSpaced.split(" ")[2]);
        crips.add(plaintextWithSpaced.split(" ")[4]);

        plaintext = plaintextWithSpaced.replace(" ","").toLowerCase();

        mono = new MonoalphabeticCribCryptanalysisImpl();
        alph = new AlphabetImpl("abcdefghij");
        oracle = new ValidateDecryptionOracle() {
            @Override
            public boolean isCorrect(String aplaintext) {
                return aplaintext.equals(plaintext);
            }
        };
    }

    @Test
    public void testKnownCiphertextAttack() throws Exception {
        String key = "abgefa".replace(" ","");
        final KeywordMonoalphabeticCipher cipher = new KeywordMonoalphabeticCipherImpl(key, alph);

        char[] realKey = new AlphabetImpl("abgefjihdc").asCharArray();
       /* run break in thread */
	    Callable<char[]> task = new Callable<char[]>() {

		    @Override
		    public char[] call() throws Exception {
			    char[] reconstructedKey = mono.knownCiphertextAttack(cipher.encrypt(plaintext), new DistributionImpl(alph, plaintext), new DictionaryImpl(plaintextWithSpaced, alph), crips, oracle);

			    return reconstructedKey;
		    }
	    };
	    ExecutorService service = Executors.newSingleThreadExecutor();
	    Future<char[]> future = service.submit(task);

	    long t = System.currentTimeMillis();
	    while (!future.isDone()) {
		    Thread.sleep(5000);
		    System.out.println(mono.getState(alph, new AlphabetImpl(new String(realKey))));
	    }
	    Assert.assertArrayEquals("couldn't get the key 'dbca'", realKey, future.get());

    }

    @Test
    public void testKnownCiphertextAttack1() throws Exception {

    }
}

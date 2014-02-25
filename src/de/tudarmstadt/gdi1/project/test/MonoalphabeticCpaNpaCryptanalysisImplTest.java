package de.tudarmstadt.gdi1.project.test;
import de.tudarmstadt.gdi1.project.analysis.EncryptionOracle;
import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticCpaNpaCryptanalysis;
import de.tudarmstadt.gdi1.project.analysis.monoalphabetic.MonoalphabeticCpaNpaCryptanalysisImpl;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Noxicon on 24.02.14.
 */
public class MonoalphabeticCpaNpaCryptanalysisImplTest extends TestCase {
    Alphabet alph;
    String cipher;
    String plain;
    String key;
    String testKeyChosenPlaintext;
    MonoalphabeticCpaNpaCryptanalysisImpl mono = new MonoalphabeticCpaNpaCryptanalysisImpl();

    @Before
    public void setUp() {

       alph = TemplateTestUtils.getDefaultAlphabet();
       plain = "sylviawagtquickdenjuxbeipforzheim";
       cipher = "uanxkcycivswkemfgplwzdgkrhqtbjgko";
       key = "cdefghijklmnopqrstuvwxyzab";
    }
    @Test
    public void testKnownPlaintextAttack() throws Exception {
        Assert.assertArrayEquals("Monoalphabetic Cipher test failed",key.toCharArray(),mono.knownPlaintextAttack(cipher, plain, alph)  );
    }

    @Test
    public void testKnownPlaintextAttack1() throws Exception {

    }

    @Test
    public void testKnownPlaintextAttack2() throws Exception {

    }

    @Test
    public void testChosenPlaintextAttack() throws Exception {
    }
}

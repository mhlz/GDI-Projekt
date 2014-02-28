package de.tudarmstadt.gdi1.project.test;

import de.tudarmstadt.gdi1.project.alphabet.Alphabet;
import de.tudarmstadt.gdi1.project.analysis.caeser.CaesarCryptanalysisImpl;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class CaesarCryptanalysisImplTest extends TestCase {
	Alphabet alph;
	String cipher;
	String plain;
	CaesarCryptanalysisImpl caesar = new CaesarCryptanalysisImpl();

	@Before
	public void setUp() {
		alph = TemplateTestUtils.getDefaultAlphabet();
		cipher = "doobrxuedvhduhehorqjwrxv"; //"All you base are belong to us" shifted by 3
		plain = "base";
	}

	@Test
	public void testKnownPlaintextAttack() throws Exception {

		Assert.assertEquals("Caesar known ciphertext attack failed", new Integer(3), caesar.knownPlaintextAttack(cipher, plain, alph));
	}
}

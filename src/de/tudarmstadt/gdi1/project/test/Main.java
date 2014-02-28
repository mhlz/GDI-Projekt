package de.tudarmstadt.gdi1.project.test;

import de.tudarmstadt.gdi1.project.test.alphabet.TemplateAlphabetTests;
import de.tudarmstadt.gdi1.project.test.alphabet.TemplateDictionaryTests;
import de.tudarmstadt.gdi1.project.test.alphabet.TemplateDistributionTests;
import de.tudarmstadt.gdi1.project.test.analysis.TemplateCaesarCryptanalysisTests;
import de.tudarmstadt.gdi1.project.test.analysis.TemplateValidationDecryptionOracle;
import de.tudarmstadt.gdi1.project.test.analysis.TemplateVigenereCryptanalysisTests;
import de.tudarmstadt.gdi1.project.test.analysis.monoalphabetic.TemplateIndividualTests;
import de.tudarmstadt.gdi1.project.test.analysis.monoalphabetic.TemplateMonoalphabeticCpaNpaCryptanalysisTests;
import de.tudarmstadt.gdi1.project.test.cipher.enigma.TemplateEnigmaTest;
import de.tudarmstadt.gdi1.project.test.cipher.enigma.TemplatePinBoardTest;
import de.tudarmstadt.gdi1.project.test.cipher.enigma.TemplateReverseRotorTest;
import de.tudarmstadt.gdi1.project.test.cipher.enigma.TemplateRotorTest;
import de.tudarmstadt.gdi1.project.test.cipher.substitution.*;
import de.tudarmstadt.gdi1.project.test.utils.TemplateUtilsTests;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//import de.tudarmstadt.gdi1.project.test.alphabet.TemplateObjectWithFrequencyTests;
//import de.tudarmstadt.gdi1.project.test.analysis.TemplateEncryptionOracleTests;

@RunWith(Suite.class)
@SuiteClasses({
	/* alphabet and co */
		TemplateAlphabetTests.class,
		TemplateDictionaryTests.class,
		TemplateDistributionTests.class,
	
	/* ciphers */
		TemplateCaesarTests.class,
		TemplateKeywordMonoalphabeticCipherTests.class,
		TemplateMonoalphabeticCipherTests.class,
		TemplatePolyalphabeticCipherTests.class,
		TemplateSubstitutionCipherTests.class,
		TemplateVigenereCipherTests.class,
	
	/* analysis */
		TemplateCaesarCryptanalysisTests.class,
		TemplateValidationDecryptionOracle.class,
		TemplateVigenereCryptanalysisTests.class,
		TemplateIndividualTests.class,
		TemplateMonoalphabeticCpaNpaCryptanalysisTests.class,
	
	/* enigma */
		TemplateEnigmaTest.class,
		TemplatePinBoardTest.class,
		TemplateReverseRotorTest.class,
		TemplateRotorTest.class,
	
	/* utils */
		TemplateUtilsTests.class,

    /*our tests*/
		CaesarCryptanalysisImplTest.class,
		VigenereCryptanalysisImplTest.class
})
public class Main {

	@BeforeClass
	public static void initTestCore() {
		TemplateTestCore.FACTORYPATH = "de.tudarmstadt.gdi1.project.FactoryImpl";
	}

}

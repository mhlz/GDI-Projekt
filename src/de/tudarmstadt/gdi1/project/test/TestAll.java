package de.tudarmstadt.gdi1.project.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
//import de.tudarmstadt.gdi1.project.test.alphabet.TemplateObjectWithFrequencyTests;
//import de.tudarmstadt.gdi1.project.test.analysis.TemplateEncryptionOracleTests;

@RunWith(Suite.class)
@SuiteClasses({
	Main.class,
	TestMonoalphabeticCribAnalysis.class,
	TestMonoalphabeticKCAnalysis.class
	})
public class TestAll {

	@BeforeClass
	public static void initTestCore() {
		TemplateTestCore.FACTORYPATH = "de.tudarmstadt.gdi1.project.FactoryIM";
	}

}

package de.tudarmstadt.gdi1.project.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.tudarmstadt.gdi1.project.test.analysis.monoalphabetic.TemplateMonoalphabeticKnownCiphertextCryptanalysisTests;

@RunWith(Suite.class)
@SuiteClasses({
	TemplateMonoalphabeticKnownCiphertextCryptanalysisTests.class,
	})
public class TestMonoalphabeticKCAnalysis {

	@BeforeClass
	public static void initTestCore() {
		TemplateTestCore.FACTORYPATH = "de.tudarmstadt.gdi1.project.FactoryIM";
	}

}

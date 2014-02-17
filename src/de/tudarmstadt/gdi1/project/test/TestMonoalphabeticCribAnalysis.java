package de.tudarmstadt.gdi1.project.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.tudarmstadt.gdi1.project.test.analysis.monoalphabetic.TemplateCribCryptanalysisTests;

@RunWith(Suite.class)
@SuiteClasses({
	TemplateCribCryptanalysisTests.class,
	})
public class TestMonoalphabeticCribAnalysis {

	@BeforeClass
	public static void initTestCore() {
		TemplateTestCore.FACTORYPATH = "de.tudarmstadt.gdi1.project.FactoryIM";
	}

}

package de.tudarmstadt.gdi1.project.test;

import de.tudarmstadt.gdi1.project.test.analysis.monoalphabetic.TemplateCribCryptanalysisTests;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		TemplateCribCryptanalysisTests.class
})
public class TestMonoalphabeticCribAnalysis {

	@BeforeClass
	public static void initTestCore() {
		TemplateTestCore.FACTORYPATH = "de.tudarmstadt.gdi1.project.FactoryImpl";
	}

}

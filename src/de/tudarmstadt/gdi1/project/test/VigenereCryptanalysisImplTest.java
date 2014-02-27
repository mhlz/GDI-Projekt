package de.tudarmstadt.gdi1.project.test;

import de.tudarmstadt.gdi1.project.analysis.vigenere.VigenereCryptanalysisImpl;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nansu on 25.02.14.
 */
public class VigenereCryptanalysisImplTest {
	@Test
	public void testGetDividends() throws Exception {
		VigenereCryptanalysisImpl test = new VigenereCryptanalysisImpl();
		List<Integer> temp = new ArrayList<Integer>();
		temp.add(1);
		temp.add(2);
		temp.add(3);
		temp.add(6);
		Assert.assertEquals(temp, test.getDividends(6));
	}

	@Test
	public void testGgT() throws Exception {
		VigenereCryptanalysisImpl test = new VigenereCryptanalysisImpl();

		Assert.assertEquals(2, test.ggT(6, 4));
	}

	@Test
	public void testGgT1() throws Exception {
		VigenereCryptanalysisImpl test = new VigenereCryptanalysisImpl();
		List<Integer> temp = new ArrayList<Integer>();
		temp.add(6);
		temp.add(4);

		Assert.assertEquals(2, test.ggT(temp));

	}

	@Test
	public void testGetDistance() throws Exception {
		VigenereCryptanalysisImpl test = new VigenereCryptanalysisImpl();
		String temp = "okabcdok";
		List<Integer> dummy = new ArrayList<Integer>();
		dummy.add(6);

		Assert.assertEquals(dummy, test.getDistance(temp, 2));
	}

	@Test
	public void testGetKeyLength() throws Exception {
		VigenereCryptanalysisImpl test = new VigenereCryptanalysisImpl();
		String dummy = "okabcdoka";
		List<Integer> temp = new ArrayList<Integer>();
		temp.add(1);
		temp.add(2);
		temp.add(3);
		temp.add(6);
		Assert.assertEquals(temp, test.getKeyLength(dummy));
	}
}

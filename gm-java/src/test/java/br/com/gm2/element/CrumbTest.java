/**
 * Copyright (C) {2017}  {Glaucio Melo}
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package br.com.gm2.element;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.element.GMFileFormat;

/**
 * Defines basic guidelines from crumb creation from incoming content.
 * 
 * @author glauciom
 *
 */
public class CrumbTest {

	@Test
	public void createCrumbFromBytesTestAllBitsActivated()
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] input = { -1 };
		int uniqueness = 1364076727;
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(0 == crumb.d && crumb.k == -input.length * GMFileFormat.BYTE_SIZE
				&& uniqueness == crumb.uniqueness);
	}

	@Test
	public void createCrumbFromBytesTestFlipBits() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] input = { -20 };
		int uniqueness = -1135700186;
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(59 == crumb.d && crumb.k == -5 && uniqueness == crumb.uniqueness);
	}

	@Test
	public void createCrumbFromBytesTestNoFlipBits() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] input = { 20 };
		int uniqueness = 12394096;
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(25 == crumb.d && crumb.k == 2 && uniqueness == crumb.uniqueness);
	}

	@Test
	public void createCrumbFromBytesTestAllBitsActivatedHash()
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] input = { -1 };
		int uniqueness = 1364076727;
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(0 == crumb.d && crumb.k == -input.length * GMFileFormat.BYTE_SIZE
				&& uniqueness == crumb.uniqueness);
	}

	@Test
	public void createCrumbFromBytesTestFlipBitsHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] input = { -20 };
		int uniqueness = -1135700186;
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(59 == crumb.d && crumb.k == -5 && uniqueness == crumb.uniqueness);
	}

	@Test
	public void createCrumbFromBytesTestNoFlipBitsHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] input = { 20 };
		int uniqueness = 12394096;
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(25 == crumb.d && crumb.k == 2 && uniqueness == crumb.uniqueness);
	}

}

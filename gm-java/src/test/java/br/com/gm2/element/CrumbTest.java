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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import br.com.gm2.core.content.ValidationType;
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
	public void createCrumbFromBytesTestAllBitsActivated() {
		byte[] input = { -1 };
		byte[] uniqueness = { -123, -27, 50, 113, -31, 64, 6, -16, 38, 89, 33, -48, 45, 77, 115, 108, -36, 88, 11, 11 };
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(0 == crumb.d && crumb.k == input.length * GMFileFormat.BYTE_SIZE
				&& Arrays.equals(uniqueness, crumb.uniqueness));
	}

	@Test
	public void createCrumbFromBytesTestFlipBits() {
		byte[] input = { -20 };
		byte[] uniqueness = { 10, -48, 82, -35, -97, 50, 64, 85, 33, -28, 60, 110, -67, -59, 47, 90, 2, 84, -109, -78 };
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(59 == crumb.d && crumb.k == 5 && Arrays.equals(uniqueness, crumb.uniqueness));
	}

	@Test
	public void createCrumbFromBytesTestNoFlipBits() {
		byte[] input = { 20 };
		byte[] uniqueness = { 60, -32, -95, -81, -112, -74, -25, -93, -35, -115, 69, -28, 16, -120, 75, 88, -114, -94,
				-48, 76 };
		Crumb crumb = new Crumb(input);
		Assert.assertTrue(25 == crumb.d && crumb.k == 2 && Arrays.equals(uniqueness, crumb.uniqueness));
	}

	@Test
	public void createCrumbFromBytesTestAllBitsActivatedHash() {
		byte[] input = { -1 };
		byte[] uniqueness = { -88, 16, 10, -26, -86, 25, 64, -48, -74, 99, -69, 49, -51, 70, 97, 66, -21, -67, -67, 81,
				-121, 19, 27, -110, -39, 56, 24, -104, 120, 50, -21, -119 };
		Crumb crumb = new Crumb(input, ValidationType.SHA256);
		Assert.assertTrue(0 == crumb.d && crumb.k == input.length * GMFileFormat.BYTE_SIZE
				&& Arrays.equals(uniqueness, crumb.uniqueness));
	}

	@Test
	public void createCrumbFromBytesTestFlipBitsHash() {
		byte[] input = { -20 };
		byte[] uniqueness = { 69, -8, 61, 23, -31, 11, 52, -4, -96, 30, -72, -12, 69, 77, -84, 52, -89, 119, -39, 64,
				74, 70, 78, 115, 44, -12, -85, -14, -64, -38, -108, -60 };
		Crumb crumb = new Crumb(input, ValidationType.SHA256);
		Assert.assertTrue(59 == crumb.d && crumb.k == 5 && Arrays.equals(uniqueness, crumb.uniqueness));
	}

	@Test
	public void createCrumbFromBytesTestNoFlipBitsHash() {
		byte[] input = { 20 };
		byte[] uniqueness = { -125, -119, 29, 127, -24, 92, 51, -27, 44, -117, 78, 88, 20, -55, 47, -74, -93, -71, 70,
				114, -103, 32, 5, 56, -90, -70, -70, -88, -76, 82, -40, 121 };
		Crumb crumb = new Crumb(input, ValidationType.SHA256);
		Assert.assertTrue(25 == crumb.d && crumb.k == 2 && Arrays.equals(uniqueness, crumb.uniqueness));
	}

}

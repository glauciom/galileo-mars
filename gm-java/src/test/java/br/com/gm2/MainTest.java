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
package br.com.gm2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.io.GMPack;
import br.com.gm2.core.io.GMUnpack;
import br.com.gm2.core.strategy.AbstractStrategy;
import br.com.gm2.core.strategy.impl.BruteForceStrategy;
import br.com.gm2.core.strategy.impl.CBinaryRecursiveSearchStrategy;

/**
 * Defines main test cases for this program.
 * 
 * @author glauciom
 *
 */
public class MainTest {

	@Test
	public void packUnpackStrategyTest() throws IOException {
		String srcFile = "src/test/resources/test.txt";
		String packedFile = "src/test/resources/test.txt.gm2";
		processFiles(new BruteForceStrategy(), srcFile, packedFile);
		processFiles(new CBinaryRecursiveSearchStrategy(), srcFile, packedFile);
	}

	// @Test
	// public void packUnpackImageBruteForceStrategyTest() throws IOException {
	// String srcFile = "src/test/resources/lena.jpg";
	// String packedFile = "src/test/resources/lena.jpg.gm2";
	// processFiles(new CBinaryRecursiveSearchStrategy(), srcFile, packedFile);
	// processFiles(new BruteForceStrategy(), srcFile, packedFile);
	// }

	private void processFiles(AbstractStrategy strategy, String srcFile, String packedFile) throws IOException {
		GMPack pack = new GMPack();
		pack.crumbIt(srcFile);
		GMUnpack unpack = new GMUnpack();
		File src = new File(srcFile);
		long time = System.currentTimeMillis();
		Crumb.metrics = new AtomicLong(0);
		File dest = unpack.unCrumbIt(strategy, packedFile);
		System.out.println("Time Elapsed: " + (System.currentTimeMillis() - time) + " milliseconds");
		System.out.println("Number of Calls: " + Crumb.metrics.toString());
		byte[] contentSrc = Files.readAllBytes(Paths.get(src.getAbsolutePath()));
		byte[] contentDest = Files.readAllBytes(Paths.get(dest.getAbsolutePath()));
		Assert.assertTrue(Arrays.equals(contentSrc, contentDest));
		dest.delete();
	}

}

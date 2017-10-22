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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.element.GMFileFormat;
import br.com.gm2.core.io.GMPack;
import br.com.gm2.core.io.GMUnpack;
import br.com.gm2.core.strategy.AbstractStrategy;
import br.com.gm2.core.strategy.impl.CBinaryRecursiveSearchStrategy;
import br.com.gm2.core.strategy.impl.HashSearchStrategy;

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
		boolean assertTrue = processFiles(new CBinaryRecursiveSearchStrategy(), srcFile, packedFile, false);
		Assert.assertTrue(assertTrue);
	}

	@Test
	public void packUnpackCBinaryAndHashStrategyTest() throws IOException {
		String srcFile = "src/test/resources/test.txt";
		String packedFile = "src/test/resources/test.txt.gm2";
		System.out.println("CBinaryRecursiveSearchStrategy");
		boolean aTrue = processFiles(new CBinaryRecursiveSearchStrategy(), srcFile, packedFile, false);
		Assert.assertTrue(aTrue);
		System.out.println("HashSearchStrategy");
		boolean assertTrue = processFiles(new HashSearchStrategy(), srcFile, packedFile, false);
		Assert.assertTrue(assertTrue);
	}

	@Test
	public void packUnpackHashStrategyTest() throws IOException {
		String srcFile = "src/test/resources/test.txt";
		String packedFile = "src/test/resources/test.txt.gm2";
		boolean assertTrue = processFiles(new HashSearchStrategy(), srcFile, packedFile, false);
		Assert.assertTrue(assertTrue);
	}

	@Test
	public void spectralTest() throws IOException {
		String dir = "src/test/resources/";
		for (int i = -128; i < 128; i++) {
			File f = new File(dir + String.valueOf(i));
			FileOutputStream out = new FileOutputStream(f);
			byte b = (byte) i;
			System.out.println("Processing File: " + b);
			out.write(new byte[] { b });
			out.close();
			boolean result = processFiles(new HashSearchStrategy(), f.getAbsolutePath(),
					f.getAbsolutePath() + GMFileFormat.gm2, true);
			Assert.assertTrue(result);
			System.out.println(" " + result);
		}

	}

	@Test
	public void packUnpackImageStrategyTest() throws IOException {
		String srcFile = "src/test/resources/lena.jpg";
		String packedFile = "src/test/resources/lena.jpg.gm2";
		System.out.println("CBinaryRecursiveSearchStrategy");
		boolean aTrue = processFiles(new CBinaryRecursiveSearchStrategy(), srcFile, packedFile, false);
		Assert.assertTrue(aTrue);
		System.out.println("HashSearchStrategy");
		boolean assertTrue = processFiles(new HashSearchStrategy(), srcFile, packedFile, false);
		Assert.assertTrue(assertTrue);
	}

	private boolean processFiles(AbstractStrategy strategy, String srcFile, String packedFile, boolean deleteSrcFiles)
			throws IOException {
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
		dest.delete();
		if (deleteSrcFiles) {
			src.delete();
			new File(packedFile).delete();
		}
		return Arrays.equals(contentSrc, contentDest);

	}

}

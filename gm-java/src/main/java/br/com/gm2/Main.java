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

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.io.GMPack;
import br.com.gm2.core.io.GMUnpack;
import br.com.gm2.core.strategy.AbstractStrategy;
import br.com.gm2.core.strategy.impl.SimviStrategy;

/**
 * Main Application. Defines the user options and triggers the classes.
 * 
 * @author glauciom
 *
 */
public class Main {
	public static void main(String[] args) throws IOException {
		Main.packUnpackImageStrategyTest();
	}

	public static void packUnpackImageStrategyTest() throws IOException {
		String srcFile = "src/test/resources/lena.jpg";
		String packedFile = "src/test/resources/lena.jpg.gm2";
		// String srcFile = "src/test/resources/test.txt";
		// String packedFile = "src/test/resources/test.txt.gm2";
		System.out.println("OptimizedHashSearchStrategy");
		// ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 0,
		// TimeUnit.SECONDS,
		// new SynchronousQueue<Runnable>());
		boolean assertTrue = processFiles(new SimviStrategy(), srcFile, packedFile, false);
		// executor.shutdownNow();
		System.out.println(assertTrue);
	}

	private static boolean processFiles(AbstractStrategy strategy, String srcFile, String packedFile,
			boolean deleteSrcFiles) throws IOException {
		GMPack pack = new GMPack();
		pack.crumbIt(srcFile);
		GMUnpack unpack = new GMUnpack();
		File src = new File(srcFile);
		Crumb.metrics = 0;
		long time = System.nanoTime();
		File dest = unpack.unCrumbIt(strategy, packedFile);
		System.out.println("Time Elapsed: " + ((float) (System.nanoTime() - time) / 1_000_000_000) + " seconds");
		System.out.println("Number of Calls: " + Crumb.metrics);
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

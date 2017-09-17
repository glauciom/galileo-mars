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

import org.junit.Test;

import br.com.gm2.core.io.GMPack;
import br.com.gm2.core.io.GMUnpack;

/**
 * Defines main test cases for this program.
 * 
 * @author glauciom
 *
 */
public class MainTest {

	public String srcFile = "src/test/resources/test.txt";
	public String packedFile = "src/test/resources/test.txt.gm2";

	@Test
	public void packUnpackTest() {
		GMPack pack = new GMPack();
		pack.crumbIt(srcFile);
		GMUnpack unpack = new GMUnpack();
		unpack.unCrumbIt(packedFile);
	}

}

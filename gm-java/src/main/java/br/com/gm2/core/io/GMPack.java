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
package br.com.gm2.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import br.com.gm2.core.content.Errors;
import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.element.GlobalHeader;

/**
 * Defines read / write file reading. From any format to gm2
 * 
 * @author glauciom
 *
 */
public class GMPack {

	private String gm2 = ".gm2";

	public void crumbIt(String file) {
		File src = new File(file);
		File dest = new File(src.getAbsolutePath() + gm2);
		GlobalHeader header = new GlobalHeader((int) src.length());
		try (InputStream is = new FileInputStream(src); OutputStream os = new FileOutputStream(dest)) {
			byte[] readBuffer = new byte[header.metadata.packetSize.getSize()];
			os.write(header.getBytes());
			int size = -1;
			while ((size = is.read(readBuffer)) != -1) {
				ByteBuffer bb = ByteBuffer.allocate(size);
				bb.put(readBuffer, 0, size);
				byte[] writeBuffer = new Crumb(bb.array()).getBytes();
				os.write(writeBuffer, 0, writeBuffer.length);
			}
		} catch (NoSuchAlgorithmException e) {
			System.err.println(Errors.ERROR_2.toString());
		} catch (IOException e) {
			System.err.println(Errors.ERROR_3.toString());
		}
	}

}

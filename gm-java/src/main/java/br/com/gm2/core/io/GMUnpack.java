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
import java.io.InputStream;
import java.io.OutputStream;

import br.com.gm2.core.content.Errors;
import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.element.GMFileFormat;
import br.com.gm2.core.element.GlobalHeader;
import br.com.gm2.core.strategy.AbstractStrategy;

/**
 * Defines Unpack routine (reading gm2 file)
 * 
 * @author glauciom
 *
 */
public class GMUnpack {

	public File unCrumbIt(AbstractStrategy strategy, String file) {
		File src = new File(file);
		File dest = new File(src.getParent() + File.separator + extractFileType(src));
		try (InputStream is = new FileInputStream(src); OutputStream os = new FileOutputStream(dest)) {
			byte[] headerbuf = new byte[GlobalHeader.globalHeaderSize];
			is.read(headerbuf, 0, GlobalHeader.globalHeaderSize);
			GlobalHeader header = GlobalHeader.readBytes(headerbuf);
			byte[] readBuffer = new byte[Crumb.crumbSize];
			int numberOfCrumbs = header.totalSize / header.metadata.packetSize.getSize();
			int index = 0;
			while ((is.read(readBuffer)) != -1) {
				int size = 0;
				if (index == numberOfCrumbs) {
					size = header.remainingSize;
				} else {
					size = header.metadata.packetSize.getSize();
				}
				if (size > 0) {
					Crumb crumb = new Crumb(readBuffer, header, size);
					byte[] writeBuffer = strategy.execute(crumb);
					os.write(writeBuffer, 0, writeBuffer.length);
				}
				index++;
			}

		} catch (Exception e) {

			System.err.println(Errors.ERROR_4.toString() + "details: " + e.getMessage());
		}
		return dest;
	}

	public String extractFileType(File src) {
		return System.currentTimeMillis() + "-" + src.getName().replaceAll(GMFileFormat.gm2, "");

	}

}

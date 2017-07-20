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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.com.gm2.core.content.Errors;

/**
 * Simple file read / write operation
 * 
 * @author glauciom
 *
 */
public final class ManageFile {

	public static byte[] read(String file) {
		try {
			return Files.readAllBytes(Paths.get(file));
		} catch (IOException e) {
			System.err.println(Errors.ERROR_0.toString());
		}
		return null;
	}

	public static void write(String file, byte[] content) {
		try {
			Files.write(Paths.get(file), content);
		} catch (IOException ex) {
			System.err.println(Errors.ERROR_1.toString());
		}
	}

}

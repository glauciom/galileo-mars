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
package br.com.gm2.core.content;

/**
 * Defines the default packet size for a given file.
 * 
 * @author glauciom
 *
 */
public enum CrumbPacket {

	CP64B(0, 4);

	private int value;
	private int size;

	CrumbPacket(int value, int size) {
		this.value = value;
		this.size = size;
	}

	public int getValue() {
		return value;
	}

	public int getSize() {
		return size;
	}

}

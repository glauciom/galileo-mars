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
package br.com.gm2.core.element;

import br.com.gm2.core.content.CrumbPacket;

/**
 * Defines the meta data for a given gm2 file
 * 
 * @author glauciom
 *
 */
public class Metadata {
	public int numberOfExecutions;
	public CrumbPacket packetSize;

	public Metadata(int numberOfExecutions, CrumbPacket packetSize) {
		this.numberOfExecutions = numberOfExecutions;
		this.packetSize = packetSize;
	}

	public Metadata() {
		this(0, CrumbPacket.CP64B);
	}

	// TODO define meta data format based on inputs.
	public byte getByte() {
		byte[] content = new byte[] { 0x00 };
		return content[0];
	}

}

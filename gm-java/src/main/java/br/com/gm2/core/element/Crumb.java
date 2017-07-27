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

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Crumb Header (for each file portion): - 1 bit, boolean: inverse (volatile,
 * during process, if k > default packet size / 2). Default: false; 4 bytes,
 * position (volatile, during process) - 4 bytes: k (number of 1's); - 4 bytes:
 * Normal distance of the content, considering the Identity Combination as
 * reference; - 20-32bytes: Hash to identify the content uniquely (32 bytes
 * sha256) or sha1 (20bytes).
 * 
 * @author glauciom
 *
 */
public class Crumb {

	public byte k;
	public short d;
	public byte uniqueness;

	public static final int crumbSize = 4;
	private CRC crc = new CRC();

	/**
	 * Constructor for packing process
	 * 
	 * @param b
	 */
	public Crumb(byte[] b) {
		createCrumbFromBytes(b);
	}

	/**
	 * Constructor for unpacking process.
	 * 
	 * @param b
	 * @param header
	 */
	public Crumb(byte[] b, GlobalHeader header) {
		// TODO read format.
	}

	public Crumb createCrumbFromBytes(byte[] b) {
		BitSet set = BitSet.valueOf(b);
		int n = GMFileFormat.BYTE_SIZE * b.length;
		this.k = (byte) set.cardinality();
		int dim = 0;
		if (this.k > n / 2) {
			set.flip(0, n);
			dim = n - k;
		} else {
			dim = this.k;
		}
		int from = 0;
		this.d = 0;
		int ind = n - dim;
		for (int i = 0; i < dim; i++) {
			int current = set.nextSetBit(from);
			if (current != ind) {
				int diff = current - ind;
				this.d += diff * diff;
			}

			from = current + 1;
			ind++;
		}

		this.uniqueness = crc.doIt(b);
		return this;
	}

	public byte[] getBytes() {
		ByteBuffer bb = ByteBuffer.allocate(crumbSize);
		bb.put(k);
		bb.putShort(d);
		bb.put(uniqueness);
		return bb.array();
	}
}

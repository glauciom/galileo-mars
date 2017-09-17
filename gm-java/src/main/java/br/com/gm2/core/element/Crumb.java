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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

	public transient int n;
	public int k;
	public int d;
	public byte[] uniqueness;
	public boolean inverse = false;

	public static final int crumbSize = 40;

	/**
	 * Constructor for packing process
	 * 
	 * @param b
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public Crumb(byte[] b) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		createCrumbFromBytes(b);
	}

	/**
	 * Constructor for unpacking process.
	 * 
	 * @param b
	 * @param header
	 */
	public Crumb(byte[] b, GlobalHeader header, int n) {
		this.n = GMFileFormat.BYTE_SIZE * n;
		setBytes(b, header);
	}

	public Crumb createCrumbFromBytes(byte[] b) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BitSet set = BitSet.valueOf(b);
		int n = GMFileFormat.BYTE_SIZE * b.length;
		this.k = (byte) set.cardinality();
		int dim = 0;
		boolean inverse = false;
		if (this.k > n / 2) {
			set.flip(0, n);
			dim = n - k;
			inverse = true;
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

		this.uniqueness = SHA(b);
		if (inverse) {
			this.k = -this.k;
		}
		return this;
	}

	public byte[] SHA(byte[] b) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-256");
		md.update(b);
		return md.digest();
	}

	public byte[] getBytes() {
		ByteBuffer bb = ByteBuffer.allocate(crumbSize);
		bb.putInt(k);
		bb.putInt(d);
		bb.put(uniqueness);
		return bb.array();
	}

	public void setBytes(byte[] crumbByte, GlobalHeader header) {
		ByteBuffer bb = ByteBuffer.wrap(crumbByte);
		this.k = bb.getInt();
		if (this.k < 0) {
			inverse = true;
			this.k = -this.k;
		}
		this.d = bb.getInt();
		ByteBuffer shaBuffer = ByteBuffer.allocate(bb.remaining());
		shaBuffer.put(crumbByte, bb.position(), bb.remaining());
		this.uniqueness = shaBuffer.array();

	}
}

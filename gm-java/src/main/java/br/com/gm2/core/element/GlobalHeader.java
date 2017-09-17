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

/**
 * Global Header: - 1 byte for meta data: default: 00000000 (1 run, 64B, SHA1,
 * zip) -- Number of running: 3 bits (up to 8 runs). -- default packet size (map
 * with 64B, 128B, 512B, 1KB) - 2 bits -- validation type (SHA1, SHA256) - 1 bit
 * -- content-type (map with zip, mp3, mkv, mp4) - 2 bits - 4 bytes:
 * Crumb-total-length (byte array size) - 4 bytes, Volatile: Remaining-length
 * size: (last packet size, remaining byte array size). Total: 5 bytes.
 * 
 * @author glauciom
 *
 */
public class GlobalHeader {

	public Metadata metadata;
	public int totalSize;
	public transient int remainingSize;

	public static final int globalHeaderSize = 5;

	public GlobalHeader(int totalSize) {
		this.metadata = new Metadata();
		this.totalSize = totalSize;
		this.remainingSize = totalSize % metadata.packetSize.getSize();
	}

	public byte[] getBytes() {
		ByteBuffer bb = ByteBuffer.allocate(globalHeaderSize);
		bb.put(metadata.getByte());
		bb.putInt(totalSize);
		return bb.array();
	}

	public static GlobalHeader readBytes(byte[] b) {
		ByteBuffer bb = ByteBuffer.wrap(b);
		bb.get(); // ignoring first byte read.
		return new GlobalHeader(bb.getInt());

	}
}

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
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

import com.google.common.hash.Hashing;

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
    public byte k;
    public int d;
    public int uniqueness;
    public boolean inverse = false;
    public static int HASH_SIZE = 4;
    public static int HEADER_SIZE = 5;
    public static final int crumbSize = HASH_SIZE + HEADER_SIZE;

    public static long metrics = 0;

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

        if (inverse) {
            this.k = (byte) -this.k;
        }
        // System.out.println("Input: " + set + "\t" + d + "\n");
        this.uniqueness = hash(toGMByteArray(set, b.length));
        return this;
    }

    public int hash(byte[] b) {
        return Hashing.murmur3_32().hashBytes(b).asInt();
    }

    public byte[] getBytes() {
        ByteBuffer bb = ByteBuffer.allocate(crumbSize);
        bb.put(k);
        bb.putInt(d);
        bb.putInt(uniqueness);
        return bb.array();
    }

    public void setBytes(byte[] crumbByte, GlobalHeader header) {
        ByteBuffer bb = ByteBuffer.wrap(crumbByte);
        this.k = bb.get();
        if (this.k < 0) {
            inverse = true;
            this.k = (byte) (n + this.k);
        }
        this.d = bb.getInt();
        this.uniqueness = bb.getInt();
    }

    public byte[] toGMByteArray(BitSet bits, int capacity) {
        byte[] bytes = new byte[capacity];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[i / GMFileFormat.BYTE_SIZE] |= 1 << (i % GMFileFormat.BYTE_SIZE);
            }
        }
        return bytes;
    }

    public byte[] toGMByteArray(int[] bits, int capacity) {
        byte[] bytes = new byte[capacity];
        for (int i = 0; i < bits.length; i++) {
            bytes[bits[i] / GMFileFormat.BYTE_SIZE] |= 1 << (bits[i] % GMFileFormat.BYTE_SIZE);
        }
        return bytes;
    }

    public byte[] toGMByteArrayFlip(byte[] bytes, int capacity) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ~bytes[i];
        }
        return bytes;
    }

    public byte[] processSubset(int[] subset, int[] identity) {
        byte[] content = toGMByteArray(subset, n / GMFileFormat.BYTE_SIZE);
        if (uniqueness == hash(content)) {
            if (inverse) {
                content = toGMByteArrayFlip(content, n / GMFileFormat.BYTE_SIZE);
            }
            return content;
        }
        return null;
    }

    public int dc(int[] subset, int[] identity) {
        metrics++;
        int result = 0;
        for (int j = 0; j < identity.length; j++) {
            int diff = identity[j] - subset[j];
            result += diff * diff;
        }
        return result;
    }

    public int dc(int[] subset, int[] identity, int i, int dp) {
        metrics++;
        int result = dp;
        for (int j = i; j < identity.length; j++) {
            int diff = identity[j] - subset[j];
            result += diff * diff;
        }
        return result;
    }
}

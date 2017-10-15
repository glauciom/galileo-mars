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
import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicLong;

import br.com.gm2.core.content.CrumbPacket;

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
    public byte[] uniqueness;
    public boolean inverse = false;
    public static int SHA_SIZE = 32;
    public static int HEADER_SIZE = 5;
    public static int truncateBytes = SHA_SIZE - CrumbPacket.CP64B.getSize();
    public static final int crumbSize = SHA_SIZE + HEADER_SIZE - truncateBytes;
    
    public static AtomicLong metrics = new AtomicLong();

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
            this.k = (byte) - this.k;
        }
        this.uniqueness = SHA(toGMByteArray(set, b.length));
        return this;
    }

    public byte[] SHA(byte[] b) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        md.update(b);
        byte res[] = md.digest();
        byte[] out = new byte[res.length - truncateBytes];
        for (int i = 0; i < out.length; i++) {
            out[i] = res[i];
        }
        return out;
    }

    public byte[] getBytes() {
        ByteBuffer bb = ByteBuffer.allocate(crumbSize);
        bb.put(k);
        bb.putInt(d);
        bb.put(uniqueness);
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
        ByteBuffer shaBuffer = ByteBuffer.allocate(bb.remaining());
        shaBuffer.put(crumbByte, bb.position(), bb.remaining());
        this.uniqueness = shaBuffer.array();
    }

    public byte[] toGMByteArray(BitSet bits, int capacity) {
        byte[] bytes = new byte[capacity];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / GMFileFormat.BYTE_SIZE - 1] |= 1 << (i % GMFileFormat.BYTE_SIZE);
            }
        }
        byte[] result = new byte[capacity];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[bytes.length - 1 - i];
        }
        return result;
    }

    public byte[] processSubset(int[] subset, int[] identity) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (dc(subset, identity) == d) {
            BitSet set = new BitSet(subset.length);
            for (int i = 0; i < subset.length; i++) {
                set.set(subset[i]);
            }
            byte[] content = toGMByteArray(set, n / GMFileFormat.BYTE_SIZE);
            if (Arrays.equals(uniqueness, SHA(content))) {
                if (inverse) {
                    set.flip(0, n);
                    content = toGMByteArray(set, n / GMFileFormat.BYTE_SIZE);
                }
                return content;
            }
        }
        return null;
    }

    private int dc(int[] subset, int[] identity) {
    	metrics.addAndGet(1);
        int result = 0;
        for (int i = 0; i < identity.length; i++) {
            int diff = identity[i] - subset[i];
            result += diff * diff;
        }
        return result;
    }
}

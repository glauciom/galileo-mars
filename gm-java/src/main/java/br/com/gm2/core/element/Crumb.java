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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

import br.com.gm2.core.content.Errors;
import br.com.gm2.core.content.ValidationType;

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

    public int k;
    public int d;
    public byte[] uniqueness;

    private int crumbSize = 28;

    public Crumb(byte[] b, ValidationType type) {
        createCrumbFromBytes(b, type);
    }

    public Crumb(byte[] b) {
        createCrumbFromBytes(b, ValidationType.SHA1);
    }

    public Crumb createCrumbFromBytes(byte[] b, ValidationType type) {
        BitSet set = BitSet.valueOf(b);
        int n = GMFileFormat.BYTE_SIZE * b.length;
        this.k = set.cardinality();
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

        try {
            MessageDigest md = MessageDigest.getInstance(type.getName());
            md.update(b, 0, b.length);
            this.uniqueness = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.err.println(Errors.ERROR_2.toString());
            return null;
        }

        return this;
    }

    public byte[] getBytes() {
        ByteBuffer bb = ByteBuffer.allocate(crumbSize);
        bb.putInt(k);
        bb.putInt(d);
        bb.put(uniqueness);
        return bb.array();

    }
}

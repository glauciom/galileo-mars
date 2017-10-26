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
package br.com.gm2.core.strategy.core;

import com.google.common.hash.Hashing;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.element.GMFileFormat;

/**
 * Define Measure and Verify static operators.
 * 
 * @author glauciom
 *
 */
public final class MVOperator {

    public static long metrics = 0;

    public static byte[] processSubset(final Crumb crumb, final int[] subset, final int[] identity) {
        int capacity = crumb.n / GMFileFormat.BYTE_SIZE;
        byte[] content = toGMByteArray(subset, capacity);
        if (crumb.uniqueness == hash(content)) {
            if (crumb.inverse) {
                content = toGMByteArrayFlip(content, capacity);
            }
            return content;
        }
        return null;
    }

    public static int hash(final byte[] b) {
        return Hashing.murmur3_32().hashBytes(b).asInt();
    }

    public static byte[] toGMByteArray(final int[] bits, final int capacity) {
        byte[] bytes = new byte[capacity];
        for (int i = 0; i < bits.length; i++) {
            bytes[bits[i] / GMFileFormat.BYTE_SIZE] |= 1 << (bits[i] % GMFileFormat.BYTE_SIZE);
        }
        return bytes;
    }

    public static byte[] toGMByteArrayFlip(final byte[] bytes, final int capacity) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ~bytes[i];
        }
        return bytes;
    }

    public static int measure(final int[] subset, final int[] identity, final int i, final int dp) {
        metrics++;
        int result = dp;
        for (int j = i; j < identity.length; j++) {
            int diff = identity[j] - subset[j];
            result += diff * diff;
        }
        return result;
    }
    
    public static String print(int[] subset, int d) {
        String result = "";
        for (int i = 0; i < subset.length; i++) {
            result += subset[i] + " ";
        }
        return result + " " + d;
    }
}

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
package br.com.gm2.core.strategy.impl.reference;

import java.math.BigInteger;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.strategy.AbstractStrategy;
import br.com.gm2.core.strategy.core.BigOperator;

/**
 * Jumps to a closer area of the solution before to apply brute force algorithm.
 * 
 * @author glauciom
 *
 */
public class HybridRankNextStrategy extends AbstractStrategy {

    private int n, k, x, y, index;
    private BigInteger serial, aproximation;
    private BruteForceStrategy bruteForceStrategy;

    @Override
    public byte[] algorithm() {

        // TODO define jump criteria.
        String number = "";

        int[] currentSubset = rankSubset(number);
        int[] previous = rankSubset(new BigInteger(number).subtract(BigInteger.ONE).toString());

        int[] indices = getIndices(currentSubset, previous);

        bruteForceStrategy = new BruteForceStrategy(subset, indices[0], indices[1]);

        return bruteForceStrategy.algorithm();
    }

    private int[] getIndices(int[] currentSubset, int[] previous) {
        int m = 0;
        int h = k;
        if (previous != null) {
            for (int i = 0; i < currentSubset.length; i++) {
                if (currentSubset[i] != previous[i]) {
                    m = previous[i];
                    h = currentSubset.length - i;
                    return new int[] { m, h };
                }
            }
        }
        return new int[] { m, h };
    }

    private int[] rankSubset(String number) {
        x = n;
        y = k - 1;
        serial = new BigInteger(number);
        if (serial.equals(BigInteger.ZERO)) {
            return null;
        }

        aproximation = BigInteger.ZERO;
        index = 0;
        for (int i = 0; i < k; i++) {
            subset[i] = element(serial);
        }
        return subset;
    }

    private int element(BigInteger serial) {
        BigInteger aux = null;
        for (int j = 1; j <= x - y + 1; j++) {
            aux = aproximation.add(BigOperator.getBinomialElements(x - j, y));
            if (aux.compareTo(serial) < 0)
                aproximation = aux;
            else {
                x = x - j;
                y = y - 1;
                index = index + j;
                return index;
            }
        }
        return index;
    }

    @Override
    public void init(Crumb crumb) {
        this.n = crumb.n;
        this.k = crumb.k;
        this.subset = new int[k];
    }

}

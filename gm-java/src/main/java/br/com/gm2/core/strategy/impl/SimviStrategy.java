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
package br.com.gm2.core.strategy.impl;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.strategy.AbstractStrategy;
import br.com.gm2.core.strategy.core.MVOperator;

/**
 * Implementation of Quick Search Strategy. Sequential Approach. Search /
 * Inspect / Measure / Verify / Infiltrate
 * 
 * @author glauciom
 *
 */
public class SimviStrategy extends AbstractStrategy {

    private int k, d;
    private Crumb crumb;

    public SimviStrategy(int[] subset) {
        this.subset = subset;
    }

    public SimviStrategy() {
    }

    @Override
    public void init(Crumb crumb) {
        this.crumb = crumb;
        int n = crumb.n;
        this.k = crumb.k;
        this.d = crumb.d;
        this.subset = new int[k];
        this.identity = new int[k];
        for (int j = 0; j < k; j++) {
            subset[j] = (n - k) + j;
            identity[j] = (n - k) + j;
        }
    }

    @Override
    public byte[] algorithm() {
        if (subset.length == 0) {
            return MVOperator.processSubset(crumb, subset, identity);
        } else {
            return search(subset, 0, 0, d, 0);
        }
    }

    private byte[] search(int[] subset, int i, int limit, int dp, int dpa) {
        byte[] result = null;
        for (subset[i] = G(i, k, dp); subset[i] >= limit; subset[i]--) {
            result = inspect(subset, i, limit, dp, dpa);
        }
        return result;
    }

    private byte[] inspect(int[] subset, int i, int limit, int dp, int dpa) {
        byte[] result = null;
        int dc = MVOperator.measure(shift(subset, i), identity, i, dpa);
        if (dc == d) {
            result = verify(subset, i, limit);
        } else if (dc > d) {
            result = infiltrate(subset, i, limit, dp, dpa);
        }
        return result;
    }

    private byte[] verify(int[] subset, int i, int limit) {
        byte[] result = MVOperator.processSubset(crumb, subset, identity);
        if (result != null) {
            subset[i] = limit;
        }
        return result;
    }

    private byte[] infiltrate(int[] subset, int i, int limit, int dp, int dpa) {
        byte[] result = null;
        if (i < k - 1) {
            int ne = (identity[i] - subset[i]) * (identity[i] - subset[i]);
            result = search(subset, i + 1, subset[i] + 1, dp - ne, dpa + ne);
            if (result != null) {
                subset[i] = limit;
            }
        } else {
            subset[i] = limit;
        }
        return result;
    }

    private int G(int i, int k, int dp) {
        return (int) Math.floor(identity[i] - Math.sqrt((k - i) * dp) / (k - i));
    }

    private int[] shift(int[] subset, int i) {
        for (int j = i; j < k; j++) {
            subset[j] = identity[j] - identity[i] + subset[i];
        }
        return subset;
    }
}

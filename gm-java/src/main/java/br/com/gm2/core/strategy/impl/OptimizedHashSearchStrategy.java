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

/**
 * Implementation of hash-based function strategy (optimized).
 * 
 * @author glauciom
 *
 */
public class OptimizedHashSearchStrategy extends AbstractStrategy {

	private int k, n, d;
	private Crumb crumb;

	public OptimizedHashSearchStrategy(int[] subset) {
		this.subset = subset;
	}

	public OptimizedHashSearchStrategy() {
	}

	@Override
	public void init(Crumb crumb) {
		this.crumb = crumb;
		this.n = crumb.n;
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
	public byte[] algorithm(Crumb crumb) {
		if (subset.length == 0) {
			return crumb.processSubset(subset, identity);
		} else {
			return hashSearch(subset, 0, 0, d, 0);
		}
	}

	private byte[] hashSearch(int[] subset, int i, int limit, int dp, int dpa) {
		return localSearch(subset, i, limit, dp, dpa, G(subset, i, dp, k));
	}

	private byte[] localSearch(int[] subset, int i, int limit, int dp, int dpa, int h) {
		byte[] result = null;
		for (subset[i] = h; subset[i] >= limit; subset[i]--) {
			subset = slide(subset, i);
			int dc = crumb.dc(subset, identity, i, dpa);
			if (dc == d) {
				result = crumb.processSubset(subset, identity);
				if (result != null) {
					break;
				}
			} else if (dc > d) {
				if (i < k - 1) {
					int diff = identity[i] == subset[i] ? 0 : identity[i] - subset[i];
					int loc = diff == 0 ? 0 : diff * diff;
					result = hashSearch(subset, i + 1, subset[i] + 1, dp - loc, dpa + loc);
					if (result != null) {
						break;
					}
				} else {
					break;
				}
			}

		}
		return result;
	}

	private int G(int[] subset, int i, int dp, int k) {
		if (identity.length == 0) {
			return 0;
		}
		int ki = k - i;
		int part = ki * dp;
		double sqr = Math.sqrt(part);
		if (ki > 1) {
			sqr = sqr / ki;
		}
		int res = (int) Math.floor(identity[i] - sqr);
		return res;
	}

	private int[] slide(int[] subset, int i) {
		int l = identity[i] - subset[i];
		for (int j = i; j < k; j++) {
			subset[j] = identity[j] - l;
		}
		return subset;
	}

	public String print(int[] subset) {
		String result = "";
		for (int i = 0; i < subset.length; i++) {
			result += subset[i] + " ";
		}
		return result;
	}
}

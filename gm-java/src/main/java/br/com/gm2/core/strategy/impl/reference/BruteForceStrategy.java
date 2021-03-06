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

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.strategy.AbstractStrategy;

/**
 * Implementation of brute force strategy.
 * 
 * @author glauciom
 *
 */
public class BruteForceStrategy extends AbstractStrategy {

	private int m, h, k, n;
	private boolean isLastElement;
	private Crumb crumb;

	public BruteForceStrategy(int[] subset, int m, int h) {
		this.subset = subset;
		this.m = m;
		this.h = h;
	}

	public BruteForceStrategy() {
	}

	@Override
	public void init(Crumb crumb) {
		this.n = crumb.n;
		this.k = crumb.k;
		this.crumb = crumb;
		this.isLastElement = false;
		this.subset = new int[k];
		this.identity = new int[k];
		this.m = 0;
		this.h = k;
		for (int j = 0; j < k; j++) {
			subset[j] = j;
			identity[j] = (n - k) + j;
		}
	}

	@Override
	public byte[] algorithm() {
		int dc = crumb.dc(subset, identity);
		byte[] result = null;
		if (dc == crumb.d) {
			result = crumb.processSubset(subset, identity);
			if (result != null) {
				return result;
			}
		}
		while (!isLastElement) {
			subset = nextKSBAlgorithm();
			dc = crumb.dc(subset, identity);
			if (dc == crumb.d) {
				result = crumb.processSubset(subset, identity);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	private int[] nextKSBAlgorithm() {
		if (isLastElement) {
			return null;
		}
		if (m < n - h - 1) {
			h = 0;
		}
		h++;
		m = subset[k - h];
		for (int j = 0; j < h; j++) {
			subset[k + j - h] = m + j + 1;
			if (subset[0] == n - k) {
				isLastElement = true;
			}
		}
		return subset;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < subset.length; i++) {
			result += subset[i] + " ";
		}
		result += "\t" + m + "\t" + h;
		return result;
	}

}

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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.strategy.AbstractStrategy;
import br.com.gm2.core.strategy.core.Found;

/**
 * Implementation of hash-based function strategy.
 * 
 * @author glauciom
 *
 */
public class HashSearchStrategy extends AbstractStrategy {

	private int k, n;

	public HashSearchStrategy(int[] subset) {
		this.subset = subset;
	}

	public HashSearchStrategy() {
	}

	@Override
	public void init(Crumb crumb) {
		this.n = crumb.n;
		this.k = crumb.k;
		this.subset = new int[k];
		this.identity = new int[k];
		for (int j = 0; j < k; j++) {
			subset[j] = (n - k) + j;
			identity[j] = (n - k) + j;
		}
	}

	@Override
	public byte[] algorithm(Crumb crumb) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] result = null;
		try {
			hashSearch(subset, 0, n - k + 1, crumb);
		} catch (Found f) {
			result = f.getValue();
		}
		return result;
	}

	private byte[] hashSearch(int[] subset, int i, int to, Crumb crumb)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, Found {
		byte[] result = null;
		int h = G(subset, identity[i], i, crumb.d, k);
		for (int j = h; j < to; j++) {
			subset = slide(subset, j, i);
			int dc = crumb.dc(subset, identity);
		//	System.out.println(print(subset) + " " + dc);
			if (dc == crumb.d) {
				result = crumb.processSubset(subset, identity);
				if (result != null) {
					throw new Found(result);
				}
			} else if (dc > crumb.d) {
				if (i != k - 1) {
					int[] in = Arrays.copyOf(subset, subset.length);
					hashSearch(in, i + 1, ulimit(in, i + 1), crumb);
				} else {
					break;
				}
			}
		}

		return result;
	}

	private int dp(int[] subset, int i) {
		int res = 0;
		for (int j = 0; j < i; j++) {
			int part = identity[j] - subset[j];
			res += part * part;
		}
		return res;
	}

	private int G(int[] subset, int y, int i, int d, int k) {
		int part = (k - i) * (d - dp(subset, i));
		double sqr = Math.sqrt(part) / (k - i);
		int res = (int) Math.floor(y - sqr);
		return y - res;
	}

	private int ulimit(int[] subset, int i) {
		if (i == 0) {
			return n - k + 1;
		} else {
			return identity[i] - subset[i] + 1;
		}
	}

	private int[] slide(int[] subset, int l, int i) {
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

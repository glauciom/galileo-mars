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

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.element.GlobalHeader;
import br.com.gm2.core.strategy.UnpackStrategy;

/**
 * Implementation of brute force strategy.
 * 
 * @author glauciom
 *
 */
public class BruteForceStrategy implements UnpackStrategy {

	private int m, h, k, n, j;
	private int[] subset;
	private boolean isLastElement;
	private int[] identity;

	@Override
	public byte[] execute(Crumb crumb, GlobalHeader header)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		this.n = crumb.n;
		if (crumb.inverse) {
			this.k = n - crumb.k;
		} else {
			this.k = crumb.k;
		}

		this.isLastElement = false;
		this.subset = new int[k];
		this.identity = new int[k];
		this.m = 0;
		this.h = k;
		for (j = 0; j < k; j++) {
			subset[j] = j;
			identity[j] = (n - k) + j;
		}

		byte[] result = crumb.processSubset(subset, identity);
		if (result != null) {
			return result;
		}
		while (!isLastElement) {
			subset = nextKSBAlgorithm();
			result = crumb.processSubset(subset, identity);
			if (result != null) {
				return result;
			}
		}

		return null;
	}

	public String printSubset(int[] subset) {
		String s = "[";
		for (int i = 0; i < subset.length - 1; i++) {
			s += subset[i] + ", ";
		}
		s += subset[subset.length - 1] + "]";
		return s;
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
		for (j = 0; j < h; j++) {
			subset[k + j - h] = m + j + 1;
			if (subset[0] == n - k) {
				isLastElement = true;
			}
		}
		return subset;
	}

}

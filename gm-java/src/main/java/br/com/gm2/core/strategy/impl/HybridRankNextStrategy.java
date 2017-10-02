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
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.strategy.AbstractStrategy;

/**
 * Jumps to a closer area of the solution before to apply brute force algorithm.
 * @author glauciom
 *
 */
public class HybridRankNextStrategy extends AbstractStrategy {

	private int n, k, x, y, index;
	private BigInteger serial, aproximation;
	private int[] subset;
	private BruteForceStrategy bruteForceStrategy;

	@Override
	public byte[] algorithm(Crumb crumb) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		// TODO define jump criteria.
		String number = "";

		x = n;
		y = k - 1;
		serial = new BigInteger(number);
		aproximation = BigInteger.ZERO;
		index = 0;
		for (int i = 0; i < k; i++) {
			subset[i] = element(serial);
		}
		bruteForceStrategy = new BruteForceStrategy();

		return bruteForceStrategy.algorithm(crumb);
	}

	public BigInteger getBinomialElements(int r, int s) {
		if ((r - s) < s) {
			return new BigInteger(factorial(r, s)).divide(new BigInteger(factorial(r - s, 1)));
		}
		return new BigInteger(factorial(r, r - s)).divide(new BigInteger(factorial(s, 1)));
	}

	private int element(BigInteger serial) {
		BigInteger aux = null;
		for (int j = 1; j <= x - y + 1; j++) {
			aux = aproximation.add(getBinomialElements(x - j, y));
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

	private String factorial(int r, int op) {
		BigInteger aux = BigInteger.ONE;
		for (int t = r; t > op; t--) {
			aux = aux.multiply(BigInteger.valueOf(t));
		}
		return aux.toString();
	}

}

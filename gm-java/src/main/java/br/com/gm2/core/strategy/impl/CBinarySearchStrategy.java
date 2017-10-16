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
import br.com.gm2.core.strategy.AbstractStrategy;

/**
 * Implementation of optimal search strategy
 * 
 * @author glauciom
 *
 */
public class CBinarySearchStrategy extends AbstractStrategy {

	private int k, n;

	public CBinarySearchStrategy(int[] subset) {
		this.subset = subset;
	}

	public CBinarySearchStrategy() {
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
		return binarySearch(subset, 0, 0, k, crumb);
	}

	private byte[] binarySearch(int[] subset, int i, int l, int k, Crumb crumb)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		int dc = crumb.dc(subset, identity);
	//	System.out.println(print(subset) + " " + dc);
		if (dc <= crumb.d) {
			if (dc == crumb.d) {
				byte[] result = crumb.processSubset(subset, identity);
				if (result != null) {
					return result;
				}
			}
			return binarySearch(slide(subset, l, k), i, l, k, crumb);
		} else {
			if (l == k - 1) {
				return binarySearch(jumpAndSlide(subset, i, l - 1, k), i, l - 1, k, crumb);
			}
			return binarySearch(jump(subset, i, l + 1, k), i, l + 1, k, crumb);
		}
	}

	private int[] slide(int[] subset, int l, int k) {
		int[] result = new int[k];
		for (int j = 0; j < l; j++) {
			result[j] = subset[j];
		}
		for (int j = l; j < k; j++) {
			result[j] = subset[j] - 1;
		}
		return result;
	}

	private int[] jump(int[] subset, int i, int l, int k) {
		int[] result = new int[k];
		for (int j = 0; j < l; j++) {
			result[j] = subset[j];
		}
		for (int j = l; j < k; j++) {
			result[j] = identity[j];
		}
		return result;
	}
	
	private int[] jumpAndSlide(int[] subset, int i, int l, int k) {
		int[] result = new int[k];
		for (int j = 0; j < l; j++) {
			result[j] = subset[j] - 1;
		}
		for (int j = l; j < k; j++) {
			result[j] = identity[j];
		}
		return result;
	}

	public String print(int[] subset) {
		String result = "";
		for (int i = 0; i < subset.length; i++) {
			result += subset[i] + " ";
		}
		return result;
	}

}

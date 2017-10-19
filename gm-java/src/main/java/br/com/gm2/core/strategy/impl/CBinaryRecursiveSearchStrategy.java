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
public class CBinaryRecursiveSearchStrategy extends AbstractStrategy {

	private int k, n;

	public CBinaryRecursiveSearchStrategy(int[] subset) {
		this.subset = subset;
	}

	public CBinaryRecursiveSearchStrategy() {
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
		return binarySearch(subset, 0, crumb);
	}

	private byte[] binarySearch(int[] subset, int i, Crumb crumb)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		int to = ulimit(subset, i);
		for (int j = 0; j < to; j++) {
			subset = slide(subset, j, i);
			int dc = crumb.dc(subset, identity);
			System.out.println(print(subset) + " " + dc);
			if (dc == crumb.d) {
				byte[] result = crumb.processSubset(subset, identity);
				if (result != null) {
					return result;
				}
			} else if (dc > crumb.d) {
				if (i != k - 1) {
					i++;
					int to2 = ulimit(subset, i);
					for (int g = 0; g < to2; g++) {
						subset = slide(subset, g, i);
						dc = crumb.dc(subset, identity);
						System.out.println(print(subset) + " " + dc);
						if (dc == crumb.d) {
							byte[] result = crumb.processSubset(subset, identity);
							if (result != null) {
								return result;
							}
						} else if (dc > crumb.d) {
							if (i != k - 1) {
								i++;
								int to3 = ulimit(subset, i);
								for (int h = 0; h < to3; h++) {
									subset = slide(subset, h, i);
									dc = crumb.dc(subset, identity);
									System.out.println(print(subset) + " " + dc);
									if (dc == crumb.d) {
										byte[] result = crumb.processSubset(subset, identity);
										if (result != null) {
											return result;
										}
									} else if (dc > crumb.d) {
										if (i != k - 1) {
											i++; // never enters here.
										} else {
											i--; // not found, roll back.
											break;
										}
									}
									if (h == to3 - 1) {
										i--;
									}
								}
							}
						}
						if (g == to2 - 1) {
							i--;
						}
					}
				}
			}
			if (j == to - 1) {
				i--;
			}
		}

		return null;
	}

	private int ulimit(int[] subset, int i) {
		if (i == 0) {
			return n - k + 1;
		} else {
			return identity[i] - subset[i];
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
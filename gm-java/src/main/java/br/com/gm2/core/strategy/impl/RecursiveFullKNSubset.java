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
 */
package br.com.gm2.core.strategy.impl;

/**
 * Class responsible for generate all Subsets of size N with K elements.
 * Documentation: https://arxiv.org/pdf/math/0503335.pdf
 * 
 * @author glauciom
 *
 */
public class RecursiveFullKNSubset {

	int rank;

	private int[] identity;
	private int[] complement;
	private int[] identityC;
	private int[] complementC;

	private int sum(int k, int[] y) {
		int z = 0;
		for (int i = 0; i < k; i++) {
			z += y[i];
		}
		return z + k;
	}

	private void doIt(int i, int n, int k, int[] x, int[] y, boolean show, int line) {
		for (x[i] = sum(i, y); x[i] <= n - (k - i); x[i]++) {
			if (i < k - 1) {
				doIt(i + 1, n, k, x, y, show, line);
			} else {
				if (k - 1 != 0) {
					y[k - 1] = x[k - 1] - x[k - 2] - 1;
				} else {
					y[k - 1] = x[0];
				}
				int q = 0;
				for (int r = 0; r < k; r++) {
					q += y[r];
				}
				y[k] = n - k - q;

				show(n, k, x, y, show, line);
			}
		}
		if (i < k - 1) {
			y[i] = 0;
		}
		if (i > 0) {
			y[i - 1]++;
		}
	}

	public long G(int[] subset, int[] search) {
		long result = 0;
		for (int i = 0; i < subset.length; i++) {
			result = dc(subset, search, result, i);
		}
		return result;
	}

	public long GL(int[] subset) {
		return G(subset, identity);
	}

	public long GU(int[] subset) {
		return G(subset, complement);
	}

	public long GCL(int[] subset) {
		return G(subset, identityC);
	}

	public long GCU(int[] subset) {
		return G(subset, complementC);
	}

	private long dc(int[] subset, int[] search, long result, int i) {
		double diff = Math.abs(subset[i] - search[i]);
		result += (diff * diff);
		return result;
	}

	public void fullKNSubSet(int n, int k, boolean show, int line) {
		int[] x = new int[k];
		int[] y = new int[k + 1];
		rank = 0;
		this.identity = new int[k];
		this.complement = new int[k];
		this.identityC = new int[k + 1];
		this.complementC = new int[k + 1];
		identityC[0] = n - k;
		complementC[k] = n - k;
		for (int i = 0; i < k; i++) {
			identity[i] = i;
			complement[i] = (n - k) + i;
		}
		doIt(0, n, k, x, y, show, line);
	}

	private int[] show(int n, int k, int[] x, int[] y, boolean show, int line) {
		if (show) {
			// double d = Math.sqrt(((GL(x) - GU(x)) * (GL(x) - GU(x))));
		//	System.out.print(rank + " " + GU(x) + " " + "\t");
			// System.out.println(rank +" " + GU(x));
			for (int i = 0; i < k; i++) {
				System.out.print(x[i] + " ");
			}
			// System.out.print("\t");
			// for (int i = k; i >= 0; i--) {
			// System.out.print(y[i] + " ");
			// }
			// System.out.print("\t");
			// byte[] compose = ComposeStruct.compose(n, k, x);
			// for (int i = 0; i <= k; i++) {
			// System.out.print(compose[i] + " ");
			// }
			System.out.println();
			rank++;
		}
		return x;
	}

	public static void main(String[] args) {
		int n = 8, k = 4;
		int line = 144;
		boolean showResults = true;
		RecursiveFullKNSubset c = new RecursiveFullKNSubset();

		// System.out.println("K-Subset: C(n,k): C(" + n + ", " + k + ")");
		// System.out.println("Composition: C(n, n - k): C(" + n + ", " + (n - k) +
		// ")");
		// System.out.println();
		// long r = System.currentTimeMillis();
		c.fullKNSubSet(n, k, showResults, line);
		// System.out.println("Time elapsed: " + (System.currentTimeMillis() - r) +
		// "ms");
	}

}

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
package br.com.gm2.core.strategy.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class BigOperator {

	private static int precision(String number) {
		return number.length() + (int) Math.ceil(Math.log(number.length()));
	}

	public static String log(String number) {
		return ApcomplexMath.log(new Apcomplex(new Apfloat(number, precision(number)))).toString(true);
	}

	public static String exp(String number) {
		BigDecimal big = new BigDecimal(
				ApcomplexMath.exp(new Apcomplex(new Apfloat(number, precision(number)))).toString(true));
		BigDecimal two = new BigDecimal(big.toBigInteger()).subtract(big);
		if (two.equals(BigDecimal.ZERO)) {
			return big.toString();
		} else if (two.compareTo(BigDecimal.ZERO) < 0) {
			return big.add(BigDecimal.ONE).toBigInteger().toString();
		}
		return big.toBigInteger().toString();

	}

	public static String error(String first, String second) {
		return new Apcomplex(first).subtract(new Apcomplex(second)).toString();
	}

	public static BigInteger getBinomialElements(int r, int s) {
		if ((r - s) < s) {
			return new BigInteger(factorial(r, s)).divide(new BigInteger(factorial(r - s, 1)));
		}
		return new BigInteger(factorial(r, r - s)).divide(new BigInteger(factorial(s, 1)));
	}

	private static String factorial(int r, int op) {
		BigInteger aux = BigInteger.ONE;
		for (int t = r; t > op; t--) {
			aux = aux.multiply(BigInteger.valueOf(t));
		}
		return aux.toString();
	}

	public static BigInteger pow(BigInteger base, BigInteger exponent) {
		BigInteger result = BigInteger.ONE;
		while (exponent.signum() > 0) {
			if (exponent.testBit(0))
				result = result.multiply(base);
			base = base.multiply(base);
			exponent = exponent.shiftRight(1);
		}
		return result;
	}

	public static void main(String[] args) {

		long time = System.currentTimeMillis();
		String k = "" + getBinomialElements(64, 32);
		System.out.println("length = " + k.length());
		String s = BigOperator.log(k);
		System.out.println("log = " + s.length());
		String s1 = BigOperator.exp(s);
		String err = BigOperator.error(k, s1);
		Apfloat ff = ApfloatMath.round(new Apfloat(s), 14, RoundingMode.FLOOR);
		Double f = Double.valueOf(ff.toString());
		System.out.println("K: " + k);
		System.out.println("truncated: " + f);
		System.out.println("first = " + s);
		System.out.println("second = " + s1);
		System.out.println("error = " + err);

		s1 = BigOperator.exp(f.toString());
		err = BigOperator.error(k, s1);

		System.out.println("Compare\n");
		System.out.println("truncated: " + f);
		System.out.println("first = " + s);
		System.out.println("second = " + s1);
		System.out.println("error = " + err);

		time = System.currentTimeMillis() - time;

		System.out.println(time);

	}

}
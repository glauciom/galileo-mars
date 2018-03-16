package br.com.gm2.core.strategy.core;

import java.math.BigDecimal;

public class BigPair {
	BigDecimal d;
	int precision;
	
	public BigPair(BigDecimal d, int precision) {
		this.d = d;
		this.precision = precision;
	}
}
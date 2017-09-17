package br.com.gm2.core.strategy.impl;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.strategy.UnpackStrategy;

public class BruteForceStrategy implements UnpackStrategy {

	@Override
	public byte[] execute(Crumb crumb) {
		// TODO Auto-generated method stub
		return new byte[] {49};
	}

}

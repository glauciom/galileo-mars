package br.com.gm2.core.strategy.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.strategy.DefaultStrategy;
import br.com.gm2.core.strategy.UnpackStrategy;

public abstract class AbstractStrategy implements DefaultStrategy, UnpackStrategy {

	protected int[] subset;
	protected int[] identity;

	public abstract void init(Crumb crumb);

	@Override
	public byte[] execute(Crumb crumb) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		this.init(crumb);
		return algorithm(crumb);
	}

}

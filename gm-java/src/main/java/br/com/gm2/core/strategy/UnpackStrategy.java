package br.com.gm2.core.strategy;

import br.com.gm2.core.element.Crumb;

public interface UnpackStrategy {

	public byte[] execute(Crumb crumb);

}

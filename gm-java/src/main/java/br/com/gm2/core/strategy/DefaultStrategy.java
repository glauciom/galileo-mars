package br.com.gm2.core.strategy;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import br.com.gm2.core.element.Crumb;

public interface DefaultStrategy {
	
    public byte[] execute(Crumb crumb) throws NoSuchAlgorithmException, UnsupportedEncodingException;


}

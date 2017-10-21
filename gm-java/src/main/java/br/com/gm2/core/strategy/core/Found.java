package br.com.gm2.core.strategy.core;

public class Found extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte[] value;
	
	public Found(byte[] value) {
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

}

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
package br.com.gm2.core.strategy;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import br.com.gm2.core.element.Crumb;

/**
 * Adding common capabilities to all Strategies.
 * @author glauciom
 *
 */
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

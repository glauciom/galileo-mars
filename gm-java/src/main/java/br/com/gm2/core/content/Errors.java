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
package br.com.gm2.core.content;

/**
 * Defines the Error Types for this program.
 * 
 * @author glauciom
 *
 */
public enum Errors {

	ERROR_0("Error Opening File"), ERROR_1("Error Writing file"), ERROR_2("Invalid Hash Algorithm"), ERROR_3("Error During file processing");

	private String message;

	Errors(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + ": " + message;
	}
}

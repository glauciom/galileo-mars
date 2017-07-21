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
package br.com.gm2.core.element;

import java.util.List;

/**
 * Global Header: 5 bytes. Crumb Header: Total: 8 bytes + 20 bytes of uniqueness
 * Global structure header: beginning of the file. Regular crumb packets: Right
 * after the Global Header Remaining Crumb Packet: last packet. Minimum content
 * size: 32 bytes + 1 byte meta data.
 * 
 * @author glauciom
 *
 */
public class GMFileFormat {
    public static final int BYTE_SIZE = 8;
    public static final String gm2 = ".gm2";
    GlobalHeader header;
    List<Crumb> crumbs;
}

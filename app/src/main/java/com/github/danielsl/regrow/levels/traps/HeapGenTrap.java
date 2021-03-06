/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.danielsl.regrow.levels.traps;

import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokoban;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanCorner;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanSwitch;

public class HeapGenTrap {

	private static final String name = "heap gen trap";	
	public static boolean gen = false;
	
	// 00x66CCEE

	public static void trigger(int pos, Char ch) {

		if (ch instanceof SheepSokoban || ch instanceof SheepSokobanCorner || ch instanceof SheepSokobanSwitch ){
			gen = true;		
		}
	}
}

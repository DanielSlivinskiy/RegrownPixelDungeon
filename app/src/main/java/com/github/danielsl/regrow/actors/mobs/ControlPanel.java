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
package com.github.danielsl.regrow.actors.mobs;

import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.buffs.Buff;
import com.github.danielsl.regrow.items.RedDewdrop;
import com.github.danielsl.regrow.sprites.DwarfKingTombSprite;

public class ControlPanel extends Mob  {

	{
		name = "dwarf king tomb";
		spriteClass = DwarfKingTombSprite.class;

		HP = HT = 6000;
		defenseSkill = 50;

		EXP = 10;
		
		hostile = false;
		state = PASSIVE;
		
		loot = new RedDewdrop();
		lootChance = 0.05f;
		
	}
	
	@Override
	public void beckon(int cell) {
		// Do nothing
	}
	
	@Override
	public void add(Buff buff) {
	}
	
	
	@Override
	public int damageRoll() {
		return 0;
	}
	
	@Override
	public int attackSkill(Char target) {
		return 0;
	}

	@Override
	public int dr() {
		return 80;
		
	}
	
	
	
	@Override
	public String description() {
		return "The tomb of the undead dwarf king "
				+ "it radiates a sickening power ";
	}
	
		
}

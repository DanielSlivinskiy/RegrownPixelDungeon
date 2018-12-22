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
package com.github.danielsl.regrow.items.misc;

import com.github.danielsl.regrow.sprites.ItemSpriteSheet;

public class Spectacles extends MiscEquippable {

	{
		name = "spectacles";
		image = ItemSpriteSheet.OTILUKES_SPECS;

		unique = true;
	}
	
	@Override
	protected MiscBuff buff() {
		return new MagicSight();
	}

	public class MagicSight extends MiscBuff {
	}
	
    @Override
	public String cursedDesc(){
		return "your " + this  + " are cursed";		
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public String desc() {
		return "I wonder what these do??? ";
	}


}

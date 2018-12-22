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
package com.github.danielsl.regrow.items.weapon.enchantments;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.buffs.Buff;
import com.github.danielsl.regrow.actors.mobs.Mob;
import com.github.danielsl.regrow.items.weapon.Weapon;
import com.github.danielsl.regrow.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.sprites.ItemSprite;
import com.github.danielsl.regrow.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class LokisPoison extends Weapon.Enchantment {

	private static final String TXT_VENOMOUS = "Loki's Venomous %s";

	private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x4400AA);

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}
	
	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		int level = Math.max(0, weapon.level);
		int distance = 1 + level*2;	
		
        for (Mob mob : Dungeon.level.mobs) {
			
        	if (Level.distance(attacker.pos, mob.pos) < distance && Random.Int(level + 3) >= 2) {

    			Buff.affect(
    					defender,
    					com.github.danielsl.regrow.actors.buffs.LokisPoison.class)
    					.set(com.github.danielsl.regrow.actors.buffs.LokisPoison
    							.durationFactor(defender) * (level + 1));
        	}
		}

		if (Random.Int(level + 3) >= 2) {

			Buff.affect(
					defender,
					com.github.danielsl.regrow.actors.buffs.LokisPoison.class)
					.set(com.github.danielsl.regrow.actors.buffs.LokisPoison
							.durationFactor(defender) * (level + 1));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Glowing glowing() {
		return PURPLE;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_VENOMOUS, weaponName);
	}

}

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

import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.buffs.Buff;
import com.github.danielsl.regrow.items.weapon.Weapon;
import com.github.danielsl.regrow.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.danielsl.regrow.sprites.ItemSprite;
import com.github.danielsl.regrow.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class SoulReaper extends Weapon.Enchantment {

    private static final String TXT_REAPING = "Reaping %s";

    private static ItemSprite.Glowing DARK_RED = new ItemSprite.Glowing(0x8c0c0c);

    @Override
    public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
        return false;
    }

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {

            Buff.prolong(
                    defender,
                    com.github.danielsl.regrow.actors.buffs.SoulMark.class,
                    2);

            return true;

    }

    @Override
    public Glowing glowing() {
        return DARK_RED;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_REAPING, weaponName);
    }

}

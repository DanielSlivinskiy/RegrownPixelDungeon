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

import java.util.HashSet;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.Actor;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.blobs.StenchGas;
import com.github.danielsl.regrow.actors.blobs.ToxicGas;
import com.github.danielsl.regrow.actors.buffs.Amok;
import com.github.danielsl.regrow.actors.buffs.Burning;
import com.github.danielsl.regrow.actors.buffs.Poison;
import com.github.danielsl.regrow.actors.buffs.Sleep;
import com.github.danielsl.regrow.actors.buffs.Terror;
import com.github.danielsl.regrow.actors.buffs.Vertigo;
import com.github.danielsl.regrow.items.Generator;
import com.github.danielsl.regrow.items.food.Meat;
import com.github.danielsl.regrow.items.scrolls.ScrollOfPsionicBlast;
import com.github.danielsl.regrow.items.weapon.enchantments.Death;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.sprites.GreyRatSprite;
import com.watabou.utils.Random;

public class GreyRat extends Mob {
	

	private static final float SPAWN_DELAY = 2f;

	{
		name = "grey rat";
		spriteClass = GreyRatSprite.class;

		HP = HT = 9+(Dungeon.depth*Random.NormalIntRange(1, 3));
		defenseSkill = 3+(Math.round((Dungeon.depth)/2));
		
		loot = new Meat();
		lootChance = 0.5f;
		
		lootOther = Generator.Category.MUSHROOM;
		lootChanceOther = 0.25f;

	}


	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(2, 5+Dungeon.depth);
	}

	@Override
	public int attackSkill(Char target) {
		return 5+Dungeon.depth;
	}

	@Override
	public int dr() {
		return 2;
	}

	@Override
	public String description() {
		return "Heartier cousins to Marsupial rats, Grey Rats have built up tolerances that allow "
				+ "them to live in the most polluted parts of the sewers. "
				+ "Probably still good to eat once cooked. ";
	}
	
	public static void spawnAround(int pos) {
		for (int n : Level.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}
	
	public static GreyRat spawnAt(int pos) {
		
        GreyRat b = new GreyRat();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Death.class);

	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(Amok.class);
		IMMUNITIES.add(Sleep.class);
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(Burning.class);
		IMMUNITIES.add(ScrollOfPsionicBlast.class);
		IMMUNITIES.add(Vertigo.class);
		IMMUNITIES.add(Poison.class);
		IMMUNITIES.add(StenchGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	

	
}

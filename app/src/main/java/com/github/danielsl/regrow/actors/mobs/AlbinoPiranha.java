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
import com.github.danielsl.regrow.Statistics;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.blobs.ToxicGas;
import com.github.danielsl.regrow.actors.buffs.Burning;
import com.github.danielsl.regrow.actors.buffs.Frost;
import com.github.danielsl.regrow.actors.buffs.Invisibility;
import com.github.danielsl.regrow.actors.buffs.Paralysis;
import com.github.danielsl.regrow.actors.buffs.Roots;
import com.github.danielsl.regrow.items.ConchShell;
import com.github.danielsl.regrow.items.Generator;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.items.food.Meat;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.sprites.AlbinoPiranhaSprite;
import com.github.danielsl.regrow.utils.GLog;
import com.watabou.utils.Random;

public class AlbinoPiranha extends Mob {
	
	private static final String TXT_KILLCOUNT = "Albino Piranha Kill Count: %s";

	{
		name = "albino giant piranha";
		spriteClass = AlbinoPiranhaSprite.class;

		baseSpeed = 2f;

		EXP = 0;
		
		loot = new Meat();
		lootChance = 0.1f;
		
	}

	public AlbinoPiranha() {
		super();

		HP = HT = 10 + Dungeon.depth * 5;
		defenseSkill = 10 + Dungeon.depth * 2;
	}

	protected boolean checkwater(int cell){
		return Level.water[cell];		
	}
	
		
	@Override
	protected boolean act() {
		
		if (!Level.water[pos]) {
			damage(HT, this);
			//die(null);
			return true;
					
				
		} else {
			// this causes pirahna to move away when a door is closed on them.
			Dungeon.level.updateFieldOfView(this);
			enemy = chooseEnemy();
			if (state == this.HUNTING
					&& !(enemy.isAlive() 
					&& Level.fieldOfView[enemy.pos] 
					&& Level.water[enemy.pos])) {
				state = this.WANDERING;
				int oldPos = pos;
				int i = 0;
				do {
					i++;
					target = Dungeon.level.randomDestination();
					if (i == 100)
						return true;
				} while (!getCloser(target));
				moveSprite(oldPos, pos);
				return true;
			}
			
			if (enemy.invisible>1){
				enemy.remove(Invisibility.class);
				GLog.w("No point being invisible when all the fish are blind!");
			}
			
			if (!Level.water[enemy.pos] || enemy.flying){
				enemy.invisible = 1;
			}	else {enemy.invisible = 0;}	
						
			return super.act();
		}
	}

	
	
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(Dungeon.depth, 4 + Dungeon.depth * 2);
	}

	@Override
	public int attackSkill(Char target) {
		return 20 + Dungeon.depth * 2;
	}

	@Override
	public int dr() {
		return Dungeon.depth;
	}

	@Override
	public void die(Object cause) {
		explodeDew(pos);
		if(Random.Int(105-Math.min(Statistics.albinoPiranhasKilled,100))==0){
		  Item mushroom = Generator.random(Generator.Category.MUSHROOM);
		  Dungeon.level.drop(mushroom, pos).sprite.drop();	
		}
		
		if(!Dungeon.limitedDrops.conchshell.dropped() && Statistics.albinoPiranhasKilled > 50 && Random.Int(10)==0) {
			Dungeon.limitedDrops.conchshell.drop();
			Dungeon.level.drop(new ConchShell(), pos).sprite.drop();
		}
		
		if(!Dungeon.limitedDrops.conchshell.dropped() && Statistics.albinoPiranhasKilled > 100) {
			Dungeon.limitedDrops.conchshell.drop();
			Dungeon.level.drop(new ConchShell(), pos).sprite.drop();
		}
		
		super.die(cause);

		Statistics.albinoPiranhasKilled++;
		GLog.w(TXT_KILLCOUNT, Statistics.albinoPiranhasKilled);
		//Badges.validatePiranhasKilled();
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	protected boolean getCloser(int target) {

		if (rooted) {
			return false;
		}

		int step = Dungeon.findPath(this, pos, target, Level.water,
				Level.fieldOfView);
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean getFurther(int target) {
		int step = Dungeon.flee(this, pos, target, Level.water,
				Level.fieldOfView);
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String description() {
		return "These huge blind cave fish inhabitant pools of underground water. "
				+"Vibrations in the water alert them to prey. "
				+ "They look ferocious and hungry.";
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(Burning.class);
		IMMUNITIES.add(Paralysis.class);
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Roots.class);
		IMMUNITIES.add(Frost.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}

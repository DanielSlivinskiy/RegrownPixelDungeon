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
import com.github.danielsl.regrow.actors.blobs.ConfusionGas;
import com.github.danielsl.regrow.actors.blobs.ToxicGas;
import com.github.danielsl.regrow.actors.buffs.Buff;
import com.github.danielsl.regrow.actors.buffs.Terror;
import com.github.danielsl.regrow.actors.hero.Hero;
import com.github.danielsl.regrow.effects.particles.SparkParticle;
import com.github.danielsl.regrow.items.RedDewdrop;
import com.github.danielsl.regrow.items.scrolls.ScrollOfPsionicBlast;
import com.github.danielsl.regrow.items.weapon.enchantments.Death;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.levels.traps.LightningTrap;
import com.github.danielsl.regrow.mechanics.Ballistica;
import com.github.danielsl.regrow.sprites.LitTowerSprite;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class LitTower extends Mob implements Callback {
	
	private static final float TIME_TO_ZAP = 2f;

	private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

	{
		name = "lightning tower";
		spriteClass = LitTowerSprite.class;

		HP = HT = 600;
		defenseSkill = 1000;

		EXP = 25;
		
		hostile = false;
		state = PASSIVE;
		
		loot = new RedDewdrop();
		lootChance = 1f;
		
	}
	
	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public int damageRoll() {
		return 0;
	}

		
	@Override
	public int attackSkill(Char target) {
		return 100;
	}

	@Override
	public int dr() {
		return 1000;
	}
	

	@Override
	public void damage(int dmg, Object src) {
	}
	
	@Override
	protected boolean act() {
		if(Level.distance(pos, Dungeon.hero.pos)<5 && Dungeon.hero.isAlive() && checkOtiluke()){
			zapAll(Dungeon.hero.pos);
		}
		return super.act();
	}
	
	@Override
	public void call() {
		next();
	}
	
	protected boolean checkOtiluke(){
      boolean check = false;
		
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof Otiluke) {
			check=true;	
			}
		}
		return check;
	}
	

	protected boolean heroNear (){
		boolean check=false;
		for (int i : Level.NEIGHBOURS9DIST2){
			int cell=pos+i;
			if (Actor.findChar(cell) != null	
				&& (Actor.findChar(cell) instanceof Hero)
				){
				check=true;
			}			
		}		
		return check;
	}
	
	
	@Override
	protected boolean canAttack(Char enemy) {
		return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {
		return false;
	}


	public void zapAll(int loc){
		
		yell("ZZZZZAAAAAAPPPPPP!!!!!!");
		
		Char hero=Dungeon.hero;
				
	    int mobDmg=Random.Int(300, 600);
		
		
		 boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[loc];
			
			
			  if (visible) {
				((LitTowerSprite) sprite).zap(loc);
			  }
			
			  
			  hero.damage(mobDmg, LightningTrap.LIGHTNING);

			  hero.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			  hero.sprite.flash();
			
			  Camera.main.shake(2, 0.3f);			
	}
	
	@Override
	public String description() {
		return "The lightning shell crackles with electric power. "
				+ "It's powerful lightning attack is drawn to all living things in the lair. ";
	}
	
	@Override
	public void add(Buff buff) {
	}
	

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
		RESISTANCES.add(LightningTrap.Electricity.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(ConfusionGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

	
}

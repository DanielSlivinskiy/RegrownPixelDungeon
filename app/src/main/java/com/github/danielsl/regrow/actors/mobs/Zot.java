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

import java.util.ArrayList;
import java.util.HashSet;

import com.github.danielsl.regrow.Assets;
import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.Actor;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.blobs.ToxicGas;
import com.github.danielsl.regrow.actors.buffs.Amok;
import com.github.danielsl.regrow.actors.buffs.Burning;
import com.github.danielsl.regrow.actors.buffs.Charm;
import com.github.danielsl.regrow.actors.buffs.Poison;
import com.github.danielsl.regrow.actors.buffs.Sleep;
import com.github.danielsl.regrow.actors.buffs.Terror;
import com.github.danielsl.regrow.actors.buffs.Vertigo;
import com.github.danielsl.regrow.actors.hero.HeroClass;
import com.github.danielsl.regrow.actors.mobs.npcs.OtilukeNPC;
import com.github.danielsl.regrow.effects.CellEmitter;
import com.github.danielsl.regrow.effects.Pushing;
import com.github.danielsl.regrow.effects.Speck;
import com.github.danielsl.regrow.items.misc.AutoPotion.AutoHealPotion;
import com.github.danielsl.regrow.items.scrolls.ScrollOfPsionicBlast;
import com.github.danielsl.regrow.items.weapon.enchantments.Death;
import com.github.danielsl.regrow.items.weapon.enchantments.Leech;
import com.github.danielsl.regrow.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.danielsl.regrow.items.weapon.missiles.JupitersWraith;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.mechanics.Ballistica;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.sprites.ZotSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Zot extends Mob {

	private static final int JUMP_DELAY = 5;

	{
		name = "Zot";
		spriteClass = ZotSprite.class;
		baseSpeed = 2f;

		HP = HT = Dungeon.playtest ? 1000 : 10000;
		EXP = 20;
		defenseSkill = 70;
	}

	private int timeToJump = JUMP_DELAY;
	
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(300, 400);
	}

	@Override
	public int attackSkill(Char target) {
		return 350;
	}

	@Override
	public int dr() {
		return 100;
	}

	@Override
	protected boolean act() {
		
		if (paralysed) {
			yell("ARRRGH!");
			
			if(!checkEyes()){
				ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

				for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
					int p = Dungeon.hero.pos + Level.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null
							&& (Level.passable[p] || Level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					MagicEye eye = new MagicEye();
					eye.pos = Random.element(spawnPoints);

					GameScene.add(eye);
					Actor.addDelayed(new Pushing(eye, pos, eye.pos), -1);
				}
			}
			
			if (HP < HT) {
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				HP = HP + 200;			
			}
		}

		
		boolean result = super.act();

		int regen = Dungeon.hero.buff(AutoHealPotion.class) != null ? 1 : Random.Int(50,100);
				
		
		if (HP < HT) {
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HP = HP + regen;			
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void die(Object cause) {
		
		Dungeon.level.locked=false;
		GameScene.bossSlain();		
		
		for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
			if (mob instanceof ZotPhase || mob instanceof MagicEye) {
				mob.die(cause);
				mob.destroy();
				mob.sprite.killAndErase();
			}
		}
		
		super.die(cause);
		yell("...");
		OtilukeNPC.spawnAt(pos);					
	}

	@Override
	protected boolean getCloser(int target) {
		if (Level.fieldOfView[target]) {
			jump();
			return true;
		} else {
			return super.getCloser(target);
		}
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {
		timeToJump--;
		if (timeToJump <= 0 && Level.adjacent(pos, enemy.pos)) {
			jump();
			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

	private void jump() {
		timeToJump = JUMP_DELAY;
		
		if (!checkPhases()){
			ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

			for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
				int p = pos + Level.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null
						&& (Level.passable[p] || Level.avoid[p])) {
					spawnPoints.add(p);
				}
			}

			if (spawnPoints.size() > 0) {
				ZotPhase zot = new ZotPhase();
				zot.pos = Random.element(spawnPoints);

				GameScene.add(zot);
				Actor.addDelayed(new Pushing(zot, pos, zot.pos), -1);
			}
		}
		
		int newPos;
		do {
			newPos = Random.Int(Level.getLength());
		} while (!Level.fieldOfView[newPos] || !Level.passable[newPos]
				|| Level.adjacent(newPos, enemy.pos)
				|| Actor.findChar(newPos) != null);

		sprite.move(pos, newPos);
		move(newPos);

		if (Dungeon.visible[newPos]) {
			CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
			Sample.INSTANCE.play(Assets.SND_PUFF);
		}

		spend(1 / speed());
	}
	
	private boolean checkPhases(){
		boolean check = false;
		int phases = 0;
		for (Mob mob : Dungeon.level.mobs) {
			if (mob != null && mob instanceof ZotPhase) {
				phases++;
				if (Dungeon.hero.heroClass!=HeroClass.HUNTRESS && phases>6){
				check=true;
				}else if (phases>10){
				  check=true;
				}
		}			
	  }
		return check;
	}
	
	private boolean checkEyes(){
		boolean check = false;
		int phases = 0;
		for (Mob mob : Dungeon.level.mobs) {
			if (mob != null && mob instanceof MagicEye) {
				phases++;
				if (Dungeon.hero.heroClass!=HeroClass.HUNTRESS && phases>20){
				check=true;
				}else if (phases>30){
				  check=true;
				}
		}			
	  }
		return check;
	}
	
	@Override
	public void damage(int dmg, Object src) {
		
		if(!(src instanceof RelicMeleeWeapon || src instanceof JupitersWraith)){
			int max = Math.round(dmg*.25f);
			dmg = Random.Int(1,max);
		}
		
		if(Dungeon.hero.heroClass==HeroClass.HUNTRESS && !checkPhases()){
			ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

			for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
				int p = Dungeon.hero.pos + Level.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null
						&& (Level.passable[p] || Level.avoid[p])) {
					spawnPoints.add(p);
				}
			}

			if (spawnPoints.size() > 0) {
				MagicEye eye = new MagicEye();
				eye.pos = Random.element(spawnPoints);

				GameScene.add(eye);
				Actor.addDelayed(new Pushing(eye, pos, eye.pos), -1);
			}
		}
		
		super.damage(dmg, src);
	}

	@Override
	public void notice() {
		super.notice();
		yell("...");
	}

	@Override
	public String description() {
		return "Zot.";
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Poison.class);
		RESISTANCES.add(Death.class);
		IMMUNITIES.add(Leech.class);
		IMMUNITIES.add(Death.class);
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(Amok.class);
		IMMUNITIES.add(Charm.class);
		IMMUNITIES.add(Sleep.class);
		IMMUNITIES.add(Burning.class);
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(ScrollOfPsionicBlast.class);
		IMMUNITIES.add(Vertigo.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}

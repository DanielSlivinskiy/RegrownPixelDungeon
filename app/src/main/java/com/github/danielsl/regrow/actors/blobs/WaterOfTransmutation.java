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
package com.github.danielsl.regrow.actors.blobs;

import com.github.danielsl.regrow.Journal;
import com.github.danielsl.regrow.Journal.Feature;
import com.github.danielsl.regrow.effects.BlobEmitter;
import com.github.danielsl.regrow.effects.Speck;
import com.github.danielsl.regrow.items.ActiveMrDestructo;
import com.github.danielsl.regrow.items.ActiveMrDestructo2;
import com.github.danielsl.regrow.items.Ankh;
import com.github.danielsl.regrow.items.Generator;
import com.github.danielsl.regrow.items.Generator.Category;
import com.github.danielsl.regrow.items.Honeypot;
import com.github.danielsl.regrow.items.Honeypot.ShatteredPot;
import com.github.danielsl.regrow.items.InactiveMrDestructo;
import com.github.danielsl.regrow.items.InactiveMrDestructo2;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.items.SteelHoneypot;
import com.github.danielsl.regrow.items.SteelHoneypot.SteelShatteredPot;
import com.github.danielsl.regrow.items.artifacts.Artifact;
import com.github.danielsl.regrow.items.food.Food;
import com.github.danielsl.regrow.items.food.PotionOfConstitution;
import com.github.danielsl.regrow.items.potions.Potion;
import com.github.danielsl.regrow.items.potions.PotionOfHealing;
import com.github.danielsl.regrow.items.potions.PotionOfMending;
import com.github.danielsl.regrow.items.potions.PotionOfMight;
import com.github.danielsl.regrow.items.potions.PotionOfOverHealing;
import com.github.danielsl.regrow.items.potions.PotionOfStrength;
import com.github.danielsl.regrow.items.rings.Ring;
import com.github.danielsl.regrow.items.scrolls.Scroll;
import com.github.danielsl.regrow.items.scrolls.ScrollOfMagicalInfusion;
import com.github.danielsl.regrow.items.scrolls.ScrollOfUpgrade;
import com.github.danielsl.regrow.items.wands.Wand;
import com.github.danielsl.regrow.items.weapon.melee.BattleAxe;
import com.github.danielsl.regrow.items.weapon.melee.Dagger;
import com.github.danielsl.regrow.items.weapon.melee.Glaive;
import com.github.danielsl.regrow.items.weapon.melee.Knuckles;
import com.github.danielsl.regrow.items.weapon.melee.Longsword;
import com.github.danielsl.regrow.items.weapon.melee.Mace;
import com.github.danielsl.regrow.items.weapon.melee.MeleeWeapon;
import com.github.danielsl.regrow.items.weapon.melee.Quarterstaff;
import com.github.danielsl.regrow.items.weapon.melee.Spear;
import com.github.danielsl.regrow.items.weapon.melee.Sword;
import com.github.danielsl.regrow.items.weapon.melee.WarHammer;
import com.github.danielsl.regrow.plants.Plant;

public class WaterOfTransmutation extends WellWater {

	@Override
	protected Item affectItem(Item item) {

		if (item instanceof MeleeWeapon) {
			item = changeWeapon((MeleeWeapon) item);
		} else if (item instanceof Scroll) {
			item = changeScroll((Scroll) item);
		} else if (item instanceof Potion) {
			item = changePotion((Potion) item);
		} else if (item instanceof Ring) {
			item = changeRing((Ring) item);
		} else if (item instanceof Wand) {
			item = changeWand((Wand) item);
		} else if (item instanceof Plant.Seed) {
			item = changeSeed((Plant.Seed) item);
		} else if (item instanceof Artifact) {
			item = changeArtifact((Artifact) item);
		} else if (item instanceof ShatteredPot) {
			item = changeHoneypot((ShatteredPot) item);
		} else if (item instanceof InactiveMrDestructo) {
			item = rechargeDestructo((InactiveMrDestructo) item);
		} else if (item instanceof ActiveMrDestructo) {
			item = upgradeDestructo((ActiveMrDestructo) item);
		} else if (item instanceof InactiveMrDestructo2) {
			item = rechargeDestructo2((InactiveMrDestructo2) item);
		} else if (item instanceof SteelShatteredPot) {
			item = changeHoneypot((SteelShatteredPot) item);
		} else if (item instanceof Honeypot) {
			item = changeHoneypot((Honeypot) item);
		} else if (item instanceof Ankh) {
			item = changeAnkh((Ankh) item);
		} else {
			item = null;
		}

		if (item != null) {
			Journal.remove(Feature.WELL_OF_TRANSMUTATION);
		}

		return item;

	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(Speck.factory(Speck.CHANGE), 0.2f, 0);
	}

	private MeleeWeapon changeWeapon(MeleeWeapon w) {

		MeleeWeapon n = null;

		if (w instanceof Knuckles) {
			n = new Dagger();
		} else if (w instanceof Dagger) {
			n = new Knuckles();
		}

		else if (w instanceof Spear) {
			n = new Quarterstaff();
		} else if (w instanceof Quarterstaff) {
			n = new Spear();
		}

		else if (w instanceof Sword) {
			n = new Mace();
		} else if (w instanceof Mace) {
			n = new Sword();
		}

		else if (w instanceof Longsword) {
			n = new BattleAxe();
		} else if (w instanceof BattleAxe) {
			n = new Longsword();
		}

		else if (w instanceof Glaive) {
			n = new WarHammer();
		} else if (w instanceof WarHammer) {
			n = new Glaive();
		}

		if (n != null) {

			int level = w.level;
			if (level > 0) {
				n.upgrade(level);
			} else if (level < 0) {
				n.degrade(-level);
			}

			n.enchantment = w.enchantment;
			n.levelKnown = w.levelKnown;
			n.cursedKnown = w.cursedKnown;
			n.cursed = w.cursed;

			return n;
		} else {
			return null;
		}
	}

	private Ring changeRing(Ring r) {
		Ring n;
		do {
			n = (Ring) Generator.random(Category.RING);
		} while (n.getClass() == r.getClass());

		n.level = 0;

		int level = r.level;
		if (level > 0) {
			n.upgrade(level);
		} else if (level < 0) {
			n.degrade(-level);
		}

		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.cursed = r.cursed;

		return n;
	}

	private Artifact changeArtifact(Artifact a) {
		Artifact n = Generator.randomArtifact();

		if (n != null) {
			n.cursedKnown = a.cursedKnown;
			n.cursed = a.cursed;
			n.levelKnown = a.levelKnown;
			n.transferUpgrade(a.visiblyUpgraded());
		}

		return n;
	}

	private Wand changeWand(Wand w) {

		Wand n;
		do {
			n = (Wand) Generator.random(Category.WAND);
		} while (n.getClass() == w.getClass());

		n.level = 0;
		n.updateLevel();
		n.upgrade(w.level);

		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;

		return n;
	}

	private Plant.Seed changeSeed(Plant.Seed s) {

		Plant.Seed n;

		do {
			n = (Plant.Seed) Generator.random(Category.SEED2);
		} while (n.getClass() == s.getClass());

		return n;
	}

	private Scroll changeScroll(Scroll s) {
		if (s instanceof ScrollOfUpgrade) {

			return new ScrollOfMagicalInfusion();

		} else if (s instanceof ScrollOfMagicalInfusion) {

			return new ScrollOfUpgrade();

		} else {

			Scroll n;
			do {
				n = (Scroll) Generator.random(Category.SCROLL);
			} while (n.getClass() == s.getClass());
			return n;
		}
	}

	private Potion changePotion(Potion p) {
		if (p instanceof PotionOfStrength) {

			return new PotionOfMight();

		} else if (p instanceof PotionOfMight) {

			return new PotionOfStrength();
		
		} else if (p instanceof PotionOfMending){
		
			return new PotionOfHealing();

		} else {

			Potion n;
			do {
				n = (Potion) Generator.random(Category.POTION);
			} while (n.getClass() == p.getClass());
			return n;
		}
	}

	
	private Potion changeAnkh(Ankh a) {
		return new PotionOfOverHealing();
	}
	
	private Food changeHoneypot(ShatteredPot s) {
		return new PotionOfConstitution();
	}
	
	private Item changeHoneypot(SteelShatteredPot s) {
		return new SteelHoneypot();
	}
	
	private Item changeHoneypot(Honeypot d) {
		return new SteelHoneypot();
	}
	
	private Item rechargeDestructo(InactiveMrDestructo d) {
		return new ActiveMrDestructo();
	}
	
	private Item upgradeDestructo(ActiveMrDestructo d) {
		return new ActiveMrDestructo2();
	}
	
	private Item rechargeDestructo2(InactiveMrDestructo2 d) {
		return new ActiveMrDestructo2();
	}
	
	
	
	@Override
	public String tileDesc() {
		return "Power of change radiates from the water of this well. "
				+ "Throw an item into the well to turn it into something else.";
	}
}

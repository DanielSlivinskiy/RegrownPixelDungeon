/*
` * Pixel Dungeon
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
package com.github.danielsl.regrow.actors.buffs;

import com.github.danielsl.regrow.Badges;
import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.ResultDescriptions;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.hero.Hero;
import com.github.danielsl.regrow.items.rings.RingOfElements.Resistance;
import com.github.danielsl.regrow.ui.BuffIndicator;
import com.github.danielsl.regrow.utils.GLog;
import com.watabou.utils.Bundle;

public class Poison extends Buff implements Hero.Doom {

	protected float left;

	private static final String LEFT = "left";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat(LEFT);
	}

	public void set(float duration) {
		this.left = duration;
	};

	@Override
	public int icon() {
		return BuffIndicator.POISON;
	}

	@Override
	public String toString() {
		return "Poisoned";
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {

			target.damage((int) (left / 2) + 1, this);
			spend(TICK);

			if ((left -= TICK) <= 0) {
				detach();
			}

		} else {

			detach();

		}

		return true;
	}

	public static float durationFactor(Char ch) {
		Resistance r = ch.buff(Resistance.class);
		return r != null ? r.durationFactor() : 1;
	}

	@Override
	public void onDeath() {
		Badges.validateDeathFromPoison();

		Dungeon.fail(ResultDescriptions.POISON);
		GLog.n("You died from poison...");
	}
}

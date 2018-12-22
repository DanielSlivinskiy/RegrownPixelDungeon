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
package com.github.danielsl.regrow.actors.mobs.npcs;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.buffs.Buff;
import com.github.danielsl.regrow.items.DewVial;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.items.Mushroom;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.sprites.TinkererSprite;
import com.github.danielsl.regrow.utils.Utils;
import com.github.danielsl.regrow.windows.WndQuest;
import com.github.danielsl.regrow.windows.WndTinkerer3;

public class Tinkerer3 extends NPC {

	{
		name = "tinkerer";
		spriteClass = TinkererSprite.class;
	}

	private static final String TXT_DUNGEON = "I'm scavenging for toadstool mushrooms. "
			+ "Could you bring me any toadstool mushrooms you find? ";
	
	
	private static final String TXT_DUNGEON2 = "Oh wow, have you seen this dungeon! This is an awesome dungeon.  ";

	private static final String TXT_MUSH = "Any luck finding toadstool mushrooms, %s?";

	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public String defenseVerb() {
		return "absorbed";
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public void interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);
		Item item = Dungeon.hero.belongings.getItem(Mushroom.class);
		Item vial = Dungeon.hero.belongings.getItem(DewVial.class);
			if (item != null && vial != null) {
				GameScene.show(new WndTinkerer3(this, item));
			} else if (item == null && vial != null) {
				tell(TXT_DUNGEON);
			} else {
				tell(TXT_DUNGEON2);
			}
		
	}

	private void tell(String format, Object... args) {
		GameScene.show(new WndQuest(this, Utils.format(format, args)));
	}

	@Override
	public String description() {
		return "The tinkerer is protected by a magical shield. ";
	}

}

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
package com.github.danielsl.regrow.levels.traps;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.ResultDescriptions;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.hero.Hero;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokoban;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanBlack;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanCorner;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.danielsl.regrow.effects.particles.ShadowParticle;
import com.github.danielsl.regrow.items.armor.Armor;
import com.github.danielsl.regrow.items.keys.IronKey;
import com.github.danielsl.regrow.items.wands.WandOfFlock.Sheep;
import com.github.danielsl.regrow.scenes.InterlevelScene;
import com.github.danielsl.regrow.sprites.HeroSprite;
import com.github.danielsl.regrow.utils.GLog;
import com.github.danielsl.regrow.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;

public class FleecingTrap {

	private static final String name = "fleecing trap";

	// 00x66CCEE

	public static void trigger(int pos, Char ch) {

		if (ch instanceof SheepSokoban || ch instanceof SheepSokobanCorner || ch instanceof SheepSokobanSwitch || ch instanceof Sheep || ch instanceof SheepSokobanBlack ){
			Camera.main.shake(2, 0.3f);
			ch.destroy();
			ch.sprite.killAndErase();
			ch.sprite.emitter().burst(ShadowParticle.UP, 5);
		
		} else if (ch != null) {
			
			int dmg = ch.HP;	
			boolean port=true;
						
			if (ch == Dungeon.hero) {
				
				Hero hero = Dungeon.hero;
			
				Armor armor = hero.belongings.armor; 
				if (armor!=null){
					hero.belongings.armor=null;
					GLog.n("The fleecing trap destroys your armor!");
					((HeroSprite) hero.sprite).updateArmor();
					dmg=dmg-1;
					port=false;
				}			
						
		    }
			
			//Port back to 1,1 or something
			
			Camera.main.shake(2, 0.3f);
			ch.sprite.emitter().burst(ShadowParticle.UP, 5);
			
			if (ch == Dungeon.hero && port) {
				 IronKey key = ((Hero)ch).belongings.getKey(IronKey.class, Dungeon.depth);
				 if (key!=null){key.detachAll(Dungeon.hero.belongings.backpack);}				
			  InterlevelScene.mode = InterlevelScene.Mode.SOKOBANFAIL;
			  Game.switchScene(InterlevelScene.class);
			}
									
			if (ch == Dungeon.hero) {

				Camera.main.shake(2, 0.3f);

				if (!ch.isAlive()) {
					Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
					GLog.n("You were killed by a discharge of a fleecing trap...");
				} 
			}
		}
		
		Dungeon.hero.next();

	}
	
	public static final Fleece FLEECE = new Fleece();

	public static class Fleece {
	}

	
}

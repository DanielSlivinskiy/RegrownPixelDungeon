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
package com.github.danielsl.regrow.levels;

import com.github.danielsl.regrow.Assets;
import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.DungeonTilemap;
import com.github.danielsl.regrow.actors.Actor;
import com.github.danielsl.regrow.actors.hero.HeroClass;
import com.github.danielsl.regrow.actors.mobs.MossySkeleton;
import com.github.danielsl.regrow.actors.mobs.npcs.Wandmaker;
import com.github.danielsl.regrow.effects.Halo;
import com.github.danielsl.regrow.effects.particles.FlameParticle;
import com.github.danielsl.regrow.items.Bomb;
import com.github.danielsl.regrow.levels.Room.Type;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class PrisonLevel extends RegularLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

	@Override
	protected boolean[] water() {
		return Patch.generate(feeling == Feeling.WATER ? 0.65f : 0.45f, 4);
	}

	@Override
	protected boolean[] grass() {
		return Patch.generate(feeling == Feeling.GRASS ? 0.60f : 0.40f, 3);
	}

	@Override
	protected boolean assignRoomType() {
		super.assignRoomType();

		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}

		return true;
	}
	
	@Override
	protected void setPar(){
		Dungeon.pars[Dungeon.depth] = 500+(Dungeon.depth*50)+(secretDoors*20);
	}

	@Override
	protected void createItems() {
		if (Dungeon.hero.heroClass==HeroClass.ROGUE && Random.Int(1) == 0){addItemToSpawn(new Bomb());}
		
		super.createItems();

		Wandmaker.Quest.spawn(this, roomEntrance);
		spawnSkeleton(this);
	}

	public static void spawnSkeleton(PrisonLevel level) {
		if (Dungeon.depth == 9 && !Dungeon.skeletonspawned){

			MossySkeleton skeleton = new MossySkeleton();
			do {
				skeleton.pos = level.randomRespawnCell();
			} while (skeleton.pos == -1);
			level.mobs.add(skeleton);
			Actor.occupyCell(skeleton);
           
			Dungeon.skeletonspawned = true;
		}
	}

	
	@Override
	protected void decorate() {

		for (int i = getWidth() + 1; i < getLength() - getWidth() - 1; i++) {
			if (map[i] == Terrain.EMPTY) {

				float c = 0.05f;
				if (map[i + 1] == Terrain.WALL
						&& map[i + getWidth()] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL
						&& map[i + getWidth()] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i + 1] == Terrain.WALL
						&& map[i - getWidth()] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL
						&& map[i - getWidth()] == Terrain.WALL) {
					c += 0.2f;
				}

				if (Random.Float() < c) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}

		for (int i = 0; i < getWidth(); i++) {
			if (map[i] == Terrain.WALL
					&& (map[i + getWidth()] == Terrain.EMPTY || map[i + getWidth()] == Terrain.EMPTY_SP)
					&& Random.Int(6) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}

		for (int i = getWidth(); i < getLength() - getWidth(); i++) {
			if (map[i] == Terrain.WALL
					&& map[i - getWidth()] == Terrain.WALL
					&& (map[i + getWidth()] == Terrain.EMPTY || map[i + getWidth()] == Terrain.EMPTY_SP)
					&& Random.Int(3) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}

		while (true) {
			int pos = roomEntrance.random();
			if (pos != entrance) {
				map[pos] = Terrain.SIGN;
				break;
			}
		}
		
		setPar();
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
		case Terrain.WATER:
			return "Dark cold water.";
		default:
			return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "There are old blood stains on the floor.";
		case Terrain.BOOKSHELF:
			return "This is probably a vestige of a prison library. Might it burn?";
		default:
			return super.tileDesc(tile);
		}
	}

	@Override
	public void addVisuals(Scene scene) {
		super.addVisuals(scene);
		addVisuals(this, scene);
	}

	public static void addVisuals(Level level, Scene scene) {
		for (int i = 0; i < getLength(); i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add(new Torch(i));
			}
		}
	}

	private static class Torch extends Emitter {

		private int pos;

		public Torch(int pos) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld(pos);
			pos(p.x - 1, p.y + 3, 2, 0);

			pour(FlameParticle.FACTORY, 0.15f);

			add(new Halo(16, 0xFFFFCC, 0.2f).point(p.x, p.y));
		}

		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}
}
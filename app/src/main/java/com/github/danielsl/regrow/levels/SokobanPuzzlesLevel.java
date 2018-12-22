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

import java.util.Collection;
import java.util.HashSet;

import com.github.danielsl.regrow.Assets;
import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.Actor;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.blobs.Alchemy;
import com.github.danielsl.regrow.actors.blobs.WellWater;
import com.github.danielsl.regrow.actors.mobs.Mob;
import com.github.danielsl.regrow.actors.mobs.SokobanSentinel;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokoban;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanBlack;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanCorner;
import com.github.danielsl.regrow.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.danielsl.regrow.items.Egg;
import com.github.danielsl.regrow.items.Gold;
import com.github.danielsl.regrow.items.Heap;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.items.Whistle;
import com.github.danielsl.regrow.items.artifacts.TimekeepersHourglass;
import com.github.danielsl.regrow.items.keys.IronKey;
import com.github.danielsl.regrow.items.potions.PotionOfLiquidFlame;
import com.github.danielsl.regrow.items.scrolls.ScrollOfMagicalInfusion;
import com.github.danielsl.regrow.items.scrolls.ScrollOfUpgrade;
import com.github.danielsl.regrow.items.wands.WandOfFlock.Sheep;
import com.github.danielsl.regrow.levels.features.Chasm;
import com.github.danielsl.regrow.levels.features.Door;
import com.github.danielsl.regrow.levels.features.HighGrass;
import com.github.danielsl.regrow.levels.traps.ActivatePortalTrap;
import com.github.danielsl.regrow.levels.traps.AlarmTrap;
import com.github.danielsl.regrow.levels.traps.ChangeSheepTrap;
import com.github.danielsl.regrow.levels.traps.FireTrap;
import com.github.danielsl.regrow.levels.traps.FleecingTrap;
import com.github.danielsl.regrow.levels.traps.GrippingTrap;
import com.github.danielsl.regrow.levels.traps.HeapGenTrap;
import com.github.danielsl.regrow.levels.traps.LightningTrap;
import com.github.danielsl.regrow.levels.traps.ParalyticTrap;
import com.github.danielsl.regrow.levels.traps.PoisonTrap;
import com.github.danielsl.regrow.levels.traps.SokobanPortalTrap;
import com.github.danielsl.regrow.levels.traps.SummoningTrap;
import com.github.danielsl.regrow.levels.traps.ToxicTrap;
import com.github.danielsl.regrow.plants.Flytrap;
import com.github.danielsl.regrow.plants.Phaseshift;
import com.github.danielsl.regrow.plants.Plant;
import com.github.danielsl.regrow.plants.Starflower;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SokobanPuzzlesLevel extends Level {


	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		WIDTH = 48;
		HEIGHT = 48;
		LENGTH = HEIGHT*WIDTH;
	}
	
	
	public HashSet<Item> heapstogen;
	public int[] heapgenspots;
	public int[] teleportspots;
	public int[] portswitchspots;
	public int[] teleportassign;
	public int[] destinationspots;
	public int[] destinationassign;
	public int prizeNo;
	
	private static final String HEAPSTOGEN = "heapstogen";
	private static final String HEAPGENSPOTS = "heapgenspots";
	private static final String TELEPORTSPOTS = "teleportspots";
	private static final String PORTSWITCHSPOTS = "portswitchspots";
	private static final String DESTINATIONSPOTS = "destinationspots";
	private static final String TELEPORTASSIGN = "teleportassign";
	private static final String DESTINATIONASSIGN= "destinationassign";
	private static final String PRIZENO = "prizeNo";
	
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HEAPSTOGEN, heapstogen);
		bundle.put(HEAPGENSPOTS, heapgenspots);
		bundle.put(TELEPORTSPOTS, teleportspots);
		bundle.put(PORTSWITCHSPOTS, portswitchspots);
		bundle.put(DESTINATIONSPOTS, destinationspots);
		bundle.put(DESTINATIONASSIGN, destinationassign);
		bundle.put(TELEPORTASSIGN, teleportassign);
		bundle.put(PRIZENO, prizeNo);
	}
	

	@Override
	public String tileName(int tile) {
		switch (tile) {
		case Terrain.WOOL_RUG:
		return "Wool rug";
	case Terrain.FLEECING_TRAP:
		return "Fleecing trap";
	case Terrain.CHANGE_SHEEP_TRAP:
		return "Change sheep trap";
	case Terrain.SOKOBAN_ITEM_REVEAL:
		return "Item creation switch";
	case Terrain.SOKOBAN_SHEEP:
		return "Floor";
	case Terrain.SWITCH_SOKOBAN_SHEEP:
		return "Floor";
	case Terrain.CORNER_SOKOBAN_SHEEP:
		return "Floor";
	case Terrain.BLACK_SOKOBAN_SHEEP:
		return "Floor";
	case Terrain.SOKOBAN_PORT_SWITCH:
		return "Portal switch";
	case Terrain.PORT_WELL:
		return "Portal";
		case Terrain.WATER:
			return "Dark cold water.";
		default:
			return super.tileName(tile);
		}
	}

	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.SOKOBAN_SHEEP:
			return "";
		case Terrain.SWITCH_SOKOBAN_SHEEP:
			return "";
		case Terrain.CORNER_SOKOBAN_SHEEP:
			return "";
		case Terrain.BLACK_SOKOBAN_SHEEP:
			return "";
		case Terrain.FLEECING_TRAP:
			return "Stepping onto a fleecing trap will destroy your armor or eject you from the level.";
		case Terrain.CHANGE_SHEEP_TRAP:
			return "This trap will change the form of any sheep.";
		case Terrain.SOKOBAN_ITEM_REVEAL:
			return "This switch creates an item somewhere on the level.";
		case Terrain.SOKOBAN_PORT_SWITCH:
			return "This switch turns on and off a portal somewhere.";
		case Terrain.PORT_WELL:
			return "This is a portal to another location on this level.";
			case Terrain.WOOL_RUG:
			return "A plush wool rug. Very nice!";
		case Terrain.EMPTY_DECO:
			return "There are old blood stains on the floor.";
		case Terrain.BOOKSHELF:
			return "This is probably a vestige of a prison library. Might it burn?";
		default:
			return super.tileDesc(tile);
		}
	}

	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		
		      super.restoreFromBundle(bundle);
		      
		      heapgenspots = bundle.getIntArray(HEAPGENSPOTS);
		      teleportspots = bundle.getIntArray(TELEPORTSPOTS);
		      portswitchspots = bundle.getIntArray(PORTSWITCHSPOTS);
		      destinationspots = bundle.getIntArray(DESTINATIONSPOTS);
		      destinationassign = bundle.getIntArray(DESTINATIONASSIGN);
		      teleportassign = bundle.getIntArray(TELEPORTASSIGN);
		      prizeNo = bundle.getInt(PRIZENO);
		      
		      heapstogen = new HashSet<Item>();
		      
		      Collection <Bundlable> collectionheap = bundle.getCollection(HEAPSTOGEN);
				for (Bundlable i : collectionheap) {
					Item item = (Item) i;
					if (item != null) {
						heapstogen.add(item);
					}
				}
	}

  @Override
  public void create() {
	   heapstogen = new HashSet<Item>();
	   heapgenspots = new int[20];
	   teleportspots = new int[10];
	   portswitchspots = new int[10];
	   destinationspots = new int[10];
	   destinationassign = new int[10];
	   teleportassign = new int[10];
	   super.create();	
   }	
  
  public void addItemToGen(Item item, int arraypos, int pos) {
		if (item != null) {
			heapstogen.add(item);
			heapgenspots[arraypos]=pos;
		}
	}
  
  
	public Item genPrizeItem() {
		return genPrizeItem(null);
	}
	
	
	public Item genPrizeItem(Class<? extends Item> match) {
		
		boolean keysLeft = false;
		
		if (heapstogen.size() == 0)
			return null;

		for (Item item : heapstogen) {
			if (match.isInstance(item)) {
				heapstogen.remove(item);
				keysLeft=true;
				return item;
			}
		}
		
		if (match == null || !keysLeft) {
			Item item = Random.element(heapstogen);
			heapstogen.remove(item);
			return item;
		}

		return null;
	}
	
	@Override
	public void press(int cell, Char ch) {

		if (pit[cell] && ch == Dungeon.hero) {
			Chasm.heroFall(cell);
			return;
		}

		TimekeepersHourglass.timeFreeze timeFreeze = null;

		if (ch != null)
			timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);

		boolean trap = false;
		
		switch (map[cell]) {

			case Terrain.FLEECING_TRAP:			
					
			if (ch != null && ch==Dungeon.hero){
				trap = true;
				FleecingTrap.trigger(cell, ch);
			}
			break;
			
		case Terrain.CHANGE_SHEEP_TRAP:
			
			if (ch instanceof SheepSokoban || ch instanceof SheepSokobanSwitch || ch instanceof SheepSokobanCorner || ch instanceof Sheep){
				trap = true;
				ChangeSheepTrap.trigger(cell, ch);
			}						
			break;
			
        case Terrain.PORT_WELL:
			
			if (ch != null && ch==Dungeon.hero){

				int portarray=-1;
				int destinationspot=cell;
				
				for(int i = 0; i < teleportspots.length; i++) {
					  if(teleportspots[i] == cell) {
						     portarray = i;
						     break;
						  }
				}
				
				if(portarray != -1) {
					destinationspot=destinationspots[portarray];
					if (destinationspot>0){
					SokobanPortalTrap.trigger(cell, ch, destinationspot);
					}
				}				
			}						
			break;
			
        case Terrain.SOKOBAN_PORT_SWITCH:
			trap=false;
			ActivatePortalTrap.trigger(cell, ch);
				
				/*	
				int arraypos = -1; //position in array of teleport switch
				int portpos = -1; //position on map of teleporter
				int portarraypos = -1; //position in array of teleporter
				int destpos = -1; //destination position assigned to switch
				
				for(int i = 0; i < portswitchspots.length; i++) {
				  if(portswitchspots[i] == cell) {
				     arraypos = i;
				     //GLog.i("Pos1 %s", arraypos);
				     break;
				  }
				}
				
				portpos = teleportassign[arraypos];
				destpos = destinationassign[arraypos];
				
				// Stepping on switch deactivates the portal 
				destpos = -1;
				
				//GLog.i("ass2 %s", portpos);
				//GLog.i("dest3 %s", destpos);
				
				for(int i = 0; i < teleportspots.length; i++) {
					  if(teleportspots[i] == portpos) {
						     portarraypos = i;
						    // GLog.i("Pos4 %s", portarraypos);
						     break;
						  }
				}
				
				if (map[portpos] == Terrain.PORT_WELL){
					destinationspots[portarraypos]=destpos;	
					GLog.i("Portal Deactivated!");
				}
				
				
			*/					
			break;


		case Terrain.HIGH_GRASS:
			HighGrass.trample(this, cell, ch);
			break;

		case Terrain.WELL:
			WellWater.affectCell(cell);
			break;

		case Terrain.ALCHEMY:
			if (ch == null) {
				Alchemy.transmute(cell);
			}
			break;

		case Terrain.DOOR:
			Door.enter(cell);
			break;
		}

		if (trap){

			if (Dungeon.visible[cell])
				Sample.INSTANCE.play(Assets.SND_TRAP);

			if (ch == Dungeon.hero)
				Dungeon.hero.interrupt();

			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);					
		} 

		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(ch);
		}
	}

	
	
	@Override
	public void mobPress(Mob mob) {

		int cell = mob.pos;

		if (pit[cell] && !mob.flying) {
			Chasm.mobFall(mob);
			return;
		}

		boolean trap = true;
		boolean fleece = false;
		boolean sheep = false;
		switch (map[cell]) {

		case Terrain.TOXIC_TRAP:
			ToxicTrap.trigger(cell, mob);
			break;

		case Terrain.FIRE_TRAP:
			FireTrap.trigger(cell, mob);
			break;

		case Terrain.PARALYTIC_TRAP:
			ParalyticTrap.trigger(cell, mob);
			break;
			
		case Terrain.FLEECING_TRAP:
			if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof SheepSokobanBlack || mob instanceof Sheep){
				fleece=true;
			}
			FleecingTrap.trigger(cell, mob);
			break;
			
		case Terrain.CHANGE_SHEEP_TRAP:
			trap=false;
			if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof Sheep){
				trap=true;
				ChangeSheepTrap.trigger(cell, mob);
			}						
			break;
			
		case Terrain.SOKOBAN_ITEM_REVEAL:
			trap=false;
			if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof SheepSokobanBlack || mob instanceof Sheep){
				HeapGenTrap.trigger(cell, mob);
				drop(genPrizeItem(IronKey.class),heapgenspots[prizeNo]);
				prizeNo++;
				sheep=true;
				trap=true;
			}						
			break;
			
		case Terrain.SOKOBAN_PORT_SWITCH:
			trap=false;
			if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof SheepSokobanBlack || mob instanceof Sheep){
				ActivatePortalTrap.trigger(cell, mob);
				
				/*
				public int[] teleportspots; location of teleports
				public int[] portswitchspots; location of switches
				public int[] teleportassign; assignment of teleports to switches
				public int[] destinationspots; current assignment of destination spots to teleports
				public int[] destinationassign; assignemnt of destination spots to switches
				*/
				
				int arraypos = -1; //position in array of teleport switch
				int portpos = -1; //position on map of teleporter
				int portarray = -1; //position in array of teleporter
				int destpos = -1; //destination position assigned to switch
				
				for(int i = 0; i < portswitchspots.length; i++) {
				  if(portswitchspots[i] == cell) {
				     arraypos = i;
				     //GLog.i("Pos1 %s", arraypos);
				     break;
				  }
				}
				
				portpos = teleportassign[arraypos];
				destpos = destinationassign[arraypos];
				
				//GLog.i("ass2 %s", portpos);
				//GLog.i("dest3 %s", destpos);
				
				for(int i = 0; i < teleportspots.length; i++) {
					  if(teleportspots[i] == portpos) {
						     portarray = i;
						    // GLog.i("Pos4 %s", portarray);
						     break;
						  }
				}
				
				if (map[portpos] == Terrain.PORT_WELL){
					destinationspots[portarray]=destpos;	
					GLog.i("Click!");
				}
				
				sheep=true;
			}						
			break;

		case Terrain.POISON_TRAP:
			PoisonTrap.trigger(cell, mob);
			break;

		case Terrain.ALARM_TRAP:
			AlarmTrap.trigger(cell, mob);
			break;

		case Terrain.LIGHTNING_TRAP:
			LightningTrap.trigger(cell, mob);
			break;

		case Terrain.GRIPPING_TRAP:
			GrippingTrap.trigger(cell, mob);
			break;

		case Terrain.SUMMONING_TRAP:
			SummoningTrap.trigger(cell, mob);
			break;

		case Terrain.DOOR:
			Door.enter(cell);

		default:
			trap = false;
		}

		if (trap && !fleece && !sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);
		}
		
		if (trap && fleece) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.WOOL_RUG);
			GameScene.updateMap(cell);
		} 	
		
		if (trap && sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.EMPTY);
			GameScene.updateMap(cell);
		}
	
		
		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(mob);
		}
		
		Dungeon.observe();
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
	protected boolean build() {
		
		map = SokobanLayouts.SOKOBAN_PUZZLE_LEVEL.clone();
		decorate();

		buildFlagMaps();
		cleanWalls();
		createSwitches();
		createSheep();
	
		entrance = 15 + WIDTH * 11;
		exit = 0;


		return true;
	}
	@Override
	protected void decorate() {
		//do nothing, all decorations are hard-coded.
	}

	@Override
	protected void createMobs() {
		
		/*
		    SokobanSentinel mob = new SokobanSentinel();
			mob.pos = 38 + WIDTH * 21;
			mobs.add(mob);
			Actor.occupyCell(mob);
		*/	
			SokobanSentinel mob2 = new SokobanSentinel();
			mob2.pos = 33 + WIDTH * 30;
			mobs.add(mob2);
			Actor.occupyCell(mob2);	
		/*	
		    SokobanSentinel mob3 = new SokobanSentinel();
			mob3.pos = 2 + WIDTH * 43;
			mobs.add(mob3);
			Actor.occupyCell(mob3);	
		*/	
	}
	
	

	protected void createSheep() {
		 for (int i = 0; i < LENGTH; i++) {				
				if (map[i]==Terrain.SOKOBAN_SHEEP){SheepSokoban npc = new SheepSokoban(); mobs.add(npc); npc.pos = i; Actor.occupyCell(npc);}
				else if (map[i]==Terrain.CORNER_SOKOBAN_SHEEP){SheepSokobanCorner npc = new SheepSokobanCorner(); mobs.add(npc); npc.pos = i; Actor.occupyCell(npc);}
				else if (map[i]==Terrain.SWITCH_SOKOBAN_SHEEP){SheepSokobanSwitch npc = new SheepSokobanSwitch(); mobs.add(npc); npc.pos = i; Actor.occupyCell(npc);}
				else if (map[i]==Terrain.BLACK_SOKOBAN_SHEEP){SheepSokobanBlack npc = new SheepSokobanBlack(); mobs.add(npc); npc.pos = i; Actor.occupyCell(npc);}
				else if (map[i]==Terrain.PORT_WELL){
				/*
					Portal portal = new Portal();
				    portal.seed(i, 1);
				   blobs.put(Portal.class, portal);
				*/
				}
				
			}
	}
	
	
	protected void createSwitches(){
		
		//spots where your portals are	
		teleportspots[0] = 11 + WIDTH * 10;	
		teleportspots[1] = 32 + WIDTH * 15;	
		teleportspots[2] = 25 + WIDTH * 40;	
		teleportspots[3] = 37 + WIDTH * 18;	
		teleportspots[4] = 45 + WIDTH * 33;	
		teleportspots[5] = 37 + WIDTH * 3;	
		teleportspots[6] = 43 + WIDTH * 2;	
			
		//spots where your portal switches are	
		portswitchspots[0] = 19 + WIDTH * 10;	
		portswitchspots[1] = 19 + WIDTH * 6;	
		portswitchspots[2] = 9 + WIDTH * 8;	
		portswitchspots[3] = 16 + WIDTH * 37;	
			
			
			
		//assign each switch to a portal	
		teleportassign[0] = 11 + WIDTH * 10;	
		teleportassign[1] = 11 + WIDTH * 10;	
		teleportassign[2] = 15 + WIDTH * 32;	
		teleportassign[3] = 37 + WIDTH * 3;	
			
			
			
		//assign each switch to a destination spot	
		destinationassign[0] = 30 + WIDTH * 16;	
		destinationassign[1] = 23 + WIDTH * 40;	
		destinationassign[2] = 37 + WIDTH * 16;	
		destinationassign[3] = 42 + WIDTH * 2;	
			
			
			
		//set the original destination of portals	
		destinationspots[0] = 0;	
		destinationspots[1] = 23 + WIDTH * 8;	
		destinationspots[2] = 23 + WIDTH * 8;	
		destinationspots[3] = 23 + WIDTH * 8;	
		destinationspots[4] = 34 + WIDTH * 6;	
		destinationspots[5] = 23 + WIDTH * 8;	
		destinationspots[6] = 23 + WIDTH * 8;	

	

				
	}
	

	@Override
	protected void createItems() {
		int goldmin=1; int goldmax=100;		
		if (first){
			goldmin=300; goldmax=500;
		}
		 for (int i = 0; i < LENGTH; i++) {				
				if (map[i]==Terrain.SOKOBAN_HEAP){
					if (first && Random.Int(5)==0){drop(new ScrollOfUpgrade(), i).type = Heap.Type.CHEST;}
					else {drop(new Gold(Random.Int(goldmin, goldmax)), i).type = Heap.Type.CHEST;}
				}
			}	
		 
		 addItemToGen(new IronKey(Dungeon.depth) , 0, 15 + WIDTH * 11);
		 addItemToGen(new IronKey(Dungeon.depth) , 1, 16 + WIDTH * 17);
		 addItemToGen(new IronKey(Dungeon.depth) , 2, 16 + WIDTH * 35);
		 addItemToGen(new IronKey(Dungeon.depth) , 3, 20 + WIDTH * 38);
		 addItemToGen(new IronKey(Dungeon.depth) , 4, 27 + WIDTH * 35);
		 addItemToGen(new IronKey(Dungeon.depth) , 5, 33 + WIDTH * 31);


		 if (first){
			 addItemToGen(new ScrollOfMagicalInfusion() , 6, 11 + WIDTH * 10);
			 addItemToGen(new ScrollOfMagicalInfusion() , 7, 41 + WIDTH * 2);
			 addItemToGen(new Egg() , 8, 41 + WIDTH * 2);
			 addItemToGen(new Phaseshift.Seed() , 9, 41 + WIDTH * 2);
			 addItemToGen(new Starflower.Seed() , 10, 41 + WIDTH * 2);
			 addItemToGen(new Flytrap.Seed() , 11, 41 + WIDTH * 2);
			 addItemToGen(new Phaseshift.Seed() , 12, 41 + WIDTH * 2);
			 addItemToGen(new Flytrap.Seed() , 13, 41 + WIDTH * 2);
			 addItemToGen(new Whistle() , 14, 41 + WIDTH * 2);
		 }
		 
		 drop(new PotionOfLiquidFlame(), 9 + WIDTH * 24).type = Heap.Type.CHEST;
	}

	@Override
	public int randomRespawnCell() {
		return -1;
	}
	

}

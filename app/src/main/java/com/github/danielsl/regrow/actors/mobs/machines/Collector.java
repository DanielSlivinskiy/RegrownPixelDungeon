package com.github.danielsl.regrow.actors.mobs.machines;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.windows.machineWindows.WndCollector;

import java.util.ArrayList;

public class Collector extends Machine{


    {
        name = "Collector";
    }

    protected static final float TIME_TO_PICK_UP = 1.0f;

    ArrayList<Item> collectedItems = new ArrayList<>();



    public ArrayList<Integer> AOE() {
        ArrayList<Integer> aoe = new ArrayList<>();

        for (int i : Level.NEIGHBOURS9) {
            aoe.add(this.pos + i + 2*getDirection());
        }
        return aoe;
    }

    public void doWork() {


        for (int n : AOE()) {
            if(Dungeon.level.heaps.get(n)!=null)
            collectedItems.add(Dungeon.level.heaps.get(n).pickUp());
        }
    }

    @Override
    public void interact(){
        GameScene.show(new WndCollector(this));
    }

    @Override
    public String description(){
        return "The Collector will put all items in a 3x3 are in front of it into it's own internal inventory, from which the items can then be picked up.";
    }

    public void collectItems(){
        for(Item i : collectedItems) {
            if(!i.doPickUpNoTime(Dungeon.hero)) i.doDrop(Dungeon.hero);

        }
        Dungeon.hero.spendAndNext(TIME_TO_PICK_UP);
        collectedItems.clear();
    }

}

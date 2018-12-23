package com.github.danielsl.regrow.actors.mobs.machines;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.windows.machineWindows.WndCollector;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

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

    public void collectItems(){
        for(Item i : collectedItems) {
            if(!i.doPickUpNoTime(Dungeon.hero)) i.doDrop(Dungeon.hero);

        }
        Dungeon.hero.spendAndNext(TIME_TO_PICK_UP);
        collectedItems.clear();
    }

    public static final String ITEMS = "collectorInventory";

//    @Override
//    public void storeInBundle(Bundle bundle) {
//        super.storeInBundle(bundle);
//        bundle.put(ITEMS, collectedItems);
//    }
//
//    @Override
//    public void restoreFromBundle(Bundle bundle) {
//        super.restoreFromBundle(bundle);
//        collectedItems.clear();
//        for (Bundlable item : bundle.getCollection(ITEMS)) {
//            if (item != null)
//                collectedItems.add((Item) item);
//        }
//
//    }

}

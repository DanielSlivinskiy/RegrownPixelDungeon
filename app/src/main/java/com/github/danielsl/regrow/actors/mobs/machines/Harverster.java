package com.github.danielsl.regrow.actors.mobs.machines;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.windows.machineWindows.WndCollector;

import java.util.ArrayList;

public class Harverster extends Machine {

    {
        name = "Harvester";
    }

    public ArrayList<Integer> AOE() {
        ArrayList<Integer> aoe = new ArrayList<>();

        for (int i : Level.NEIGHBOURS8) {
            aoe.add(this.pos + i);
        }
        return aoe;
    }

    public void doWork() {

        showAOE();
        for (int n : Level.NEIGHBOURS8) {
            int c = this.pos + n;

            Dungeon.level.press(c, this);
        }
    }

    @Override
    public void interact(){
    }

    @Override
    public String description(){
        return "The Harvester will trample all tall grass tiles in a 3x3 area around it.";
    }

}

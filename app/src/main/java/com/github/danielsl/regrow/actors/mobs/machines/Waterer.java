package com.github.danielsl.regrow.actors.mobs.machines;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.blobs.Blob;
import com.github.danielsl.regrow.actors.blobs.Water;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.levels.Terrain;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.windows.machineWindows.WndWaterer;

import java.util.ArrayList;

public class Waterer extends Machine{

    public ArrayList<Integer> AOE() {
        ArrayList<Integer> aoe = new ArrayList<>();

        for (int i : Level.NEIGHBOURS9) {
            aoe.add(this.pos + i + 2*getDirection());
        }
        return aoe;
    }

    public void doWork() {

            showAOE();
            for (int i : AOE()) {


                if (Dungeon.visible[i]) {
                    int c = Dungeon.level.map[i];

                    if (c == Terrain.GRASS) {
                        GameScene.add(Blob.seed(i, (2) * 20, Water.class));
                    }
                }
            }

    }

    @Override
    public void interact(){
        GameScene.show(new WndWaterer(this));
    }

}

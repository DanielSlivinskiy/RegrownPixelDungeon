package com.github.danielsl.regrow.actors.mobs.machines;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.Actor;
import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.mobs.Mob;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.windows.machineWindows.WndCollector;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Attractor extends Machine{


    {
        name = "Attractor";
    }

    public ArrayList<Integer> AOE() {
        ArrayList<Integer> aoe = new ArrayList<>();

        for (int i : Level.NEIGHBOURS9) {
            aoe.add(this.pos + i + 2*getDirection());
        }
        return aoe;
    }

    public void doWork() {
        for(int n : AOE()){

            Char m = Char.findChar(n);
            if(m != null && m instanceof Mob) {
                ((Mob) m).aggro(this);
            }
        }
    }

    @Override
    public String description(){
        return "";
    }

}

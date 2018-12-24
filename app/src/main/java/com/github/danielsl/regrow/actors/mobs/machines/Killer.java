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

public class Killer extends Machine{


    {
        name = "Killer";
    }

    public ArrayList<Integer> AOE() {
        ArrayList<Integer> aoe = new ArrayList<>();

            aoe.add(this.pos + getDirection());
            aoe.add(this.pos + 2*getDirection());
            aoe.add(this.pos + 3*getDirection());

        return aoe;
    }

    public void doWork() {
        for(int n : AOE()){

            Char m = Char.findChar(n);
            if(m != null && m instanceof Mob) {
                ((Mob)m).damage(4, this);
            }
        }
    }

    @Override
    public String description(){
        return "";
    }

}

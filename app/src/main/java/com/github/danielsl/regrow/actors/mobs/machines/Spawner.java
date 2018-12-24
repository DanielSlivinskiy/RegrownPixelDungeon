package com.github.danielsl.regrow.actors.mobs.machines;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.Actor;
import com.github.danielsl.regrow.actors.mobs.Mob;
import com.github.danielsl.regrow.actors.mobs.Rat;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.windows.machineWindows.WndCollector;
import com.github.danielsl.regrow.windows.machineWindows.WndSpawner;
import com.watabou.utils.Random;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Spawner extends Machine {

    Class<? extends Mob> soulClass = Rat.class;

    {
        name = "Spawner";
    }

    public ArrayList<Integer> AOE() {
        ArrayList<Integer> aoe = new ArrayList<>();

        for (int i : Level.NEIGHBOURS8) {
            aoe.add(this.pos + i);
        }
        return aoe;
    }

    public void setSoulClass(Class<? extends Mob> soulClass) {
        this.soulClass = soulClass;
    }



    public void doWork() {
    if(soulClass != null)
        for (int n : AOE()) {

            if(Random.Int(16) == 1){
                Mob mob;



                try {
                    mob = soulClass.getConstructor().newInstance();
                } catch (NoSuchMethodException e) {
                    mob = new Rat();
                } catch (InstantiationException e) {
                    mob = new Rat();
                } catch (IllegalAccessException e) {
                    mob = new Rat();
                } catch (InvocationTargetException e) {
                    mob = new Rat();
                }

                boolean[] passable = Level.passable;

                if (Actor.findChar(n) == null && passable[n]) {
                    mob.state = mob.WANDERING;
                    mob.pos = n;
                    GameScene.add(mob);
                }

            }
        }
    }

    @Override
    public void interact(){
        GameScene.show(new WndSpawner(this));
    }

    @Override
    public String description(){
        return "The Harvester will trample all tall grass tiles in a 3x3 area around it.";
    }

}

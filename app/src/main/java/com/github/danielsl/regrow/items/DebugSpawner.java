package com.github.danielsl.regrow.items;

import com.github.danielsl.regrow.Dungeon;
import com.github.danielsl.regrow.actors.hero.Hero;
import com.github.danielsl.regrow.actors.mobs.machines.Collector;
import com.github.danielsl.regrow.actors.mobs.machines.Harverster;
import com.github.danielsl.regrow.actors.mobs.machines.Waterer;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DebugSpawner extends Item {

    {
        name = "spawner";
        image = ItemSpriteSheet.POTION_AMBER;
        bones = false;
        stackable = false;
    }

    private static final String AC_COLLECTOR = "COLLECTOR";
    private static final String AC_HARVERSTER = "HARVESTER";
    private static final String AC_WATERER = "WATERER";


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

            actions.add(AC_COLLECTOR);
            actions.add(AC_HARVERSTER);
            actions.add(AC_WATERER);

        return actions;

    }

    public void execute(Hero hero, String action) {
        if (action.equals(AC_COLLECTOR)) {
            Collector collector = new Collector();
            collector.pos = hero.pos + 1;
            GameScene.add(collector);
        } else if (action.equals(AC_HARVERSTER)) {
            Harverster harverster = new Harverster();
            harverster.pos = hero.pos - 1;

            GameScene.add(harverster);

        } else if (action.equals(AC_WATERER)) {
            Waterer waterer = new Waterer();
            waterer.pos = hero.pos + Level.getWidth();

            GameScene.add(waterer);

        } else {
            super.execute(hero, action);
        }

    }
}

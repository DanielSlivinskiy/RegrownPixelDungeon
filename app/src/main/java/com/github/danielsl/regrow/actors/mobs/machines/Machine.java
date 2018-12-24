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
package com.github.danielsl.regrow.actors.mobs.machines;

import java.util.ArrayList;
import java.util.HashSet;

import com.github.danielsl.regrow.actors.Char;
import com.github.danielsl.regrow.actors.buffs.Buff;
import com.github.danielsl.regrow.actors.buffs.Paralysis;
import com.github.danielsl.regrow.actors.buffs.Roots;
import com.github.danielsl.regrow.actors.mobs.npcs.NPC;
import com.github.danielsl.regrow.effects.CheckedCell;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.levels.Level;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.sprites.TowerSprite;
import com.github.danielsl.regrow.windows.machineWindows.WndCollector;
import com.github.danielsl.regrow.windows.machineWindows.WndMachine;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Machine extends NPC {

    {
        name = "machine";
        spriteClass = TowerSprite.class;
        flying = false;

        state = PASSIVE;
    }

    public int orientation = 0;

    public int rotate(){
        this.orientation = this.orientation < 3 ? this.orientation+1 : 0;
        return this.orientation;
    }

    public abstract ArrayList<Integer> AOE();

    public void showAOE(){
        for (int n : AOE()) {

            sprite.parent.addToBack(new CheckedCell(n));
        }
    };

    public int getDirection(){
        switch (this.orientation){
            case 0:
                return 1;
            case 1:
                return Level.getWidth();
            case 2:
                return -1;
            case 3:
                return -Level.getWidth();
            default:
                return 1;
        }



    }

    @Override
    protected boolean act() {
        super.act();

        boolean justAlerted = alerted;
        alerted = false;

        this.doWork();

        boolean enemyInFOV = enemy != null && enemy.isAlive()
                && Level.fieldOfView[enemy.pos] && enemy.invisible<=0 ;

        return state.act(enemyInFOV, justAlerted);
    }

    public abstract void doWork();


    public Machine() {
        super();
    }

    @Override
    public String description() {
        return "An intricate arcane device designed to automate the most tedious of tasks.";
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    @Override
    public String defenseVerb() {
        return "evaded";
    }

    @Override
    public float speed() {
        return 1f;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(Roots.class);
    }

    @Override
    public void interact(){
        GameScene.show(new WndMachine(this));
    }


    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    public static final String ORIENTATION = "orientation";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ORIENTATION, orientation);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        orientation = bundle.getInt(ORIENTATION);


    }



}

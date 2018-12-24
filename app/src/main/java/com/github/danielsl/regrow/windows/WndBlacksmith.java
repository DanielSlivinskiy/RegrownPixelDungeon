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
package com.github.danielsl.regrow.windows;

import com.github.danielsl.regrow.Assets;
import com.github.danielsl.regrow.Chrome;
import com.github.danielsl.regrow.actors.hero.Hero;
import com.github.danielsl.regrow.actors.mobs.npcs.Blacksmith;
import com.github.danielsl.regrow.items.Item;
import com.github.danielsl.regrow.scenes.GameScene;
import com.github.danielsl.regrow.scenes.PixelScene;
import com.github.danielsl.regrow.ui.ItemSlot;
import com.github.danielsl.regrow.ui.RedButton;
import com.github.danielsl.regrow.ui.Window;
import com.github.danielsl.regrow.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

public class WndBlacksmith extends Window {

	private static final int BTN_SIZE = 36;
	private static final float GAP = 2;
	private static final float BTN_GAP = 10;
	private static final int WIDTH = 116;

	private ItemButton btnPressed;

	private ItemButton btnItem1;
	private ItemButton btnItem2;
	private RedButton btnReforge;

	private static final String TXT_PROMPT = "Ok, a deal is a deal, dat's what I can do for you: I can reforge "
			+ "any 2 items and turn them into one of a better quality. "
			+ "The first item will get some or all of the upgrades from the second. "
			+ "The second item will be destroyed. "
			+ "I'm more successful when you bring me lots of dark gold. ";
	private static final String TXT_SELECT1 = "Select an item to enhance.";
	private static final String TXT_SELECT2 = "Select an item to melt down.";
	private static final String TXT_REFORGE = "Reforge them";

	public WndBlacksmith(Blacksmith troll, Hero hero) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(troll.sprite());
		titlebar.label(Utils.capitalize(troll.name));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		BitmapTextMultiline message = PixelScene.createMultiline(TXT_PROMPT, 6);
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add(message);

		btnItem1 = new ItemButton() {
			@Override
			protected void onClick() {
				btnPressed = btnItem1;
				GameScene.selectItem(itemSelector, WndBag.Mode.UPGRADEABLESIMPLE,
						TXT_SELECT1);
			}
		};
		btnItem1.setRect((WIDTH - BTN_GAP) / 2 - BTN_SIZE,
				message.y + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE);
		add(btnItem1);

		btnItem2 = new ItemButton() {
			@Override
			protected void onClick() {
				btnPressed = btnItem2;
				GameScene.selectItem(itemSelector, WndBag.Mode.UPGRADEABLESIMPLE,
						TXT_SELECT2);
			}
		};
		btnItem2.setRect(btnItem1.right() + BTN_GAP, btnItem1.top(), BTN_SIZE,
				BTN_SIZE);
		add(btnItem2);

		btnReforge = new RedButton(TXT_REFORGE) {
			@Override
			protected void onClick() {
				Blacksmith.upgrade(btnItem1.item, btnItem2.item);
				hide();
			}
		};
		btnReforge.enable(false);
		btnReforge.setRect(0, btnItem1.bottom() + BTN_GAP, WIDTH, 20);
		add(btnReforge);

		resize(WIDTH, (int) btnReforge.bottom());
	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				btnPressed.item(item);

				if (btnItem1.item != null && btnItem2.item != null) {
					String result = Blacksmith.verify(btnItem1.item,
							btnItem2.item);
					if (result != null) {
						GameScene.show(new WndMessage(result));
						btnReforge.enable(false);
					} else {
						btnReforge.enable(true);
					}
				}
			}
		}
	};


}

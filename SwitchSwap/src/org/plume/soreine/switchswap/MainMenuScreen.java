package org.plume.soreine.switchswap;

import org.plume.soreine.framework.Game;
import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Screen;

import android.graphics.Color;

public class MainMenuScreen extends Screen {

	private Graphics g;
	
	public MainMenuScreen(Game game) {
		super(game);
		g = game.getGraphics();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(float deltaTime) {
		g.drawRect(0, 0, g.getWidth(), g.getHeight(), Color.RED);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub

	}

}

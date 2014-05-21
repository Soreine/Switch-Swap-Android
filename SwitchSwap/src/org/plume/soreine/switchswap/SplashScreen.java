package org.plume.soreine.switchswap;

import org.plume.soreine.framework.Game;
import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Screen;
import org.plume.soreine.framework.Graphics.ImageFormat;

import android.graphics.Color;
import android.util.Log;

public class SplashScreen extends Screen {

	private boolean firstTime = true;
	private long chrono1;

	public SplashScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Log.d("SplashScreen", "Update");
		if (firstTime) {
			Graphics g = game.getGraphics();
			chrono1 = System.currentTimeMillis();
			// Load all the stuff in Assets
			firstTime = false;
		}

		// Continue to Main Menu
		long chrono2 = System.currentTimeMillis();
		if (chrono2 - chrono1 > 4000)
			game.setScreen(new SwitchSwapScreen(game));
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		int logoHeight = Assets.logoMoon.getHeight();
		int logoWidth = Assets.logoMoon.getWidth();
		int bufferHeight = g.getHeight();
		int bufferWidth = g.getWidth();
		int ratio = Math
				.min(bufferHeight / logoHeight, bufferWidth / logoWidth) / 2;
		g.drawRect(0, 0, bufferWidth, bufferHeight, Color.WHITE);
		g.drawScaledImage(Assets.logoMoon,
				(bufferWidth - logoWidth * ratio) / 2,
				(bufferHeight - logoHeight * ratio) / 2, logoWidth * ratio,
				logoHeight * ratio, 0, 0, logoWidth, logoHeight);
		;
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

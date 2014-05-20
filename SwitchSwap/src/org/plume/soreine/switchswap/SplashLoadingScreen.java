package org.plume.soreine.switchswap;

import org.plume.soreine.framework.Game;
import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Screen;
import org.plume.soreine.framework.Graphics.ImageFormat;

public class SplashLoadingScreen extends Screen {

	public SplashLoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.logoMoon = g.newImage("logo-moon.png", ImageFormat.ARGB4444);
		Assets.soundMoon = game.getAudio().createSound("SoreineSplash.ogg");
		game.setScreen(new SplashScreen(game));

	}

	@Override
	public void paint(float deltaTime) {
		// TODO Auto-generated method stub

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

package org.plume.soreine.framework;

public abstract class Screen {

	protected final Game game;

	public Screen(Game game) {
		this.game = game;
	}

	// deltaTime in milliseconds
	public abstract void update(float deltaTime);

	// deltaTime in milliseconds
	public abstract void paint(float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

	public abstract void backButton();
}

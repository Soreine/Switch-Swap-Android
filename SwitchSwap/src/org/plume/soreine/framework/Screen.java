package org.plume.soreine.framework;

public abstract class Screen {

	protected final Game game;

	public Screen(Game game) {
		this.game = game;
	}

	// ms_delta in milliseconds
	public abstract void update(float ms_delta);

	/**
	 * @param advancement : the progression between the last frame updated and the next frame.
	 */
	public abstract void paint(float advancement);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

	public abstract void backButton();
}

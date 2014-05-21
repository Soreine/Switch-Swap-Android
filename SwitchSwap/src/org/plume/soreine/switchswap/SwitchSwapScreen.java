package org.plume.soreine.switchswap;

import java.util.List;

import org.plume.soreine.framework.Game;
import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Screen;
import org.plume.soreine.framework.Input.TouchEvent;
import org.plume.soreine.framework.implementation.AndroidGame;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class SwitchSwapScreen extends Screen {

	private Graphics g;
	private static int width, height;

	enum GameState {
		RUNNING, PAUSED, GAMEOVER
	}

	private static GameState state;

	private static Board board;

	public SwitchSwapScreen(Game game) {
		super(game);
		g = game.getGraphics();

		int rows = 3;
		int columns = 3;
		int numberOfColor = 2;

		width = g.getWidth();
		height = g.getHeight();

		int offset = width / 20;

		// (int x, int y, int width, int height, int rows, int columns, int
		// colorNumber)
		board = new Board(offset, offset, width - 2 * offset, width - 2
				* offset, rows, columns, numberOfColor);

		state = GameState.RUNNING;

	}

	@Override
	public void update(float deltaTime) {

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		for (TouchEvent event : touchEvents) {
			if (event.type == TouchEvent.TOUCH_UP)
				board.handleEvent(event);
		}

		board.update(deltaTime);
		switch (state) {
		case RUNNING:
			break;
		case GAMEOVER:
			break;
		case PAUSED:
			break;
		default:
			break;
		}

	}

	private void updateRunning(List<TouchEvent> touchEvents) {
		for (TouchEvent event : touchEvents) {
		}
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		for (TouchEvent event : touchEvents) {
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		for (TouchEvent event : touchEvents) {
		}
	}

	@Override
	public void paint(float deltaTime) {

		// Draw background
		g.clearScreen(Color.BLACK);

		board.draw(g);

		switch (state) {
		case GAMEOVER:
			break;
		case PAUSED:
			break;
		case RUNNING:
			break;
		default:
			break;
		}

	}

	@Override
	public void pause() {
		// state = GameState.PAUSED;
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
		Log.d("backButton()", "Call to backbutton");
		// Display an exit dialog
		ExitDialogFragment dialog = new ExitDialogFragment();
		dialog.show(((AndroidGame) game).getSupportFragmentManager(), "test");
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

}

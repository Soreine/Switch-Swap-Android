package org.plume.soreine.switchswap;

import java.util.List;

import org.plume.soreine.framework.Game;
import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Input.TouchEvent;
import org.plume.soreine.framework.Screen;
import org.plume.soreine.framework.implementation.AndroidGame;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class SwitchSwapScreen extends Screen {

	private Graphics g;
	private static int width, height;
	private static final int backgroundColor = Color.WHITE;
			
	private static boolean showFPS = true;
	private int timePainted = 0;
	private Paint fpsPaint;
	private long lastRendered;
	private int fps = 0;

	enum GameState {
		RUNNING, PAUSED, GAMEOVER
	}

	private static GameState state;

	private static Board board;

	public SwitchSwapScreen(Game game) {
		super(game);
		g = game.getGraphics();

		lastRendered = System.currentTimeMillis();
		fpsPaint = new Paint();
		fpsPaint.setTextSize(width / 30);
		fpsPaint.setTextAlign(Paint.Align.LEFT);
		fpsPaint.setAntiAlias(true);
		fpsPaint.setColor(Color.RED);

		int rows = 4;
		int columns = 6;
		int numberOfColor = 2;

		width = g.getWidth();
		height = g.getHeight();

		int offset = width / 30;

		// (int x, int y, int width, int height, int rows, int columns, int
		// colorNumber)
		board = new Board(offset, offset, width - 2 * offset, width - 2
				* offset, rows, columns, numberOfColor);

		board.instantShuffle(rows * columns);

		state = GameState.RUNNING;
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		for (TouchEvent event : touchEvents) {
			if (event.type == TouchEvent.TOUCH_DOWN)
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
		g.clearScreen(backgroundColor);

		board.draw(g);

		// Show fps
		timePainted++;
		if (showFPS && (timePainted > 30)) {
			long currentTime = System.currentTimeMillis();
			fps = (int) ((timePainted*1000) / (currentTime - lastRendered));
			lastRendered = currentTime;
			timePainted = 0;
		}

		g.drawString("FPS " + fps, (int) (width * 0.02), (int) (height * 0.99),
				fpsPaint);

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

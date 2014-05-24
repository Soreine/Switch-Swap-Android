package org.plume.soreine.switchswap;

import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Input.TouchEvent;
import android.graphics.Color;

public class Tile {

	public enum Moving {
		UP, DOWN, RIGHT, LEFT, STILL
	}

	// // Double buffered variables ? TODO
	// private int[] state = { 0, 0 };
	// private Moving[] moving = { Moving.STILL, Moving.STILL };
	// private boolean[] mustPropagate = { false, false };
	// private double[] rotation = { 0, 0 };
	// private int current = 0;

	private static int sideColor = Color.rgb(128, 128, 128);

	private int state;

	private Moving moving;

	private int x, y, sizeX, sizeY, maxState;

	// The index on the board
	public int i, j;
	private Board board;

	private boolean mustPropagate = false;

	private double rotation = 0;

	private static final int MS_ROTATION_TIME = 500;

	public Tile(int x, int y, int sizeX, int sizeY, int maxState, int initState) {
		assert (maxState > initState);

		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.maxState = maxState;
		this.state = initState;
		this.moving = Moving.STILL;
	}

	// With random initial state
	public Tile(int x, int y, int sizeX, int sizeY, int maxState) {
		this(x, y, sizeX, sizeY, maxState, 0);
	}

	public void setLocationOnBoard(Board board, int i, int j) {
		this.i = i;
		this.j = j;
		this.board = board;
	}

	public void instantSwap() {
		this.moving = Moving.STILL;
		this.state = (this.state + 1) % maxState;
	}

	public void swap(Moving move) {
		if (mustPropagate) {
			propagate();
		}
		this.instantSwap();
		this.mustPropagate = true;
		this.moving = move;
		this.rotation = 0;
	}

	public void swapAlone(Moving move) {
		if (mustPropagate) {
			propagate();
		}
		this.instantSwap();
		this.moving = move;
		this.mustPropagate = false;
		this.rotation = 0;
	}

	public void draw(Graphics g) {
		if (moving == Moving.STILL) {
			g.drawRect(x, y, sizeX, sizeY, board.palette(state));
		} else {
			drawTurning(g);
		}

	}

	public void drawTurning(Graphics g) {
		// The visible color
		int color = (rotation > 0.5) ? board.palette(state) : board
				.palette((state - 1 + maxState) % maxState);

		double beta = Math.atan(0.2);
		double alpha = Math.PI * rotation;
		// The projected coordinates of the tile vertices, relative to the tile
		// size

		double sideBegin, sideEnd, tileBegin, tileEnd;

		sideBegin = (1 - Math.cos(alpha - beta)) / 2;
		sideEnd = (1 - Math.cos(alpha + beta)) / 2;
		if (rotation > 0.5) {
			// rotation > 0.5
			tileEnd = sideBegin;
			tileBegin = 1 - sideEnd;
		} else {
			// rotation < 0.5
			tileBegin = sideEnd;
			tileEnd = 1 - sideBegin;
		}

		// The ratio giving the visible size of the facets
		double sideRatio = Math.abs(sideEnd - sideBegin);
		double tileRatio = Math.abs(tileEnd - tileBegin);

		// The face
		int tileX = x;
		int tileY = y;
		int tileSizeX = sizeX;
		int tileSizeY = sizeY;
		// The side
		int sideX = x;
		int sideY = y;
		int sideSizeX = sizeX;
		int sideSizeY = sizeY;

		switch (moving) {
		case DOWN:
			tileY = y + ((int) Math.floor(tileBegin * sizeY));
			tileSizeY = (int) Math.ceil(tileRatio * sizeY) + 1;
			// The side
			sideY = y + ((int) Math.floor(sideBegin * sizeY));
			sideSizeY = (int) Math.ceil(sideRatio * sizeY) + 1;
			break;
		case UP:
			tileY = y + ((int) Math.floor((1 - tileEnd) * sizeY));
			tileSizeY = (int) Math.ceil(tileRatio * sizeY) + 1;
			// The side
			sideY = y + ((int) Math.floor((1 - sideEnd) * sizeY));
			sideSizeY = (int) Math.ceil(sideRatio * sizeY) + 1;
			break;
		case RIGHT:
			tileX = x + ((int) Math.floor(tileBegin * sizeX));
			tileSizeX = (int) Math.ceil(tileRatio * sizeX) + 1;
			// The side
			sideX = x + ((int) Math.floor(sideBegin * sizeX));
			sideSizeX = (int) Math.ceil(sideRatio * sizeX) + 1;
			break;
		case LEFT:
			tileX =  x + ((int) Math.floor((1 - tileEnd) * sizeX));
			tileSizeX = (int) Math.ceil(tileRatio * sizeX) + 1;
			// The side
			sideX = x + ((int) Math.floor((1 - sideEnd) * sizeX));
			sideSizeX = (int) Math.ceil(sideRatio * sizeX) + 1;
			break;
		default:
			break;
		}

		// The tile facet
		g.drawRect(tileX, tileY, tileSizeX, tileSizeY, color);
		// The side facet
		g.drawRect(sideX, sideY, sideSizeX, sideSizeY, sideColor);
	}

	public boolean touch(TouchEvent event) {
		return Utility.inBounds(event, x, y, sizeX, sizeY);
	}

	public void update(float deltaTime) {
		if (moving != Moving.STILL) {

			int numberOfTiles = (moving == Moving.UP || moving == Moving.DOWN) ? board
					.getRows() : board.getColumns();

			rotation += deltaTime / MS_ROTATION_TIME;
			if (mustPropagate && rotation > 2.0 / ((double) numberOfTiles)) {
				propagate();
			}
			if (rotation > 1) {
				rotation = 0;
				moving = Moving.STILL;
			}
		}
	}

	private void propagate() {
		mustPropagate = false;
		board.propagateFrom(moving, i, j);
	}

}

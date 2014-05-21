package org.plume.soreine.switchswap;

import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Input.TouchEvent;
import android.graphics.Color;

public class Tile {

	public enum Moving {
		UP, DOWN, RIGHT, LEFT, STILL
	}

	private static int sideColor = Color.GRAY;
	private static int[] palette = { Color.rgb(199, 231, 136),
			Color.rgb(183, 136, 231), Color.rgb(136, 190, 231) };

	private int state;

	private Moving moving;

	private int x, y, sizeX, sizeY, maxState;

	// The index on the board
	public int i, j;
	private Board board;

	private boolean mustPropagate = false;

	private double rotation = 0;

	private static final int MS_ROTATION_TIME = 1000;

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
		this.instantSwap();
		this.moving = move;
		this.mustPropagate = true;
		this.rotation = 0;
	}

	public void swapAlone(Moving move) {
		this.instantSwap();
		this.moving = move;
		this.mustPropagate = false;
		this.rotation = 0;
	}

	public void draw(Graphics g) {
		// if (moving == Moving.STILL) {
			g.drawRect(x, y, sizeX, sizeY, palette[state]);
//		} else {
//			drawTurning(g);
//		}

	}

	public void drawTurning(Graphics g) {
		// The visible color
		int color = (rotation > 0.5) ? color = palette[state]
				: palette[(state - 1 + maxState) % maxState];

		double beta = Math.atan(5);
		// The projected coordinates of the tile vertices, relative to the tile
		// size
		double sideBegin, sideEnd, tileBegin, tileEnd;

		sideBegin = (1 - Math.cos(rotation - beta)) / 2;
		sideEnd = (1 - Math.cos(rotation + beta)) / 2;
		if (rotation > 0.5) {
			tileBegin = sideEnd;
			tileEnd = 1 - sideBegin;
		} else {
			tileEnd = sideBegin;
			tileBegin = 1 - sideEnd;
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
			tileY = (int) (y + tileBegin * sizeY);
			tileSizeY = (int) tileRatio * sizeY;
			// The side
			sideY = (int) (y + sideBegin * sizeY);
			sideSizeY = (int) sideRatio * sizeY;
			break;
		case UP:
			tileY = (int) (y + (1 - tileEnd) * sizeY);
			tileSizeY = (int) tileRatio * sizeY;
			// The side
			sideY = (int) (y + (1 - sideEnd) * sizeY);
			sideSizeY = (int) sideRatio * sizeY;
			break;
		case RIGHT:
			tileX = (int) (x + tileBegin * sizeX);
			tileSizeX = (int) tileRatio * sizeX;
			// The side
			sideX = (int) (x + sideBegin * sizeX);
			sideSizeX = (int) sideRatio * sizeX;
			break;
		case LEFT:
			tileX = (int) (x + (1 - tileEnd) * sizeX);
			tileSizeX = (int) tileRatio * sizeX;
			// The side
			sideX = (int) (x + (1 - sideEnd) * sizeX);
			sideSizeX = (int) sideRatio * sizeX;
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
		if (moving != Tile.Moving.STILL) {
			rotation += deltaTime / MS_ROTATION_TIME;
			if (mustPropagate && rotation > 0.5) {
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
		board.propagate(moving, i, j);
	}

}

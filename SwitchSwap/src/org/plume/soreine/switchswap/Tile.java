package org.plume.soreine.switchswap;

import java.util.Random;

import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Input.TouchEvent;

import android.graphics.Color;

public class Tile {

	enum Moving {
		UP, DOWN, RIGHT, LEFT, STILL
	}

	int[] tileColor = { Color.rgb(199, 231, 136), Color.rgb(183, 136, 231),
			Color.rgb(136, 190, 231) };

	private int state;

	private Moving moving;

	private int x, y, sizeX, sizeY, maxState;

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

	// With random state
	public Tile(int x, int y, int sizeX, int sizeY, int maxState) {
		this(x, y, sizeX, sizeY, maxState, 0);
	}

	public void swap() {
		this.instantSwap();
		// TODO 
	}

	public void instantSwap() {
		this.state = (this.state + 1) % maxState;
	}

	public void draw(Graphics g) {
		g.drawRect(x, y, sizeX, sizeY, tileColor[state]);
	}

	public boolean touch(TouchEvent event) {
		return Utility.inBounds(event, x, y, sizeX, sizeY);
	}

	public void update(float deltaTime) {
		switch (moving) {
		case STILL:
			break;

		case DOWN:
			break;

		case LEFT:
			break;

		case RIGHT:
			break;

		case UP:
			break;

		default:
			break;
		}
	}

}

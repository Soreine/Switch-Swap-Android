package org.plume.soreine.switchswap;

import java.util.Random;

import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Input.TouchEvent;
import org.plume.soreine.framework.Queue.EmptyQueueException;

import android.graphics.Color;

public class Board {

	private Tile[] tiles;

	private int numberOfColors;

	private int[] palette;

	private int rows, columns;

	private int x, y, width, height, padding;

	private BoardQueue eventQueue;

	public Board(int x, int y, int width, int height, int rows, int columns,
			int numberOfColors) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.tiles = new Tile[rows * columns];

		double ratioGap = 0.16;

		this.padding = width / 20;
		int innerWidth = width - 2 * padding;
		int innerHeight = height - 2 * padding;

		// The size of the tiles
		int sizeX = (int) (innerWidth / (columns * (1 + ratioGap) - ratioGap));
		int sizeY = (int) (innerHeight / (rows * (1 + ratioGap) - ratioGap));

		// The gap between each tile
		int gapX = (int) (sizeX * ratioGap);
		int gapY = (int) (sizeY * ratioGap);

		// The distance from the beginning of a tile to the beginning of the
		// next
		int widthStep = sizeX + gapX;
		int heightStep = sizeY + gapY;

		this.rows = rows;
		this.columns = columns;

		int tileX = x + padding;
		int tileY = y + padding;
		// Initialize the array of tiles
		int location;

		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < columns; i++) {
				location = j * columns + i;
				tiles[location] = new Tile(tileX, tileY, sizeX, sizeY,
						numberOfColors);
				tiles[location].setLocationOnBoard(this, i, j);
				tileX += widthStep;
			}

			tileY += heightStep;
			tileX = x + padding;
		}

		// Generate the palette of colors
		this.numberOfColors = numberOfColors;
		this.palette = new int[numberOfColors];
		generatePalette();

		this.eventQueue = new BoardQueue(30);
	}

	private void generatePalette() {
		float[] hsv = new float[3];

		int[] hue = new int[numberOfColors];

		int numberOfSat = 2;
		float[] sat = { 0.6f, 0.8f };

		int numberOfValue = 2;
		float[] value = { 0.6f, 0.4f, 0.5f };

		int initialHue = (new Random()).nextInt(360);
		int hueStep = 360 / numberOfColors;
		for (int i = 0; i < numberOfColors; i++) {
			hue[i] = (initialHue + i * hueStep) % 360;
		}

		for (int i = 0; i < numberOfColors; i++) {
			hsv[0] = hue[i];
			hsv[1] = sat[i % numberOfSat];
			hsv[2] = value[i % numberOfValue];
			palette[i] = Color.HSVToColor(hsv);
		}

		palette[0] = Color.BLACK;
		palette[1] = Color.WHITE;

	}

	public void instantShuffle(int times) {
		Random rnd = new Random();
		for (int k = 0; k < times; k++) {
			instantSwap(rnd.nextInt(rows * columns));
		}
	}

	public void handleEvent(TouchEvent event) {
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {

				// Check if touched
				if (tiles[j * columns + i].touch(event)) {
					swap(i, j);
					return;
				}
			}
		}
	}

	public void update(float deltaTime) {
		for (Tile tile : tiles) {
			tile.update(deltaTime);
		}

		// Handle board event (like propagation of movement)
		try {
			BoardEvent event;
			while (true) {
				event = this.eventQueue.pull();
				event.handle(this);
			}

		} catch (EmptyQueueException e) {
		}
	}

	// Swap with propagation
	public void swap(int i, int j) {
		int coord = j * columns + i;
		// Propagate
		tiles[coord].swapAlone(Tile.Moving.UP);

		// Down
		if (j + 1 < rows)
			tiles[coord + columns].swap(Tile.Moving.DOWN);
		// Up
		if (j - 1 >= 0)
			tiles[coord - columns].swap(Tile.Moving.UP);
		// Right
		if (i + 1 < columns)
			tiles[coord + 1].swap(Tile.Moving.RIGHT);
		// Left
		if (i - 1 >= 0)
			tiles[coord - 1].swap(Tile.Moving.LEFT);
	}

	// Swap with propagation instantly
	public void instantSwap(int i, int j) {
		// Propagate
		// Vertically
		for (int k = 0; k < rows; k++) {
			tiles[k * columns + i].instantSwap();
		}
		// Horizontally
		for (int k = 0; k < columns; k++) {
			if (k != i) {
				tiles[j * columns + k].instantSwap();
			}
		}
	}

	public void instantSwap(int number) {
		int j = number / columns;
		int i = (number - j * columns);
		instantSwap(i, j);
	}

	public void propagateFrom(Tile.Moving move, int i, int j) {
		int nextI = i, nextJ = j;
		switch (move) {
		case DOWN:
			nextJ++;
			break;
		case LEFT:
			nextI--;
			break;
		case RIGHT:
			nextI++;
			break;
		case UP:
			nextJ--;
			break;
		default:
			return;
		}

		if (nextI < columns && nextJ < rows && nextI >= 0 && nextJ >= 0)
			this.eventQueue.push(new BoardEvent(move, nextI, nextJ));
	}

	public void propagate(Tile.Moving move, int i, int j) {
		tiles[j * columns + i].swap(move);
	}

	public void draw(Graphics g) {
		// Draw the board
		g.drawRoundRect(x, y, width, height, padding, padding,
				Color.rgb(200, 200, 200));
		// Draw the tiles
		for (Tile tile : tiles) {
			tile.draw(g);
		}
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int palette(int state) {
		assert (state < numberOfColors);
		return palette[state];
	}
}

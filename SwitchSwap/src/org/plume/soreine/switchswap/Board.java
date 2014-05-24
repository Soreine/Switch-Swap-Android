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

	private BoardQueue eventQueue;

	public Board(int x, int y, int width, int height, int rows, int columns,
			int numberOfColors) {
		this.tiles = new Tile[rows * columns];

		double ratioGap = 0.12;

		// The size of the tiles
		int sizeX = (int) (width / (columns * (1 + ratioGap) - ratioGap));
		int sizeY = (int) (height / (rows * (1 + ratioGap) - ratioGap));

		// The gap between each tile
		int gapX = (int) (sizeX * ratioGap);
		int gapY = (int) (sizeY * ratioGap);

		// The distance from the beginning of a tile to the beginning of the
		// next
		int widthStep = sizeX + gapX;
		int heightStep = sizeY + gapY;

		this.rows = rows;
		this.columns = columns;

		// Generate the palette of colors
		this.numberOfColors = numberOfColors;
		this.palette = new int[numberOfColors];
		generatePalette();

		// Initialize the array of tiles
		int location;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				location = i * columns + j;
				tiles[location] = new Tile(x + widthStep * j, y + heightStep
						* i, sizeX, sizeY, numberOfColors);
				tiles[location].setLocationOnBoard(this, i, j);
			}
		}

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

	}

	public void instantShuffle(int times) {
		Random rnd = new Random();
		for (int k = 0; k < times; k++) {
			instantSwap(rnd.nextInt(rows * columns));
		}
	}

	public void handleEvent(TouchEvent event) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				// Check if touched
				if (tiles[i * columns + j].touch(event)) {
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
		int coord = i * columns + j;
		// Propagate
		tiles[coord].swapAlone(Tile.Moving.UP);

		if (j + 1 < rows)
			tiles[coord + 1].swap(Tile.Moving.RIGHT);
		if (j - 1 >= 0)
			tiles[coord - 1].swap(Tile.Moving.LEFT);
		if (i + 1 < rows)
			tiles[coord + columns].swap(Tile.Moving.DOWN);
		if (i - 1 >= 0)
			tiles[coord - columns].swap(Tile.Moving.UP);
	}

	// Swap with propagation instantly
	public void instantSwap(int i, int j) {
		// Propagate
		for (int k = 0; k < rows; k++) {
			tiles[i * columns + k].instantSwap();
		}
		for (int k = 0; k < columns; k++) {
			if (k != i) {
				tiles[k * columns + j].instantSwap();
			}
		}
	}

	public void instantSwap(int number) {
		int i = number / columns;
		int j = (number - i * columns);
		instantSwap(i, j);
	}

	public void propagateFrom(Tile.Moving move, int i, int j) {
		int nextI = i, nextJ = j;
		switch (move) {
		case DOWN:
			nextI++;
			break;
		case LEFT:
			nextJ--;
			break;
		case RIGHT:
			nextJ++;
			break;
		case UP:
			nextI--;
			break;
		default:
			return;
		}

		if (nextI < columns && nextJ < rows && nextI >= 0 && nextJ >= 0)
			this.eventQueue.push(new BoardEvent(move, nextI, nextJ));
	}

	public void propagate(Tile.Moving move, int i, int j) {
		tiles[i * columns + j].swap(move);
	}

	public void draw(Graphics g) {
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

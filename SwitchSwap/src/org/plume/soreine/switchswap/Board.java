package org.plume.soreine.switchswap;

import java.util.ArrayList;
import java.util.Random;

import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Input.TouchEvent;

public class Board {

	private Tile[] tiles;

	private int rows, columns;

	public Board(int x, int y, int width, int height, int rows, int columns,
			int numberOfColors) {
		this.tiles = new Tile[rows * columns];

		// The size of the tiles
		int sizeX = (int) (width / (columns * 1.25 - 0.25));
		int sizeY = (int) (height / (rows * 1.25 - 0.25));

		// The gap between each tile
		int gapX = sizeX / 4;
		int gapY = sizeY / 4;

		// The distance from the beginning of a tile to the beginning of the
		// next
		int widthStep = sizeX + gapX;
		int heightStep = sizeY + gapY;

		this.rows = rows;
		this.columns = columns;

		Random rnd = new Random();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				tiles[i * columns + j] = new Tile(x + widthStep * j, y
						+ heightStep * i, sizeX, sizeY, numberOfColors);
			}
		}

		// for (int i = 0; i < rows; i++) {
		// for (int j = 0; j < columns; j++) {
		// if (rnd.nextBoolean())
		// propagate(i, j);
		// }
		// }

		instantShuffle(rows * columns);

	}

	public void instantShuffle(int times) {
		Random rnd = new Random();
		for (int k = 0; k < times; k++) {
			instantPropagate(rnd.nextInt(rows * columns));
		}
	}

	public void handleEvent(TouchEvent event) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				// Check if touched
				if (tiles[i * columns + j].touch(event)) {
					propagate(i, j);
					return;
				}
			}
		}
	}

	public void update(float deltaTime) {
		for (Tile tile : tiles) {
			tile.update(deltaTime);
		}

	}

	// Swap with propagation
	public void propagate(int i, int j, boolean instant) {
		// Propagate
		for (int k = 0; k < rows; k++) {
			if (instant)
				tiles[i * columns + k].instantSwap();
			else
				tiles[i * columns + k].swap();
		}
		for (int k = 0; k < columns; k++) {
			if (k != i) {
				if (instant)
					tiles[k * columns + j].instantSwap();
				else
					tiles[k * columns + j].swap();
			}
		}
	}

	public void propagate(int number, boolean instant) {
		int i = number / columns;
		int j = (number - i * columns);
		propagate(i, j, instant);
	}

	public void draw(Graphics g) {
		for (Tile tile : tiles) {
			tile.draw(g);
		}
	}

}

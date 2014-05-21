package org.plume.soreine.switchswap;

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

		int location;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				location = i * columns + j;
				tiles[location] = new Tile(x + widthStep * j, y + heightStep
						* i, sizeX, sizeY, numberOfColors);
				tiles[location].setLocationOnBoard(this, i, j);
			}
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

	public void propagate(Tile.Moving move, int i, int j) {
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
			tiles[nextI * columns + nextJ].swap(move);
	}

	public void draw(Graphics g) {
		for (Tile tile : tiles) {
			tile.draw(g);
		}
	}

}

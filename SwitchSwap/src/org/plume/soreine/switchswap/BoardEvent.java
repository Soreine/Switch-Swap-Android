package org.plume.soreine.switchswap;

import org.plume.soreine.switchswap.Tile.Moving;

public class BoardEvent {

	private Moving move;
	private int i, j;
	
	public BoardEvent(Moving move, int i, int j) {
		this.move = move;
		this.i = i;
		this.j = j;
	}

	protected void handle(Board board) {
		board.propagate(move, i, j);
	}

}

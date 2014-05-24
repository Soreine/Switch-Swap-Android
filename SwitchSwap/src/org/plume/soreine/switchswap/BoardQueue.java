package org.plume.soreine.switchswap;

import org.plume.soreine.framework.Queue;

public class BoardQueue implements Queue<BoardEvent> {

	private int head = 0;
	private int tail = 0;
	private final int maxSize;
	private final BoardEvent[] pending;

	public BoardQueue(int maxSize) {
		this.maxSize = maxSize;
		this.pending = new BoardEvent[maxSize];
	}

	@Override
	public void push(BoardEvent event) throws FullQueueException {
		if (isFull())
			throw new FullQueueException();
		pending[tail] = event;
		tail = (tail + 1) % maxSize;
	}

	@Override
	public BoardEvent pull() throws EmptyQueueException {
		if (isEmpty())
			throw new EmptyQueueException();

		BoardEvent event = pending[head];
		head = (head + 1) % maxSize;
		return event;
	}

	private boolean isEmpty() {
		return tail == head;
	}

	private boolean isFull() {
		return (tail + 1) % maxSize == head;
	}

}

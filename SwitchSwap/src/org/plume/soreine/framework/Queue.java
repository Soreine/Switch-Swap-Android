package org.plume.soreine.framework;

public interface Queue<T> {

	public class EmptyQueueException extends RuntimeException {
		private static final long serialVersionUID = 1L;		
	}
	
	public class FullQueueException extends RuntimeException {
		private static final long serialVersionUID = 2L;		
	}
	
	void push(T object) throws FullQueueException;
	
	T pull() throws EmptyQueueException;
		
}
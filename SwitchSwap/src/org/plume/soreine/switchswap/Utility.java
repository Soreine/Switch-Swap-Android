package org.plume.soreine.switchswap;

import org.plume.soreine.framework.Input.TouchEvent;

public class Utility {
	public static boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		return (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y
				+ height - 1);
	}
}

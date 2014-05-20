package org.plume.soreine.framework;

import org.plume.soreine.framework.Graphics.ImageFormat;

public interface Image {
	public int getWidth();

	public int getHeight();

	public ImageFormat getFormat();

	public void dispose();
}

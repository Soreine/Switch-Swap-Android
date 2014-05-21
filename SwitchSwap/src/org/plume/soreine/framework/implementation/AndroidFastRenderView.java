package org.plume.soreine.framework.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {

	private final static long NS_IN_MS = 1000000;
	private final static float MS_PER_UPDATE = (float) 50;
	private final static int MAXIMUM_UPDATES_WITHOUT_RENDERING = 5;

	AndroidGame game;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;

	public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();

	}

	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();

	}

	public void run() {
		Rect dstRect = new Rect();
		long previous = System.nanoTime();
		float lag = MS_PER_UPDATE; // milliseconds

		while (running) {
			if (!holder.getSurface().isValid())
				continue;

			long current = System.nanoTime();
			double elapsed = (current - previous) / NS_IN_MS;
			previous = current;

			lag += elapsed;

			int nbUpdates = 0;
			while (lag >= MS_PER_UPDATE
					&& nbUpdates <= MAXIMUM_UPDATES_WITHOUT_RENDERING) {
				game.getCurrentScreen().update(MS_PER_UPDATE);
				lag -= MS_PER_UPDATE;
				nbUpdates++;

			}

			game.getCurrentScreen().paint(lag * NS_IN_MS / MS_PER_UPDATE);

			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(framebuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);

		}
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				renderThread.join();
				break;
			} catch (InterruptedException e) {
				// retry
			}

		}
	}

}
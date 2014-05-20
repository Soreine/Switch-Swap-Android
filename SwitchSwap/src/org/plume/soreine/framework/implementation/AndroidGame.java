package org.plume.soreine.framework.implementation;

import org.plume.soreine.framework.Audio;
import org.plume.soreine.framework.FileIO;
import org.plume.soreine.framework.Game;
import org.plume.soreine.framework.Graphics;
import org.plume.soreine.framework.Input;
import org.plume.soreine.framework.Randomizer;
import org.plume.soreine.framework.Screen;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class AndroidGame extends FragmentActivity implements Game {
	public int resBig;
	public int resSmall;
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;
	Randomizer randomizer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

		// This works since API 1 but is deprecated
		@SuppressWarnings("deprecation")
		int frameBufferWidth = getWindowManager().getDefaultDisplay()
				.getWidth();
		@SuppressWarnings("deprecation")
		int frameBufferHeight = getWindowManager().getDefaultDisplay()
				.getHeight();
		
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);

		/*
		 * Needs API 13 or higher Point displaySize = new Point();
		 * getWindowManager().getDefaultDisplay().getSize(displaySize); float
		 * scaleX = (float) frameBufferWidth / displaySize.x; float scaleY =
		 * (float) frameBufferHeight / displaySize.y;
		 */

		// This works since API 1 but is deprecated
		@SuppressWarnings("deprecation")
		float scaleX = (float) frameBufferWidth
				/ getWindowManager().getDefaultDisplay().getWidth();
		@SuppressWarnings("deprecation")
		float scaleY = (float) frameBufferHeight
				/ getWindowManager().getDefaultDisplay().getHeight();

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		randomizer = new AndroidRandom();
		screen = getInitScreen();
		setContentView(renderView);

		// PowerManager powerManager = (PowerManager)
		// getSystemService(Context.POWER_SERVICE);
		// wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
		// "MyGame");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onResume() {
		super.onResume();
		// wakeLock.acquire();

		if (android.os.Build.VERSION.SDK_INT > 14) {
			setLowProfile();
		}

		screen.resume();
		renderView.resume();
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void setLowProfile() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LOW_PROFILE);

	}

	@Override
	public void onPause() {
		super.onPause();
		// wakeLock.release();
		renderView.pause();
		screen.pause();

		if (isFinishing())
			screen.dispose();
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen() {

		return screen;
	}
	
	@Override
	public Randomizer getRandomizer() {
		return randomizer;
	}
}
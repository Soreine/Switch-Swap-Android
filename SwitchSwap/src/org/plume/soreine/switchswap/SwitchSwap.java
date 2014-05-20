package org.plume.soreine.switchswap;
import org.plume.soreine.framework.Screen;
import org.plume.soreine.framework.implementation.AndroidGame;


public class SwitchSwap extends AndroidGame {

	@Override
	public Screen getInitScreen() {
		return new SplashLoadingScreen(this);
	}

}

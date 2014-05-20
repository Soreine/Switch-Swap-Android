package org.plume.soreine.framework.implementation;

import java.util.Random;

import org.plume.soreine.framework.Randomizer;

public class AndroidRandom implements Randomizer {

	private Random rnd;
	
	
	public AndroidRandom() {
		this.rnd = new Random();
	}
	
	@Override
	public void seed() {
		this.rnd = new Random();
	}

	@Override
	public int getInt() {
		return rnd.nextInt();
	}

	@Override
	public int getInt(int n) {
return rnd.nextInt(n);
	}

	@Override
	public float getFloat() {
	return rnd.nextFloat();
	}

	@Override
	public long getLong() {
		return rnd.nextLong();
	}

	@Override
	public boolean getBool() {
		return rnd.nextBoolean();
	}

}

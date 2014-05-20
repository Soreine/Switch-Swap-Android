package org.plume.soreine.framework;

public interface Randomizer {

	public void seed();
	
	public int getInt();
	
	public int getInt(int n);
	
	public float getFloat();
	
	public long getLong();
	
	public boolean getBool();
}

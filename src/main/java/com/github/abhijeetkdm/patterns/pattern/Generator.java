package com.github.abhijeetkdm.patterns.pattern;

public interface Generator {

	public double getMinRate();

	public double getMaxRate();

	public void calculateRate() ;
	
	public double getCurrentRate();
	
	public double getCurrentInterval();

	public boolean isCurrentCycle();

}

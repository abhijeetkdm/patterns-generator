package com.github.abhijeetkdm.patterns.pattern;

import java.sql.Timestamp;
import com.github.abhijeetkdm.patterns.configuration.GeneratorConfiguration;
import com.google.common.annotations.Beta;

/**
 * This class contains is used to generate Triangular Curve Pattern.
 * 
 */

public class TriangularCurveGenerator implements Generator{

	private double minimumRate;
	private double maximumRate;
	private double currentRate;
	private long intertimeDiff;
	private Timestamp intermediateStartTime = null;
	private Timestamp intermediateStopTime = null;
	private boolean CurveRise;
	private double rateChangeIntervalSeconds;
	private double currentIntervalSeconds;
	public double peakIntervalSeconds;
	private double RateChange;

	public TriangularCurveGenerator(GeneratorConfiguration config) {

		maximumRate = config.getGeneratorPPSMax();
		minimumRate = config.getGeneratorPPSMin();
		
		//Minimum rate can not be negative or 0.
		if (minimumRate <=0)
			minimumRate = 10.0;
		
		//start With minimum rate
		currentRate = minimumRate;
		//Increment to the mid point of range (mid point of min and max)
		RateChange = (maximumRate-minimumRate)/2;
		
		rateChangeIntervalSeconds = config.getMsgChangeIntervalSeconds() * 1000.0;
		currentIntervalSeconds = rateChangeIntervalSeconds;
		
		CurveRise = true;
		intertimeDiff = 0;
		intermediateStartTime = getCurrentTimeStamp();
	}
	
    /**
     * Returns Minimum rate. 
     */

	public double getMinRate() {
		return minimumRate;
	}
	
	/**
     * Returns Maximum rate. 
     */

	public double getMaxRate() {
		return maximumRate;
	}
	
	/**
     * Returns Current rate. 
     */

	public double getCurrentRate() {
		return currentRate;
	}
	
	/**
     * Returns Current Interval time (in sec). 
     */

	public double getCurrentInterval() {
		return currentIntervalSeconds;
	}
	
	@Beta
	/**
     * Updates Current rate.  
     */

	public void calculateRate() {
		if (CurveRise) {
			if (currentRate < maximumRate) {
				//Increase by rate change but not more than maximum
				currentRate = Math.min(maximumRate, 
								(currentRate + RateChange));
			} else {
				//Peak is reached. Time for downward slope 
				CurveRise = false;
			}
		} else {
			if (currentRate > minimumRate) {
				//Decrease by rate change but not less than minimum
				currentRate = Math.max(minimumRate,
									(currentRate - RateChange));
			} else {
				//Minimum is reached. Time for upward slope
				CurveRise = true;
			}
		}
	}
	
	@Beta
	/**
     * Check if current rate change interval is over.
     * If yes, update current rate  
     */

	public boolean isCurrentCycle() {
		intermediateStopTime = getCurrentTimeStamp();
		intertimeDiff = (intermediateStopTime.getTime() - intermediateStartTime
				.getTime());
		if (intertimeDiff < this.getCurrentInterval()) {
			return true;
		} else {
			intermediateStartTime = this.getCurrentTimeStamp();
			this.calculateRate();
			return false;
		}
	}

	/**
     * Returns Current Time. 
     */
	private Timestamp getCurrentTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

}

package com.github.abhijeetkdm.patterns.pattern;

import java.sql.Timestamp;

import com.github.abhijeetkdm.patterns.configuration.GeneratorConfiguration;
import com.google.common.annotations.Beta;

/**
 * This class contains is used to generate Gaussian Curve Pattern.
 * 
 */

public class GaussianCurveGenerator implements Generator{

	private double minimumRate;
	private double maximumRate;
	private double currentRate;
	private long intertimeDiff;
	Timestamp intermediateStartTime = null;
	Timestamp intermediateStopTime = null;
	private boolean bellCurveRise;
	private double msgRateIncrementPercent;
	private double normalIntervalSeconds;
	private double peakIntervalSeconds;
	private double currentIntervalSeconds;

	public GaussianCurveGenerator(GeneratorConfiguration config) {

		maximumRate = config.getGeneratorPPSMax();
		msgRateIncrementPercent = config.getMsgRateIncrementPercentage();
		normalIntervalSeconds = config.getMsgNormalIntervalSeconds();
		peakIntervalSeconds = config.getMsgPeakIntervalSeconds();
		minimumRate = maximumRate * (1 - config.getMsgDistributionPercent() / 100);
		// start with minimum rate
		currentRate = minimumRate;
		// start with normal interval secs
		currentIntervalSeconds = normalIntervalSeconds * 1000.0;
		bellCurveRise = true;
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
		if (bellCurveRise) {
			if (currentRate < maximumRate) {
				//Increase by message increment rate but not more than maximum
				currentRate = Math.min(maximumRate,
								currentRate * (1 + msgRateIncrementPercent / 100));
			} else {
				//Peak is reached. Time for downward slope
				bellCurveRise = false;
				// Maintain peak for peak interval seconds
				currentIntervalSeconds = peakIntervalSeconds * 1000.0;
			}
		} else {
			if (currentRate > minimumRate) {
				//Decrease by message increment rate but not less than minimum
				currentRate = Math.max(minimumRate,
								currentRate * (1 - msgRateIncrementPercent / 100));
				//Change current interval back to normal interval 
				currentIntervalSeconds = normalIntervalSeconds * 1000.0;
			} else {
				//Minimum is reached. Time for upward slope
				bellCurveRise = true;
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

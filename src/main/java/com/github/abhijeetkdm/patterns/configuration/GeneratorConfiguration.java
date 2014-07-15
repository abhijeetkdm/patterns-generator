
package com.github.abhijeetkdm.patterns.configuration;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class GeneratorConfiguration implements Serializable {

	@Value("${producer.pattern.type}")
	private int patternType;
	
	@Value("${producer.pps.max}")
	private double generatorPacketPerSecondMax;
	
	@Value("${producer.pps.min}")
	private double generatorPacketPerSecondMin;
	
	@Value("${producer.msgcount}")
	private int msgCount;

	@Value("${producer.gaussian.peak.interval.seconds}")
	private double msgPeakIntervalSeconds;

	@Value("${producer.gaussian.normal.interval.seconds}")
	private double msgNormalIntervalSeconds;
	
	@Value("${producer.triangular.change.interval.seconds}")
	private double msgChangeIntervalSeconds;

	@Value("${producer.gaussian.distribution.percentage}")
	private double msgDistributionPercent;

	@Value("${producer.gaussian.rate.increment.percentage}")
	private double msgRateIncrementPercentage;
	
	public int getPatternType(){
		return patternType;
	}

	public double getGeneratorPPSMax() {
		return generatorPacketPerSecondMax;
	}
	
	public double getGeneratorPPSMin() {
		return generatorPacketPerSecondMin;
	}
	
	public int getMsgCount() {
		return msgCount;
	}

	public double getMsgPeakIntervalSeconds() {
		return msgPeakIntervalSeconds;
	}

	public double getMsgNormalIntervalSeconds() {
		return msgNormalIntervalSeconds;
	}

	public double getMsgChangeIntervalSeconds() {
		return msgChangeIntervalSeconds;
	}
	
	public double getMsgDistributionPercent() {
		return msgDistributionPercent;
	}

	public double getMsgRateIncrementPercentage() {
		return msgRateIncrementPercentage;
	}

}
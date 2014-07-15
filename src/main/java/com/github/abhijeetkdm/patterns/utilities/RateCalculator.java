package com.github.abhijeetkdm.patterns.utilities;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains is used to keep track of packet rate and display it.
 * 
 */

public class RateCalculator extends TimerTask{
	
	public static int totalCount = 0;

	private static AtomicInteger count = new AtomicInteger();
	
	/**
     * Returns total count. 
     */
	
	public static int getTotalCount() {
		return totalCount;
	}
	
	/**
     * Returns count in last interval and reset. 
     */

	public static int getAndResetCount() {
		int cnt = count.getAndSet(0);
		totalCount += cnt;
		return cnt;
	}
	
	/**
     * Increment Count. 
     */

	public static void increment() {
		count.incrementAndGet();
	}
	
	/**
     * Display count. 
     */
    public void run() {
    	System.out.println("Total packet count: "+ getTotalCount()+" Count per second: " + getAndResetCount());    	
    }

}




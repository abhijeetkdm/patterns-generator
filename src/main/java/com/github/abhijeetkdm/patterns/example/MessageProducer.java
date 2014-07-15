package com.github.abhijeetkdm.patterns.example;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Log4jConfigurer;

import com.github.abhijeetkdm.patterns.configuration.SpringApplicationContextFactory;
import com.github.abhijeetkdm.patterns.configuration.GeneratorConfiguration;
import com.github.abhijeetkdm.patterns.pattern.GaussianCurveGenerator;
import com.github.abhijeetkdm.patterns.pattern.Generator;
import com.github.abhijeetkdm.patterns.pattern.TriangularCurveGenerator;
import com.github.abhijeetkdm.patterns.utilities.RateCalculator;
import com.google.common.util.concurrent.RateLimiter;


/**
 * This class is for Example, How to use pattern generator for in message producer 
 */

@Component
public class MessageProducer {

	private Generator curveGenerator;
	private RateLimiter rateLimiter;
	private Timer timer = new Timer();
	
	
	@Autowired
	private GeneratorConfiguration config;
	
	private int msgCount;
	
	public MessageProducer() {

	}
	
	public void configure(){
		
		//Using Gaussian generator as default for now 
		if (config.getPatternType() == 1)
			curveGenerator = new GaussianCurveGenerator(config);
	    else if (config.getPatternType() == 2)
			curveGenerator = new TriangularCurveGenerator(config);
		else 
			curveGenerator = new GaussianCurveGenerator(config);
		
		msgCount = config.getMsgCount();
		System.out.println("Selected pattern type - "+ config.getPatternType());
		rateLimiter = RateLimiter.create(curveGenerator.getCurrentRate());
	}
	
	public void produce(){
		//start rate calculator and display rate every 1 sec.
		timer.schedule(new RateCalculator(), 0, 1000);
		int count =0;
		while (true && count < msgCount) {
			while (curveGenerator.isCurrentCycle() && count < msgCount) {
				/*
				 Code to produce packets  
				 */
				rateLimiter.acquire();
				// increment rate counter
				RateCalculator.increment();
				count++;
			}
 		rateLimiter.setRate(curveGenerator.getCurrentRate());
 		System.out.println("Changing rate to "
				+ curveGenerator.getCurrentRate());
		}
	}
	
	public static void main(String[] args) {
		try {
			ApplicationContext ctxt = SpringApplicationContextFactory
					.newInstance();

			MessageProducer producer = ctxt.getBean(MessageProducer.class);
			producer.configure();
			producer.produce();
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					System.out.println("Running Shutdown Hook");
				}
			});
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}

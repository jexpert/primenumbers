package uk.co.jexpert.primenumbers.impl;

import java.io.Serializable;
import java.util.stream.LongStream;

import uk.co.jexpert.primenumbers.api.PrimeNumberChecker;
import uk.co.jexpert.primenumbers.api.PrimeNumberGenerator;

/**
 * Simple implementation which checks if the number is prime by testing all longs up until 
 * the square root of the number 
 * @author Mikhail
 *
 */
public class PrimeNumberCheckerBasicImpl implements PrimeNumberChecker, Serializable {
	private static final long serialVersionUID = 1L;

	PrimeNumberGenerator generator;
	
	
	public PrimeNumberCheckerBasicImpl() {
		
	}
	
	public PrimeNumberCheckerBasicImpl(PrimeNumberGenerator generator) {
		super();
		this.generator = generator;
	}


	@Override
	public boolean isPrimeNumber(long number) {
		long v = Math.abs(number);
		long r = (long)Math.sqrt(v);
		if(generator==null)		//	no generator - just try all numbers 
			return !LongStream.range(2, r+1).parallel().anyMatch(l -> v%l==0);
		else					//	try all prime numbers from generator
			return !generator.stream(r).parallel().anyMatch(l -> v%l==0);
	}

}

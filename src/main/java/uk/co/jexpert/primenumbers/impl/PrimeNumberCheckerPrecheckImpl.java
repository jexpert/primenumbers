package uk.co.jexpert.primenumbers.impl;

import java.io.Serializable;
import java.util.Arrays;

import com.google.common.io.CharStreams;

import uk.co.jexpert.primenumbers.api.PrimeNumberChecker;

/**
 * This is not really an implementation of the check - this implementation returns
 * FALSE for all simple patterns (e.g. even, last digit 5, divide by 3, etc.) where just looking at the number you can tell that it is not a prime
 * number. This is not the full implementation, but just an optimisation example.  
 * It returns TRUE if the number MAY be a prime number, but does not guarantee that.  
 * @author Mikhail
 */
public class PrimeNumberCheckerPrecheckImpl implements PrimeNumberChecker, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isPrimeNumber(long number) {
		String s = Long.toString(number);
		int l = s.length();
		if(l<2)
			return true;
		char c = s.charAt(s.length()-1);
		if(c=='2' || c=='4' || c=='5' || c=='6' || c=='8' || c=='0') 
			return false;
		long sum = s.chars().map(ch -> ch-(int)'0').sum();
		if(sum%3==0)
			return false;
		return true;
	}

}

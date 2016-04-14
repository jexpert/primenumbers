package uk.co.jexpert.primenumbers.api;

/**
 * Simple interface to check if the number is a prime number
 * @author Mikhail
 */
public interface PrimeNumberChecker {
	/**
	 * Returns TRUE if the number is a prime number, otherwise returns false 
	 * @param number number to check
	 * @return TRUE if it's a prime number, otherwise false
	 */
	boolean isPrimeNumber(long number);
}

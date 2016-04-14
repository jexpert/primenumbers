package uk.co.jexpert.primenumbers.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import uk.co.jexpert.primenumbers.api.PrimeNumberGenerator;

/**
 * This is a trivial implementation of the sieve of Eratosthenes algorithm which uses a bitset internally
 * and therefore the maximum number it can generate is limited to INT maximum value
 * Obviously it would make sense to cache bitset and just generate streams - not for thsi test.
 * @author Mikhail
  */
public class PrimeNumberGeneratorBitsetImpl implements PrimeNumberGenerator, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public LongStream stream(long upto) throws IllegalArgumentException {

		//	prepare bitset where the index is a number and it's prime if FALSE in the bitset
		BitSet bitset = generateBitSet(upto);

		//	create stream to return only indexes where a bit is FALSE - prime numbers
		LongStream res = LongStream.range(2, upto).filter(l -> !bitset.get((int)l));
		
		return res;
	}
	
	/**
	 * Generates and fills a bitset for upto size. Each bit is FALSE if its index is prime
	 * @param upto max size of the bitset
	 * @return created and filled bitset
	 * @throws IllegalArgumentException problems with size
	 */
	protected BitSet generateBitSet(long upto) throws IllegalArgumentException {
		//	some checks
		System.out.println("Generating for upto="+upto);
		
		if(upto<2)
			throw new IllegalArgumentException("Generator maximum parameter must be at least 2 ");
		if(upto>Integer.MAX_VALUE)
			throw new IllegalArgumentException("Bitset implementation can only calculte prime numbers up to "+Integer.MAX_VALUE);
		
		BitSet res = new BitSet((int)upto);

		//	could not make it recursive - stack overflow, so it is like this
		int d = fillBitSet(res, 2, (int)upto);
		while(d>0)
			d = fillBitSet(res, d+1, (int)upto);
		
		return res;
	}

	/**
	 * Finds a next prime number >= from and fills the bitset setting bits to TRUE for multipliers of this
	 * prime number. Returns the prime number it found or -1 if none.
	 * @param bitset bitset to fill
	 * @param from index to start looking for a next prime
	 * @param maxValue max (size) of the bitset
	 * @return next found prime or -1 if none 
	 */
	protected int fillBitSet(BitSet bitset, int from, int maxValue) {
		//	find the first prime after from value and up until half of the maxValue. there is no point to go beyond 1/2
		int div = -1;
		for(int i=from; i<=maxValue/2; i++)
			if(!bitset.get(i)) {
				div = i;
				break;
			}
		//	set bit to true for all multiplied values of the prime found
		if(div>0) {
			int cur = div+div;
			while(cur<maxValue && cur>0) {
				bitset.set(cur);
				cur+=div;
			}
			return div;
		}else
			return -1;
	}

}



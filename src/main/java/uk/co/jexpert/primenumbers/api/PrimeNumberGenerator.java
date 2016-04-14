package uk.co.jexpert.primenumbers.api;

import java.util.stream.LongStream;

public interface PrimeNumberGenerator {

	LongStream stream(long upto) throws IllegalArgumentException;
}

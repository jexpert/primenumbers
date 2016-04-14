package uk.co.jexpert.primenumbers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.Test;

import uk.co.jexpert.primenumbers.api.PrimeNumberChecker;
import uk.co.jexpert.primenumbers.api.PrimeNumberGenerator;
import uk.co.jexpert.primenumbers.impl.PrimeNumberCheckerBasicImpl;
import uk.co.jexpert.primenumbers.impl.PrimeNumberCheckerPrecheckImpl;
import uk.co.jexpert.primenumbers.impl.PrimeNumberGeneratorBitsetImpl;

public class TestPrimeChecker {

	static long MAX = 1000;
	
	@Test
	public void test() {

		final PrimeNumberChecker preCheck = new PrimeNumberCheckerPrecheckImpl();
		final PrimeNumberChecker check = new PrimeNumberCheckerBasicImpl();

		double nt = System.nanoTime();
/*		
		List<Long> res = LongStream.range(2, 1000).boxed()
			.filter(l -> preCheck.isPrimeNumber(l))
			.filter(l -> check.isPrimeNumber(l))
			.collect(Collectors.toList());
*/		
		
		List<Long> res2 = LongStream.range(2, MAX).parallel()
				.filter(l -> preCheck.isPrimeNumber(l))
				.filter(l -> check.isPrimeNumber(l))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		double tt = (System.nanoTime()-nt)/1000000.0;

//		res2.stream().forEach(l -> System.out.println(l));

		
//		String joined = Arrays.asList(res).stream()
//			    .map(Object::toString)
//			    .collect(Collectors.joining(", "));

//		System.out.println(joined);
		System.out.println("Took: "+tt+"ms. for "+res2.size()+" items found out of "+MAX);
		
		assertEquals(168, res2.size());
//			.forEach(l -> System.out.println(l));
		
//			.collect(Collectors.toList());
		
		
//		System.out.println(res);
//		fail("Not yet implemented");
	}

//	@Test
	public void testFiller() {
		ArrayList<Long> res = new ArrayList<Long>();
		
		double nt = System.nanoTime();
		
		LongStream str = LongStream.range(2, 100000);
		
		while(true) {
			OptionalLong ol = str.findFirst();
			if(!ol.isPresent())
				break;
			long f = ol.getAsLong();
			res.add(f);
			long[] str2 = str
					.filter(l -> l%f!=0)
					.toArray();
			str = LongStream.of(str2);
		}

		double tt = (System.nanoTime()-nt)/1000000.0;
		
		res.stream().forEach(l -> System.out.println(l));
		System.out.println("Took (new alg): "+tt+"ms. for "+res.size()+" items found");
		
	}


	@Test
	public void testBitSet() {
		double nt = System.nanoTime();
		
		PrimeNumberGenerator gen = new PrimeNumberGeneratorBitsetImpl();

		LongStream str = gen.stream(MAX);
//		Collection<Long> res = gen.generate(MAX);
		
		double tt = (System.nanoTime()-nt)/1000000.0;

		long cnt = str.count();
		
//		str.parallel().forEach(l -> System.out.println(l));
		
		System.out.println("Took (BitSet): "+tt+"ms. for "+cnt+" items found out of "+MAX);
		assertEquals(168, cnt);
	}
}

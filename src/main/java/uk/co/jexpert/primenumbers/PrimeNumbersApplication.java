package uk.co.jexpert.primenumbers;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.jexpert.primenumbers.resources.PrimeNumberCheckEndpoint;
import uk.co.jexpert.primenumbers.resources.PrimeNumbersEndpoint;
import uk.co.jexpert.primenumbers.api.PrimeNumberGenerator;
import uk.co.jexpert.primenumbers.impl.PrimeNumberGeneratorBitsetImpl;

public class PrimeNumbersApplication extends Application<PrimeNumbersConfig> {
    public static void main(String[] args) throws Exception {
        new PrimeNumbersApplication().run(args);
    }

    @Override
    public String getName() {
        return "prime-numbers";
    }

    @Override
    public void initialize(Bootstrap<PrimeNumbersConfig> bootstrap) {
    }

    @Override
    public void run(PrimeNumbersConfig configuration, Environment environment) {
    	try {

    		//	using config to create generator
	    	String clsn = configuration.getGeneratorClass();
	    	Class cls = PrimeNumberGeneratorBitsetImpl.class; 	//	default
	    	
	    	if(clsn!=null)
	    		cls = Class.forName(clsn);
	    	final PrimeNumberGenerator gen = (PrimeNumberGenerator)cls.newInstance();
	        final PrimeNumbersEndpoint prime = new PrimeNumbersEndpoint (
	        		gen
	            );
	        final PrimeNumberCheckEndpoint check = new PrimeNumberCheckEndpoint(
	        		gen
	            );

	        System.out.println("Using generator: "+gen.getClass());
	        
	        //	register endpoint
	        environment.jersey().register(prime);
	        environment.jersey().register(check);
	        
    	}catch(Exception e) {
    		throw new RuntimeException("Invalid configuration",e);
    	}
    }

}

package uk.co.jexpert.primenumbers.resources;

import uk.co.jexpert.primenumbers.api.PrimeNumberChecker;
import uk.co.jexpert.primenumbers.api.PrimeNumberGenerator;
import uk.co.jexpert.primenumbers.impl.PrimeNumberCheckerBasicImpl;
import uk.co.jexpert.primenumbers.impl.PrimeNumberCheckerPrecheckImpl;

import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.stream.LongStream;

@Path("/primecheck")
@Produces(MediaType.APPLICATION_JSON)
public class PrimeNumberCheckEndpoint {
    private final PrimeNumberGenerator generator;

    public PrimeNumberCheckEndpoint(PrimeNumberGenerator generator) {
        this.generator = generator;
    }

    @GET
    @Timed
    public CheckResult checkPrime(@QueryParam("number") String numberValue) {
    	final String param = numberValue;
    	
        long val = 0;
        Throwable error = null;
        
        //	parse parameter
        if(param!=null) {
        	try {
        		BigDecimal dec = new BigDecimal(param);
        		val = dec.longValueExact();		//	must be long
        	}catch(NumberFormatException e) {	//	invalid format
        		error = new IllegalArgumentException("parameter must be numeric and long");
        	}catch(ArithmeticException e) {		//	not exactly long
        		error = new IllegalArgumentException("parameter must be long");
        	}
        }else
    		error = new IllegalArgumentException("parameter must be long and provided");

        if(error!=null)
        	return new CheckResult(val, false, error);

        //	precheck on simple patterns
        PrimeNumberChecker checker = new PrimeNumberCheckerPrecheckImpl();
        if(!checker.isPrimeNumber(val))
        	return new CheckResult(val, false, null);
        
        checker = new PrimeNumberCheckerBasicImpl(generator);
        try {
        	return new CheckResult(val, checker.isPrimeNumber(val), null);
        }catch(Exception e) {
        	return new CheckResult(val, false, e);
        }
    }
}
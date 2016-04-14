package uk.co.jexpert.primenumbers.resources;

import uk.co.jexpert.primenumbers.api.PrimeNumberGenerator;
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

@Path("/primenumbers")
@Produces(MediaType.APPLICATION_JSON)
public class PrimeNumbersEndpoint {
    private final PrimeNumberGenerator generator;
    private final ObjectMapper objectMapper;

    public PrimeNumbersEndpoint(PrimeNumberGenerator generator) {
        this.generator = generator;
        this.objectMapper = new ObjectMapper(); 
    }

    @GET
    @Timed
    public Response generatePrimes(@QueryParam("max") Optional<String> maxValue) {
    	final String maxParam = maxValue.orNull();
    	
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

            	Throwable error = null;			//	hold processing error
                long max = Integer.MAX_VALUE;	//	maximum value to generate for

                //	parse parameter or use MAX integer if not present
                if(maxParam!=null) {
                	try {
                		BigDecimal dec = new BigDecimal(maxParam);
                		max = dec.longValueExact();		//	must be long
                	}catch(NumberFormatException e) {	//	invalid format
                		error = new IllegalArgumentException("max parameter must be numeric and long");
                	}catch(ArithmeticException e) {		//	not exactly long
                		error = new IllegalArgumentException("max parameter must be long");
                	}
                }
                
                
                //	start marshalling result: echo max value, array or primes and an error
                JsonGenerator jg = objectMapper.getFactory().createGenerator( os, JsonEncoding.UTF8 );

                jg.writeStartObject();
                jg.writeFieldName( "max" );
                jg.writeNumber(max);
                jg.writeFieldName( "values" );

                //	write primes
                jg.writeStartArray();
                try {
                	if(error==null) {	//	do it if no  error with parameter
	                	LongStream str =  generator.stream(max);		// generate
		                str.forEach(l -> 
							{ 
								try {
									jg.writeNumber(l);
								}catch(Exception e) {
									throw new ArithmeticException("Failed to write results");
								}
							}
						);
                	}
                }catch(IllegalArgumentException e3) {	//	failed to create stream
                	error = e3;
                }catch(ArithmeticException e2) {		//	failed to write, just use this runtime
                	error = e2.getCause();
                }finally{
                	jg.writeEndArray();
                }
                
                //	write error if any
                if(error!=null) {
	                jg.writeFieldName( "error" );
	                writeError(error,jg);
                }
                
                jg.writeEndObject();
                
                jg.flush();
                jg.close();
                
            }
        };
        
    	return Response.ok(stream).build();
    }
    
    protected void writeError(Throwable error, JsonGenerator jg) throws IOException {
        jg.writeStartObject();
        jg.writeFieldName( "className" );
        jg.writeString(error.getClass().getName());
        jg.writeFieldName( "message" );
        jg.writeString(error.getMessage());
        jg.writeEndObject();
    }
}
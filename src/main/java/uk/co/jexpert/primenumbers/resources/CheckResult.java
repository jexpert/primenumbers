package uk.co.jexpert.primenumbers.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CheckResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long number;
    private boolean prime;
    private Throwable error;
    
    public CheckResult() {
        // Jackson deserialization
    }

    public CheckResult(long number, boolean prime, Throwable error) {
		super();
		this.number = number;
		this.prime = prime;
		this.error = error;
	}

	@JsonProperty
	public long getNumber() {
		return number;
	}

	@JsonProperty
	public boolean isPrime() {
		return prime;
	}

	@JsonProperty
	public String getErrorClass() {
		return (error==null)?null:error.getClass().getName();
	}

	@JsonProperty
	public String getErrorMessage() {
		return (error==null)?null:error.getMessage();
	}
}
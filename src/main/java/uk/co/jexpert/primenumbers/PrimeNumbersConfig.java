package uk.co.jexpert.primenumbers;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class PrimeNumbersConfig extends Configuration {
    @NotEmpty
    private String generatorClass;

    @JsonProperty
    public String getGeneratorClass() {
        return generatorClass;
    }

    @JsonProperty
    public void setGeneratorClass(String className) {
        this.generatorClass= className;
    }

}
package com.technologies.mobile.free_exchange_admin.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 26.11.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CanPublish {

    @JsonProperty("can")
    boolean can;

    @JsonProperty("errors")
    String[] errors;

    @JsonProperty("warnings")
    String[] warnings;

    public boolean isCan() {
        return can;
    }

    public String[] getErrors() {
        return errors;
    }

    public String[] getWarnings() {
        return warnings;
    }
}

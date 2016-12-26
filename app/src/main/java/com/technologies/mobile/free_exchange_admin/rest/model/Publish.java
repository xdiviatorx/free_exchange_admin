package com.technologies.mobile.free_exchange_admin.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 26.11.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Publish {

    @JsonProperty("publish")
    Check check;

    public Check getCheck() {
        return check;
    }
}

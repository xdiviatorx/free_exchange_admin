package com.technologies.mobile.free_exchange_admin.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 17.11.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleResponse {

    @JsonProperty("RESPONSE")
    String status;

    public String getStatus() {
        return status;
    }
}

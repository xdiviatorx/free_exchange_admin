package com.technologies.mobile.free_exchange_admin.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 18.08.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetOffersResponse {

    @JsonProperty("RESPONSE")
    OffersArray offersArray;

    public OffersArray getOffersArray() {
        return offersArray;
    }
}

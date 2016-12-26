package com.technologies.mobile.free_exchange_admin.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 17.08.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OffersArray {

    @JsonProperty("count")
    int count;

    @JsonProperty("offers")
    Offer[] offers;

    public int getCount() {
        return count;
    }

    public Offer[] getOffers() {
        return offers;
    }
}

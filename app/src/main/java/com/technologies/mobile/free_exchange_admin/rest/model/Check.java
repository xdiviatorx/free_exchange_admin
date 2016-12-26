package com.technologies.mobile.free_exchange_admin.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 26.11.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Check {

    @JsonProperty("vk")
    CanPublish canPublishVk;

    @JsonProperty("site")
    CanPublish canPublishSite;

    @JsonProperty("telegramm")
    CanPublish canPublishTelegramm;

    public CanPublish getCanPublishVk() {
        return canPublishVk;
    }

    public CanPublish getCanPublishSite() {
        return canPublishSite;
    }

    public CanPublish getCanPublishTelegramm() {
        return canPublishTelegramm;
    }
}

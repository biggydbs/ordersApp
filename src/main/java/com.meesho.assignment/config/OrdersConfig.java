package com.meesho.assignment.config;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;

import javax.validation.Valid;

/**
 * Created by hitesh.jain1 on 30/06/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersConfig extends Configuration {

    @Valid
    @Getter
    @JsonProperty
    private SQLConfig sqlConfig = new SQLConfig();

    @Valid
    @Getter
    @JsonProperty
    private KafkaConfig kafkaConfig = new KafkaConfig();
}

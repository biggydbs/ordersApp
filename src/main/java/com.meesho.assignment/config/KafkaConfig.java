package com.meesho.assignment.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * Created by hitesh.jain1 on 03/07/18.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaConfig {
    @Valid
    @Getter
    private String brokerList;

    @Valid
    @Getter
    private String invoiceTopic;

    @Valid
    @Getter
    private String emailTopic;

    @Valid
    @Getter
    private String batchSize;

    @Valid
    @Getter
    private String retryCount;

    @Valid
    @Getter
    private String inFlightRequest;

    @Valid
    @Getter
    private String requestTimeout;

    @Valid
    @Getter
    private String retryBackOffTime;

    @Valid
    @Getter
    private String ackConfig;

    @Valid
    @Getter
    private String lingerTime;
}

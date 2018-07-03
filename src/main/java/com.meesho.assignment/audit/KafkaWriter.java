package com.meesho.assignment.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.assignment.config.KafkaConfig;
import com.meesho.assignment.dao.EventData;
import com.meesho.assignment.dao.EventMetadata;
import io.dropwizard.lifecycle.Managed;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by hitesh.jain1 on 03/07/18.
 */

@Slf4j
@Data
@Builder
public class KafkaWriter implements Managed {
    protected KafkaProducer producer;
    protected KafkaConfig kafkaConfig;
    protected ObjectMapper mapper;

    public void pushAudit(Map <String, Object> audit, String topic) throws JsonProcessingException {
        ProducerRecord <String, String> message = null;
        try {
            final EventMetadata metadata = new EventMetadata();
            final EventData data = new EventData();
            data.setId(UUID.randomUUID().toString());
            metadata.setSchema(topic);
            metadata.setSender("ordersApp");
            metadata.setTimestamp(System.currentTimeMillis());
            data.setMetadata(metadata);
            data.setData(audit);
            message = new ProducerRecord <String, String>(topic, mapper.writeValueAsString(data));
            log.info("Successfully created record");
        } catch (Exception e) {
            log.info("Error while creating record");
        }
        producer.send(message);
    }

    @Override
    public void start() throws Exception {
        Properties props = new Properties();
        try {
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBrokerList());

            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class.getName());

            props.put(ProducerConfig.ACKS_CONFIG, kafkaConfig.getAckConfig());

            props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfig.getBatchSize());

            props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaConfig.getLingerTime());

            props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaConfig.getInFlightRequest());

            props.put(ProducerConfig.RETRIES_CONFIG, kafkaConfig.getRetryCount());

            props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaConfig.getRequestTimeout());

            props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, kafkaConfig.getRetryBackOffTime());

            producer = new KafkaProducer <>(props);

        } catch (Exception e) {
            log.info("Unable to initialize kafka : {}", e.fillInStackTrace());
            throw new Exception("Unable to initialize kafka");
        }
    }

    @Override
    public void stop() throws Exception {
        getProducer().flush();
    }
}

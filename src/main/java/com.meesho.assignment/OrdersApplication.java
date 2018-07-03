package com.meesho.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.assignment.audit.KafkaWriter;
import com.meesho.assignment.config.OrdersConfig;
import com.meesho.assignment.dao.SampleDao;
import com.meesho.assignment.resource.OrdersResource;
import com.meesho.assignment.service.OrdersService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


/**
 * Created by hitesh.jain1 on 30/06/18.
 */
public class OrdersApplication extends Application <OrdersConfig> {

    public static void main(String[] args) throws Exception {
        new OrdersApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap <OrdersConfig> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(OrdersConfig configuration, Environment environment) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final SampleDao sampleDao = SampleDao.builder().sqlConfig(configuration.getSqlConfig()).build();

        KafkaWriter kafkaWriter = KafkaWriter.builder().mapper(mapper).kafkaConfig(configuration.getKafkaConfig()).build();

        OrdersService ordersService = new OrdersService(sampleDao, kafkaWriter,
                configuration.getKafkaConfig().getEmailTopic(), configuration.getKafkaConfig().getInvoiceTopic(),
                mapper);

        OrdersResource ordersResource = new OrdersResource(ordersService);

        environment.jersey().register(ordersResource);
    }
}

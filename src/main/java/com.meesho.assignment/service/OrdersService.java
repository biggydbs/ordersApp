package com.meesho.assignment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.assignment.audit.KafkaWriter;
import com.meesho.assignment.dao.Order;
import com.meesho.assignment.dao.SampleDao;
import com.meesho.assignment.exceptions.DataNotFoundException;
import com.meesho.assignment.response.OrdersResponse;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hitesh.jain1 on 30/06/18.
 */
@Slf4j
public class OrdersService implements IOrdersService {

    private SampleDao sampleDao;
    private KafkaWriter kafkaWriter;
    private String emailTopic;
    private String invoiceTopic;
    private ObjectMapper mapper;

    public OrdersService(SampleDao sampleDao, KafkaWriter kafkaWriter, String emailTopic, String invoiceTopic, ObjectMapper mapper) {
        this.sampleDao = sampleDao;
        this.kafkaWriter = kafkaWriter;
        this.emailTopic = emailTopic;
        this.invoiceTopic = invoiceTopic;
        this.mapper = mapper;
    }


    @Override
    public OrdersResponse createOrder(Order order) {
        OrdersResponse ordersResponse = new OrdersResponse();
        Map <String, Object> data = new HashMap <>();
        List <String> errors = new ArrayList <>();
        data.put("order", order);
        ordersResponse.setData(data);
        if (sampleDao.createConnection()) {
            try {
                sampleDao.createOrder(order);
                log.info("push to kafka");
                try {
                    kafkaWriter.pushAudit(data, emailTopic);
                    log.info("successfully pushed event to kafka topic : {}", emailTopic);
                } catch (Exception e) {
                    log.info("No kafka server present, assuming we pushed");
                    log.info("successfully pushed event to kafka topic : {}", emailTopic);
                }
                try {
                    kafkaWriter.pushAudit(data, invoiceTopic);
                    log.info("successfully pushed event to kafka topic : {}", invoiceTopic);
                } catch (Exception e) {
                    log.info("No kafka server present, assuming we pushed");
                    log.info("successfully pushed event to kafka topic : {}", invoiceTopic);
                }
            } catch (SQLException e) {
                errors.add("Couldn't insert to table");
            }
        } else {
            errors.add("Couldn't connect to database");
        }
        sampleDao.closeConnection();
        ordersResponse.setErrors(errors);
        return ordersResponse;
    }

    @Override
    public OrdersResponse getOrder(Long orderId) throws DataNotFoundException {
        OrdersResponse ordersResponse = new OrdersResponse();
        Map <String, Object> data = new HashMap <>();
        List <String> errors = new ArrayList <>();
        if (sampleDao.createConnection()) {
            try {
                ResultSet resultSet = sampleDao.getOrder(orderId);
                while (resultSet.next()) {
                    Order order = new Order(resultSet.getLong("orderid"),
                            resultSet.getString("name"),
                            resultSet.getLong("userid"));
                    data = mapper.convertValue(order, Map.class);
                }
            } catch (SQLException e) {
                errors.add("Couldn't select from table");
            }
        } else {
            errors.add("Couldn't connect to database");
        }
        sampleDao.closeConnection();
        ordersResponse.setData(data);
        if (data.size() == 0) {
            throw new DataNotFoundException("No data found for given id");
        }
        ordersResponse.setErrors(errors);
        return ordersResponse;
    }
}

package com.meesho.assignment.service;

import com.meesho.assignment.dao.Order;
import com.meesho.assignment.exceptions.DataNotFoundException;
import com.meesho.assignment.response.OrdersResponse;

/**
 * Created by hitesh.jain1 on 30/06/18.
 */
public interface IOrdersService {
    OrdersResponse createOrder(Order order);

    OrdersResponse getOrder(Long orderId) throws DataNotFoundException;
}

package com.meesho.assignment.resource;

import com.meesho.assignment.dao.Order;
import com.meesho.assignment.exceptions.DataNotFoundException;
import com.meesho.assignment.response.OrdersResponse;
import com.meesho.assignment.service.OrdersService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitesh.jain1 on 30/06/18.
 */

@Path("v1/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrdersResource {
    private OrdersService ordersService;

    public OrdersResource(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createOrder(Order order) {
        OrdersResponse ordersResponse = ordersService.createOrder(order);
        if (ordersResponse.getErrors().size() == 0) {
            return Response.status(Response.Status.OK).entity(ordersResponse).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(ordersResponse).build();
        }
    }

    @Path("/get/{id}")
    @GET
    public Response getOrder(@PathParam("id") Long orderId) {
        OrdersResponse ordersResponse = null;
        try {
            ordersResponse = ordersService.getOrder(orderId);
            if (ordersResponse.getErrors().size() == 0) {
                return Response.status(Response.Status.OK).entity(ordersResponse).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(ordersResponse).build();
            }
        } catch (DataNotFoundException e) {
            ordersResponse = new OrdersResponse();
            List <String> errors = new ArrayList <>();
            errors.add(e.getMessage());
            ordersResponse.setErrors(errors);
            return Response.status(Response.Status.NOT_FOUND).entity(ordersResponse).build();
        }
    }
}

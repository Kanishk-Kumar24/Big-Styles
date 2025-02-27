package com.ecom.apis.service.order;

import com.ecom.apis.entity.Orders;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;

import java.util.List;

public interface OrderService {
    String addOrder(String createTime, String id) throws NotFoundException;

    List<Orders> allOrders();

    List<Orders> userOrders() throws NotFoundException;
}

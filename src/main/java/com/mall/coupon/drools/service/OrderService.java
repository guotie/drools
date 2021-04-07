package com.mall.coupon.drools.service;

import com.mall.coupon.drools.model.OrderItem;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OrderService {
    private static Map<Integer, List<OrderItem>> orderDetails;

    public static void addOrderDetail(Integer orderId, List<OrderItem> items) {
        if (null == orderDetails) {
            orderDetails = new ConcurrentHashMap<>();
        }
        orderDetails.put(orderId, items);
    }

    public static List<OrderItem> getOrderItems(Integer orderId) {
        if (null == orderId) {
            log.warn("param orderId is null");
            return new ArrayList<>();
        }
        if (null == orderDetails) {
            return new ArrayList<>();
        }

        List<OrderItem> items = orderDetails.get(orderId);
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }
}

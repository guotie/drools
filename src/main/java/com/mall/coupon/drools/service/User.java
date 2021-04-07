package com.mall.coupon.drools.service;

public class User {
    public static String userIdentify(String userType, String userId) {
        return userType + "-" + userId;
    }
}

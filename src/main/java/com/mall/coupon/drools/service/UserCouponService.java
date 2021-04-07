package com.mall.coupon.drools.service;

// 用户的coupon列表

import com.mall.coupon.drools.model.Coupon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserCouponService {
    private static Map<String, Coupon> userCoupons;

    public static void addUserCoupon(String code, Coupon coupon) {
        if (userCoupons == null) {
            userCoupons = new ConcurrentHashMap<>();
        }
        userCoupons.put(code, coupon);
    }

    public static Coupon getUserCouponByCode(String code) {
        if (userCoupons == null) {
            return null;
        }
        return userCoupons.get(code);
    }
}

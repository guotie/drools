package com.mall.coupon.drools.service;

// 用户是否可以参与某个优惠券

import com.mall.coupon.drools.model.Coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private static Map<Integer, List<String>> couponUsers = null;

    public static void addCouponUser(Integer couponId, String userType, String userId) {
        if (couponUsers == null) {
            couponUsers = new ConcurrentHashMap<>();
        }
        String key = User.userIdentify(userType, userId);
        List<String> users = couponUsers.get(couponId);
        if (users == null) {
            users = new ArrayList<>();
            users.add(key);
        } else {
            if (!users.contains(key)) {
                users.add(key);
            }
        }
    }

    public static boolean userInCoupon(String userType, String userId, Coupon coupon) {
        if (coupon.notLimitUser()) {
            return true;
        }
        String key = User.userIdentify(userType, userId);
        List<String> users = couponUsers.get(coupon.getId());
        if (users == null) {
            return false;
        }
        return users.contains(key);
    }
}

package com.mall.coupon.drools.service;

// 模拟用户的coupon

import com.mall.coupon.drools.model.Coupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CouponBatchService {
    private static Map<String, Coupon> couponBatch;

    public static void addCouponBatch(Coupon coupon) {
        if (couponBatch == null) {
            couponBatch = new ConcurrentHashMap<>();
        }
        couponBatch.put(coupon.getCouponBatchCode(), coupon);
    }

    // 模拟获取coupon 适用的 sku list
    public static List<Integer> getCouponSKUIds(String code) {
        List<Integer> ids = new ArrayList<>();
        Coupon coupon = getCouponByCode(code);
        if (coupon == null) {
            log.warn("not found coupon by code {}", code);
            return ids;
        }
        // 从数据库的关联表中查找该电子券可以使用的sku list
        ids.add(10);
        ids.add(20);
        ids.add(24);
        ids.add(28);
        return ids;
    }

    public static boolean skuUsable(String code, Integer skuId) {
        List<Integer> ids = getCouponSKUIds(code);
        return ids.contains(skuId);
    }

    // code 可以是 batch code, 也可以是用户获取的code
    public static Coupon getCouponByCode(String code) {
        // 1. 先安装用户的code来查
        Coupon coupon = UserCouponService.getUserCouponByCode(code);
        if (coupon != null) {
            // todo 时间检查
            return coupon;
        }

        if (couponBatch == null) {
            return null;
        }
        coupon = couponBatch.get(code);
        if (coupon == null) {
            return null;
        }
        // 如果 coupon 不需要绑定
        if (coupon.notBindingType()) {
            return coupon;
        }
        return null;
    }
}

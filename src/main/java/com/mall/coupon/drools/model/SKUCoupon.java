package com.mall.coupon.drools.model;

import lombok.Data;

import java.util.List;

@Data
public class SKUCoupon extends SKU {
    private Integer discount;     // 折扣金额
    private Integer apportion;    // 分摊金额
    private List<String> coupon;  // 适用的 coupon batch code
}

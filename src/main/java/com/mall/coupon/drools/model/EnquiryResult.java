package com.mall.coupon.drools.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 询价结果

@Data
public class EnquiryResult {
    // 总折扣, 单位分
    private Integer totalDiscount;
    // 总折扣, 单位分
    private Integer couponDiscount;
    // 支付折扣金额
    private Integer payDiscount;
    // 运费折扣金额
    private Integer freightDiscount;
    // 积分抵扣金额
    private Integer pointDiscount;
    // 赠品
    private List<SKUGift> gifts;
    // order中每件商品的折扣分摊
    private List<SKUCoupon> items;
    // 适用的 coupon 列表
    private List<Coupon> coupons;

    public EnquiryResult() {
        this.totalDiscount = 0;
        this.couponDiscount = 0;
        this.payDiscount = 0;
        this.freightDiscount = 0;
        this.pointDiscount = 0;
        this.gifts = new ArrayList<>();
        this.items = new ArrayList<>();
        this.coupons = new ArrayList<>();
    }
}

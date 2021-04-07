package com.mall.coupon.drools.model;

import lombok.Data;

@Data
public class SKUGift extends SKU {
    private Integer count;
    private Integer price; // 可选 赠品价格, 覆盖原价
}

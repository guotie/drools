package com.mall.coupon.drools.model;

import lombok.Data;

@Data
public class SKU {
    private Integer id;
    private Integer productId;
    private String skuCode;
    private Integer price;
    private Integer promotionPrice;
}

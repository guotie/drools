package com.mall.coupon.drools.model;

import lombok.Data;

// Order 包含的产品信息

@Data
public class OrderItem {
    private Integer id; //                   bigint not null auto_increment,
    private Integer orderId; //             bigint comment '订单id',
    private String orderSn; //             varchar(64) comment '订单编号',
    private Integer productId; //           bigint comment '商品id',
    private String productName; //         varchar(200) comment '商品名称',
    private String productBrand; //        varchar(200) comment '商品品牌',
    private String productSn; //           varchar(64) comment '商品条码',
    private Integer productPrice; //        decimal(10,2) comment '销售价格',
    private Integer productQuantity; //     int comment '购买数量',
    private Integer productSkuId; //       bigint comment '商品sku编号',
    private String productSkuCode; //     varchar(50) comment '商品sku条码',
    private Integer productCategoryId; //  bigint comment '商品分类id',
    private String promotionName; //       varchar(200) comment '商品促销名称',
    private Integer promotionAmount; //     decimal(10,2) comment '商品促销分解金额',
    private Integer couponAmount; //        decimal(10,2) comment '优惠券优惠分解金额',
    private Integer integrationAmount; //   decimal(10,2) comment '积分优惠分解金额',
    private Integer totalAmount; //          decimal(10,2) comment 'productPrice * productQuantity',
    private Integer realAmount; //          decimal(10,2) comment '该商品经过优惠后的分解金额',
    private Integer giftIntegration; //     int not null default 0 comment '商品赠送积分',
    private Integer giftGrowth; //          int not null default 0 comment '商品赠送成长值',
    private String productAttr; //         varchar(500) comment '商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]',

    public OrderItem(Integer productId, Integer skuId, Integer price, Integer count) {
        this.productId = productId;
        this.productSkuId = skuId;
        this.productPrice = price;
        this.productQuantity = count;
        this.totalAmount = price * count;
    }
}

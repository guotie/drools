package com.mall.coupon.drools.model;

import lombok.Data;

// Order 总表
@Data
public class Order {
    private Integer id;
    private String userType;
    private String userId;
    private String username;
    private String orderSn;  //

    private Integer totalAmount;  //         decimal(10,2) comment '订单总金额',
    private Integer payAmount;  //            decimal(10,2) comment '应付金额（实际支付金额）',
    private Integer freightAmount;  //        decimal(10,2) comment '运费金额',
    private Integer promotionAmount;  //      decimal(10,2) comment '促销优化金额（促销价、满减、阶梯价）',
    private Integer integrationAmount;  //    decimal(10,2) comment '积分抵扣金额',
    private Integer couponAmount;  //         decimal(10,2) comment '优惠券抵扣金额',
    private Integer discountAmount;  //       decimal(10,2) comment '管理员后台调整订单使用的折扣金额',
    private Integer payType;  //              int(1) comment '支付方式：0->未支付；1->支付宝；2->微信',
    private Integer sourceType;  //           int(1) comment '订单来源：0->PC订单；1->app订单',
    private Integer status;  //                int(1) comment '订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单',
    private Integer orderType;  //
}

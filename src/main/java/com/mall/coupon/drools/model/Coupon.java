package com.mall.coupon.drools.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class  Coupon {
    private Integer id;
    private String couponBatchCode;
    // (1 固定折扣、2 固定减免金额、3 固定金额) 4: 满减； 5: 折扣， 6: 满赠； 7: 返还； 8: 支付方式； 9: 兑换；
    private String couponType;
    private String useType;
    private String usetimeType;
    private Date startTime;
    private Date endTime;
    private BigDecimal expiryTime;
    private Integer limitPerUser;
    private Integer limitNumber;
    private Integer prodNumber;
    private Integer extendNumber;
    private String productId;
    private String productAmount;
    private BigDecimal discountNum;
    private String calculateExpres;
    private String couponBatchDesc;
    private Date createTime;
    private String isDelete;
    private String activityType;

    // extend field
    private String version;
    private String couponName;
    private String claimType;
    private String channelType;
    private String groupType;
    private String subCouponType;
    private String discountPkgType;
    private String discountPkgMonth;
    private String logoActive;
    private String logoInactive;
    private String couponRules;
    private String feTag;
    private Integer nominal;
    private Integer minBuyAmount;
    private Integer minBuyCount;
    private Integer maxDiscountCount;
    private String isThird;
    private Integer claimCount;
    private String status;
    private String mallId;
    private Integer price;
    private Integer maxDiscountAmount;
    private String currency;

    public boolean notLimitUser() {
        // 为空或为零时 不限用户
        return groupType == null || "".equals(groupType) || "0".equals(groupType);
    }

    public boolean notBindingType() {
        return claimType == null || "".equals(claimType) || "0".equals(claimType);
    }
}

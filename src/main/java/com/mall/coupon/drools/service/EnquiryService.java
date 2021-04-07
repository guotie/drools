package com.mall.coupon.drools.service;

// 调用规则脚本询价

import com.mall.coupon.drools.model.EnquiryResult;
import com.mall.coupon.drools.model.Coupon;
import com.mall.coupon.drools.model.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnquiryService {

    // 询价
    public static EnquiryResult enquiry(Order order, String code) {
        EnquiryResult result = new EnquiryResult();
        Coupon coupon = CouponBatchService.getCouponByCode(code);

        if (coupon == null) {
            log.warn("not found coupon {}", code);
            return result;
        }

        return result;
    }
}

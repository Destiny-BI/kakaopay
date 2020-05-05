package com.kakaopay.coupon.bicoupon.model.coupon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 쿠폰 정보 output
 */
@Getter
@Setter
@NoArgsConstructor
public class CouponInfo {
    private String user;
    private String couponName;
    private String couponNumber;
    private String couponStatus;
    private BigDecimal discountAmount;
    private String issueDate;
    private String expireDate;
}

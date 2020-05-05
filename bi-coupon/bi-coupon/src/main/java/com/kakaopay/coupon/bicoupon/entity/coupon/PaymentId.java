package com.kakaopay.coupon.bicoupon.entity.coupon;

import java.io.Serializable;
import java.util.UUID;

public class PaymentId implements Serializable {
    private long user;
    private UUID coupon;
    private String payStsCd;
}

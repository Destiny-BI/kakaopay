package com.kakaopay.coupon.bicoupon.advice.exception;

public class CCouponNotPayException extends RuntimeException {
    public CCouponNotPayException(String msg, Throwable t) {
        super(msg, t);
    }

    public CCouponNotPayException(String msg) {
        super(msg);
    }

    public CCouponNotPayException() {
        super();
    }
}

package com.kakaopay.coupon.bicoupon.advice.exception;

public class CCouponExpiredException extends RuntimeException {
    public CCouponExpiredException(String msg, Throwable t) {
        super(msg, t);
    }

    public CCouponExpiredException(String msg) {
        super(msg);
    }

    public CCouponExpiredException() {
        super();
    }
}

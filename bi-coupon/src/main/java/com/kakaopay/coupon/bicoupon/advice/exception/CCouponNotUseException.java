package com.kakaopay.coupon.bicoupon.advice.exception;

public class CCouponNotUseException extends RuntimeException {
    public CCouponNotUseException(String msg, Throwable t) {
        super(msg, t);
    }

    public CCouponNotUseException(String msg) {
        super(msg);
    }

    public CCouponNotUseException() {
        super();
    }
}

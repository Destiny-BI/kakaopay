package com.kakaopay.coupon.bicoupon.advice.exception;

public class CCouponNbrFailException extends RuntimeException {
    public CCouponNbrFailException(String msg, Throwable t) {
        super(msg, t);
    }

    public CCouponNbrFailException(String msg) {
        super(msg);
    }

    public CCouponNbrFailException() {
        super();
    }
}

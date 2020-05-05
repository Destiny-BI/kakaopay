package com.kakaopay.coupon.bicoupon.advice.exception;

public class CCouponNotCancelException extends RuntimeException {
    public CCouponNotCancelException(String msg, Throwable t) {
        super(msg, t);
    }

    public CCouponNotCancelException(String msg) {
        super(msg);
    }

    public CCouponNotCancelException() {
        super();
    }
}

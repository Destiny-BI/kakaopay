package com.kakaopay.coupon.bicoupon.advice.exception;

public class CPayNotFoundException extends RuntimeException {
    public CPayNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CPayNotFoundException(String msg) {
        super(msg);
    }

    public CPayNotFoundException() {
        super();
    }
}

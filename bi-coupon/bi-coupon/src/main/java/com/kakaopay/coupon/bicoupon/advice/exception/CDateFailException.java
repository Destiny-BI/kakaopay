package com.kakaopay.coupon.bicoupon.advice.exception;

public class CDateFailException extends RuntimeException {
    public CDateFailException(String msg, Throwable t) {
        super(msg, t);
    }

    public CDateFailException(String msg) {
        super(msg);
    }

    public CDateFailException() {
        super();
    }
}

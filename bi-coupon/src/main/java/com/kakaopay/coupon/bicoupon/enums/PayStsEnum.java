package com.kakaopay.coupon.bicoupon.enums;

/**
 * 쿠폰의 지급에 대한 관계 상태
 */
public enum PayStsEnum {
    ACTIVE      ("1"),
    TERMINATE   ("2");

    private String value;

    PayStsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}

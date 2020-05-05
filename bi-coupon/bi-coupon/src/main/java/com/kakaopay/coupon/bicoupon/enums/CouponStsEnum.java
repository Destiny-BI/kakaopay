package com.kakaopay.coupon.bicoupon.enums;

/**
 * 쿠폰의 상태를 정의.
 */
public enum CouponStsEnum {

    ISSUED ("I", "발급된 쿠폰"),       // 발급된 쿠폰
    PAID   ("P", "지급된 쿠폰"),       // 지급된 쿠폰
    USED   ("U", "사용된 쿠폰");       // 사용된 쿠폰

    private String value;
    private String name;

    CouponStsEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }
    public String getName() { return name; }

    public static CouponStsEnum getEnum(String value) {
        for (CouponStsEnum item : CouponStsEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

}

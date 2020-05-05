package com.kakaopay.coupon.batch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageForm {
    private String userId;
    private String couponNbr;
    private String msg;

    @Override
    public String toString() {
        return "MessageForm{" +
                "사용자 = '" + userId + '\'' +
                ", 쿠폰 번호 = '" + couponNbr + '\'' +
                ", 메시지 = '" + msg + '\'' +
                '}';
    }
}

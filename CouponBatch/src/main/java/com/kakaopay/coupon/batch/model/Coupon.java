package com.kakaopay.coupon.batch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class Coupon {
    @Id
    private UUID couponId;

    private String couponNm;
    private String couponStsCd;
    private BigDecimal discountAmt;
    private String issueDt;
    private String expireDt;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public String getCouponIdToString() {
        return couponId.toString();
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponNm='" + couponNm + '\'' +
                ", couponStsCd='" + couponStsCd + '\'' +
                ", discountAmt=" + discountAmt +
                ", issueDt='" + issueDt + '\'' +
                ", expireDt='" + expireDt + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

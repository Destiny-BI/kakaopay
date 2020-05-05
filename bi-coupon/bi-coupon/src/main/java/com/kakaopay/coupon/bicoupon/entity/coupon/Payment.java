package com.kakaopay.coupon.bicoupon.entity.coupon;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaopay.coupon.bicoupon.entity.common.CommonDateEntity;
import com.kakaopay.coupon.bicoupon.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@IdClass(PaymentId.class)
public class Payment extends CommonDateEntity {
    @Id
    @Column(nullable = false, length = 1)
    private String payStsCd;        // 지급 상태
    @Column(nullable = false, length = 8)
    private String payStartDt;           // 지급 시작일자
    @Column(nullable = false, length = 8)
    private String payEndDt;              // 지급 종료일자

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;  // 사용 시 셋팅, 지급 : 쿠폰 관계 - N:1

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msrl")
    private User user;      // 사용 시 셋팅, 지급 : 사용자 관계 - N:1

    // 생성자
    public Payment(User user, Coupon coupon, String payStsCd, String payStartDt, String payEndDt) {
        this.user = user;
        this.coupon = coupon;
        this.payStsCd = payStsCd;
        this.payStartDt = payStartDt;
        this.payEndDt = payEndDt;
    }

}

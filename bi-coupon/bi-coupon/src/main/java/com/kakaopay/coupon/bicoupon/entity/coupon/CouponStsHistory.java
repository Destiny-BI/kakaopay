package com.kakaopay.coupon.bicoupon.entity.coupon;

import com.kakaopay.coupon.bicoupon.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "couponStsHst")
@IdClass(CouponStsHistoryId.class)
public class CouponStsHistory extends CommonDateEntity {

    @Id
    private long chngSeqNbr;

    @Column(nullable = false, length = 2)
    private String couponStsCd;

    @Column(nullable = false, length = 8)
    private String chngStsDt;

    @Column(nullable = false, length = 1)
    private String cnclYn;

    @Id
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;  // 이력 : 쿠폰 의 관계 - N : 1

    // 생성자
    public CouponStsHistory(Coupon coupon, long chngSeqNbr, String couponStsCd, String chngStsDt, String cnclYn) {
        this.coupon = coupon;
        this.chngSeqNbr = chngSeqNbr;
        this.couponStsCd = couponStsCd;
        this.chngStsDt = chngStsDt;
        this.cnclYn = cnclYn;
    }

}

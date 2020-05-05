package com.kakaopay.coupon.bicoupon.entity.coupon;

import com.kakaopay.coupon.bicoupon.advice.exception.CCouponExpiredException;
import com.kakaopay.coupon.bicoupon.advice.exception.CCouponNotCancelException;
import com.kakaopay.coupon.bicoupon.advice.exception.CCouponNotPayException;
import com.kakaopay.coupon.bicoupon.advice.exception.CCouponNotUseException;
import com.kakaopay.coupon.bicoupon.entity.common.CommonDateEntity;
import com.kakaopay.coupon.bicoupon.entity.user.User;
import com.kakaopay.coupon.bicoupon.enums.CouponStsEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Coupon extends CommonDateEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID couponId;      // 쿠폰 식별자, 수정 불가

    @Column(nullable = false, length = 100, updatable = false)
    private String couponNm;    // 쿠폰명, 수정 불가

    @Column(nullable = false, length = 1)
    private String couponStsCd;   // 쿠폰 상태코드

    @Column(length = 10, updatable = false)
    private BigDecimal discountAmt;

    @Column(nullable = false, length = 8, updatable = false)
    private String issueDt;    // 쿠폰발급일자, 수정 불가

    @Column(nullable = false, length = 8, updatable = false)
    private String expireDt;     // 쿠폰만료일자, 수정 불가

    @Column(nullable = true, length = 100)
    private String userId;            // user info

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon")
    private List<CouponStsHistory> couponStsHistories = new ArrayList<CouponStsHistory>();


    // 생성자
    public Coupon(String couponNm, String couponStsCd, BigDecimal discountAmt, String issueDt, String expireDt) {
        this.couponNm = couponNm;
        this.discountAmt = discountAmt;
        this.couponStsCd = couponStsCd;
        this.issueDt = issueDt;
        this.expireDt = expireDt;
    }

    public void pay(User user) {
        // 만기일 체크
        _validPastMaturityCoupon();
        // 쿠폰이 발급 상태인지 확인해야 함.
        if (!CouponStsEnum.ISSUED.getValue().equals(this.couponStsCd)) {
            // 지급할 수 없는 쿠폰입니다.
            throw new CCouponNotPayException();
        }
        this.userId = user.getUsername();
        this.couponStsCd = CouponStsEnum.PAID.getValue();
    }

    public void use() {
        // 만기일 체크
        _validPastMaturityCoupon();
        // 사용 가능 여부 확인
        if (!CouponStsEnum.PAID.getValue().equals(couponStsCd)) {
            // exception : 사용할 수 없는 쿠폰입니다.
            throw new CCouponNotUseException();
        }
        this.couponStsCd = CouponStsEnum.USED.getValue();
    }

    public void cancel() {
        // 만기일 체크
        _validPastMaturityCoupon();
        if (!CouponStsEnum.USED.getValue().equals(couponStsCd)) {
            // exception : 취소할 수 없는 쿠폰입니다.
            throw new CCouponNotCancelException();
        }
        this.couponStsCd = CouponStsEnum.PAID.getValue();
    }

    /**
     * 쿠폰의 만기일 체크
     */
    private void _validPastMaturityCoupon() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String toDate = sdf.format(date);

        if (toDate.compareTo(expireDt) > 0) {
            // exception : 만료된 쿠폰입니다.
            throw new CCouponExpiredException();
        }
    }
}

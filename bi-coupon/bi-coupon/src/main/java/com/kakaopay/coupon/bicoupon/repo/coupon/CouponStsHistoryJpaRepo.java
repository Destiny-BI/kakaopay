package com.kakaopay.coupon.bicoupon.repo.coupon;

import com.kakaopay.coupon.bicoupon.entity.coupon.Coupon;
import com.kakaopay.coupon.bicoupon.entity.coupon.CouponStsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponStsHistoryJpaRepo extends JpaRepository<CouponStsHistory, Long> {
    @Query("select max(csh.chngSeqNbr) from CouponStsHistory csh where csh.coupon.couponId = :#{#paramCoupon.couponId}")
    Long findMaxSeq(@Param("paramCoupon") Coupon coupon);
}

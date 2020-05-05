package com.kakaopay.coupon.bicoupon.repo.coupon;

import com.kakaopay.coupon.bicoupon.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponJpaRepo extends JpaRepository<Coupon, UUID> {
    // 사용되지 않고 만료된 쿠폰 목록 조회
    @Query("select c from Coupon c where c.expireDt = :expireDt and c.couponStsCd <> 'U'")
    List<Coupon> findAllByExpireDt(String expireDt);

    // 사용가능한 쿠폰 한 개 조회
    Optional<Coupon> findFirstByCouponStsCd(String couponStsCd);
}

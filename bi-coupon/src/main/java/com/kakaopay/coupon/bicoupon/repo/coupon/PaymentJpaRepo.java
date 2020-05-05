package com.kakaopay.coupon.bicoupon.repo.coupon;

import com.kakaopay.coupon.bicoupon.entity.coupon.Coupon;
import com.kakaopay.coupon.bicoupon.entity.coupon.Payment;
import com.kakaopay.coupon.bicoupon.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepo extends JpaRepository<Payment, Integer> {
    @Query("select p from Payment as p where p.user.msrl = :#{#paramUser.msrl} and p.payStsCd = '1'")
    List<Payment> findAllByUser(@Param("paramUser") User user);

    @Query("select p from Payment as p where p.user.msrl = :#{#paramUser.msrl} and p.coupon.couponId = :couponId and p.payStsCd = '1'")
    Optional<Payment> findByUserAndCouponId(@Param("paramUser") User user, UUID couponId);

}

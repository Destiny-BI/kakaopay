package com.kakaopay.coupon.bicoupon.repo;

import com.kakaopay.coupon.bicoupon.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Table 에 질의를 요청하기 위한 Repository 생성
 *
 */
public interface UserJpaRepo extends JpaRepository<User, Long> {

    Optional<User> findByUid(String email);
}

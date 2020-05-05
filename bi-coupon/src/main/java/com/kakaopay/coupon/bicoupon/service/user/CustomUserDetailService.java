package com.kakaopay.coupon.bicoupon.service.user;

import com.kakaopay.coupon.bicoupon.advice.exception.CEmailSigninFailedException;
import com.kakaopay.coupon.bicoupon.advice.exception.CUserNotFoundException;
import com.kakaopay.coupon.bicoupon.repo.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepo userJpaRepo;

    @Override
    public UserDetails loadUserByUsername(String userPk) {
        return userJpaRepo.findById(Long.valueOf(userPk)).orElseThrow(CUserNotFoundException::new);
    }
}

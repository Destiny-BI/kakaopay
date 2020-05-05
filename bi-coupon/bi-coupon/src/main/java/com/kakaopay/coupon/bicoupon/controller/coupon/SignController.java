package com.kakaopay.coupon.bicoupon.controller.coupon;

import com.kakaopay.coupon.bicoupon.advice.exception.CEmailSigninFailedException;
import com.kakaopay.coupon.bicoupon.controller.config.security.JwtTokenProvider;
import com.kakaopay.coupon.bicoupon.entity.user.User;
import com.kakaopay.coupon.bicoupon.model.response.CommonResult;
import com.kakaopay.coupon.bicoupon.model.response.SingleResult;
import com.kakaopay.coupon.bicoupon.repo.UserJpaRepo;
import com.kakaopay.coupon.bicoupon.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/bicoupon")
public class SignController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider  jwtTokenProvider;
    private final ResponseService   responseService;
    private final PasswordEncoder   passwordEncoder;

    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@RequestParam String id, @RequestParam String password) {
        User user = userJpaRepo.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CEmailSigninFailedException();
        }
        return responseService.getSingeResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @PostMapping(value = "/signup")
    public CommonResult signup(@RequestParam String id, @RequestParam String password, @RequestParam String name, @RequestParam boolean isAdmin) {
        String role = "ROLE_USER";
        if (isAdmin) {
            role = "ROLE_ADMIN";
        }

        userJpaRepo.save(User.builder()
        .uid(id)
        .password(passwordEncoder.encode(password))
        .name(name)
        .roles(Collections.singletonList(role))
        .build());
        return responseService.getSuccessResult();
    }
}

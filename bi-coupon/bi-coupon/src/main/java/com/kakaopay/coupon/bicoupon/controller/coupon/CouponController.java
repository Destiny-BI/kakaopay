package com.kakaopay.coupon.bicoupon.controller.coupon;

import com.kakaopay.coupon.bicoupon.model.coupon.CouponInfo;
import com.kakaopay.coupon.bicoupon.model.coupon.ParamsCoupon;
import com.kakaopay.coupon.bicoupon.model.response.CommonResult;
import com.kakaopay.coupon.bicoupon.model.response.ListResult;
import com.kakaopay.coupon.bicoupon.model.response.SingleResult;
import com.kakaopay.coupon.bicoupon.service.ResponseService;
import com.kakaopay.coupon.bicoupon.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/bicoupon/management")
public class CouponController {

    private final CouponService couponService;
    private final ResponseService responseService;

    @PostMapping(value = "/coupon/creation")
    public CommonResult issueCoupon(@RequestParam int count) {
        couponService.generateCoupon(count);
        return responseService.getSuccessResult();
    }

    @PutMapping(value = "/coupon/payment")
    public SingleResult<CouponInfo> payCoupon(@RequestParam String userId) {
        return responseService.getSingeResult(couponService.payCoupon(userId));
    }

    @GetMapping(value = "/coupon/information/payment")
    public ListResult<CouponInfo> findPaidCouponList(@RequestParam String userId) {
        return responseService.getListResult(couponService.getPaidCouponList(userId));
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/coupon/use")
    public CommonResult useCoupon(@RequestParam String couponNbr) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        couponService.useCoupon(id, couponNbr);
        return responseService.getSuccessResult();
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/coupon/use-cancellation")
    public CommonResult cancelCoupon(@RequestParam String couponNbr) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        couponService.cancelCoupon(id, couponNbr);
        return responseService.getSuccessResult();
    }

    @GetMapping(value = "/coupon/information/expiration")
    public ListResult<CouponInfo> findExpiredCouponOnToday(@Nullable @RequestParam String today) {
        return responseService.getListResult(couponService.getExpiredCouponListOnToday(today));
    }


}

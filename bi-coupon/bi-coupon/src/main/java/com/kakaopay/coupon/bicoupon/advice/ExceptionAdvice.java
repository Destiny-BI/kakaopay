package com.kakaopay.coupon.bicoupon.advice;

import com.kakaopay.coupon.bicoupon.advice.exception.*;
import com.kakaopay.coupon.bicoupon.model.response.CommonResult;
import com.kakaopay.coupon.bicoupon.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        // 예외 처리의 메시지를 MessageSource 에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler(CEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSigninFailedException(HttpServletRequest request, CEmailSigninFailedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("entryPointException.code")), getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));
    }

    @ExceptionHandler(CCouponNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult couponNotFoundException(HttpServletRequest request, CCouponNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("couponNotFound.code")), getMessage("couponNotFound.msg"));
    }

    @ExceptionHandler(CCouponNotUseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult couponNotUseException(HttpServletRequest request, CCouponNotUseException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("couponNotUse.code")), getMessage("couponNotUse.msg"));
    }

    @ExceptionHandler(CCouponNotPayException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult couponNotPayException(HttpServletRequest request, CCouponNotPayException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("couponNotPay.code")), getMessage("couponNotPay.msg"));
    }

    @ExceptionHandler(CCouponNotCancelException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult couponNotCancelException(HttpServletRequest request, CCouponNotCancelException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("couponNotCancel.code")), getMessage("couponNotCancel.msg"));
    }

    @ExceptionHandler(CDateFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult dateFailException(HttpServletRequest request, CDateFailException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("dateFail.code")), getMessage("dateFail.msg"));
    }

    @ExceptionHandler(CPayNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult payNotFoundException(HttpServletRequest request, CPayNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("payNotFound.code")), getMessage("payNotFound.msg"));
    }

    @ExceptionHandler(CCouponExpiredException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult couponExpiredException(HttpServletRequest request, CCouponExpiredException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("couponExpired.code")), getMessage("couponExpired.msg"));
    }

    @ExceptionHandler(CCouponNbrFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult couponNbrFailException(HttpServletRequest request, CCouponNbrFailException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("couponNbrFail.code")), getMessage("couponNbrFail.msg"));
    }





    // code 정보에 해당하는 메시지를 조회한다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }
    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}

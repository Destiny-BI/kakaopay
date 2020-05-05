package com.kakaopay.coupon.bicoupon.model.coupon;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.GeneratedValue;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 쿠폰 정보를 입력받는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ParamsCoupon {
    @NotEmpty
    @Size(max=50)
    private String couponNm;
    @NotEmpty
    @Size(min=8, max=8)
    private String expireDt;
}

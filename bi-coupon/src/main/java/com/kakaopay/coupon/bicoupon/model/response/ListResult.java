package com.kakaopay.coupon.bicoupon.model.response;

import java.util.List;

/**
 * 결과가 여러건인 api를 담는 모델
 * CommonResult 를 상속받으므로 api 요청 결과도 같이 출력
 * @param <T>
 */
public class ListResult<T> extends CommonResult {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

package com.kakaopay.coupon.bicoupon.model.response;

/**
 * 결과가 단일건일 api 를 담는 모델.
 * CommonResult 를 상속받으므로 api 요청 결과도 같이 출력된다.
 * @param <T>
 */
public class SingleResult<T> extends CommonResult {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

## Reat API 기반 쿠폰시스템

< 문제 >
7. 선택 문제
  - 발급된 쿠폰 중 만료 3일전 사용자에게 메시지("쿠폰이 3일 후 만료됩니다.") 를
    발송하는 기능을 구현하세요. (실제 메세지를 발송하는 것이 아닌 stdout 등으로 출력하시면 됩니다.)

## 개발 프레임워크
- Spring Framework

## 문제해결 전략
- Spring Batch 사용.
- 구현 <br>
  reader : 입력된 일자 기준으로 3일 후 만료되는 쿠폰을 조회 한 후 <br>
  processor : message 를 포함한 DTO 로 변경. <br>
  writer : DTO 의 정보를 출력.

## 빌드 및 실행 방법
- 배치 실행 parameter <br>
  job.name=couponJob toDay=20200505 <br>
- 배치 실행 (intellij) <br>
  com.kakaopay.coupon.batch.CouponBatchApplication > 우 클릭 후 Run 'CouponBatchApplication'
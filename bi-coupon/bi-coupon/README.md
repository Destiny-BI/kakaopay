## Reat API 기반 쿠폰시스템

< 문제 > <br>
필수문제
1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API <br>
  \- 쿠폰 생성
2. 생성된 쿠폰 중 하나를 사용자에게 지급하는 API<br>
  \- 쿠폰 지급<br>
  \- output : 쿠폰 번호<br>
  \- 생성된 쿠폰 중 하나를 지급
3. 사용자에게 지급된 쿠폰을 조회하는 API
4. 지급된 쿠폰 중 하나를 사용하는 API<br>
  \- 쿠폰 재사용은 불가 <br>
  \- input : 쿠폰 번호
5. 지급된 쿠폰 중 하나를 사용 취소하는 API<br>
  \- 취소된 쿠폰은 재사용 가능<br>
  \- input : 쿠폰 번호
6. 발급된 쿠폰 중 당일 만료된 전체 쿠폰 목록을 조회하는 API<br>
  \- 만료일이 하루 전 대상 조회(만료일이 지나야 만료된 것이라 가정)
  
## 개발 프레임워크
- Spring Framework (SpringBoot 2.2.6)

## 문제해결 전략
- SpringBoot 환경 구성(JPA, H2 DB, Lambok, SpringTest).
- 인증/권한을 위한 Spring Security / JWT Token 사용.<br>
  : signup("/bicoupon/signup"), signin("/bicoupon/signin")
- Password 암호화 Spring Security 의 PasswordEncoder 사용.  
- 중복되지 않는 쿠폰 번호를 위한 UUID 사용.
- UUID 사용으로 100억개 이상의 쿠폰 생성 가능.
- 쿠폰의 상태 변경 시 쿠폰 객체 내에서 검증 및 상태 변경.
- RESTFUL 전략<br>
  : URL 에 동사 사용 X<br>
  : POST(사물이나 행위의 생성이나 시작 - 문제.1) <br>
  : PUT(사물이나 행위의 상태 변화 - 문제.2,4,5) <br>
  : GET(정보 조회  - 문제.3,6) <br>
  : DELETE(의미적 삭제) <br> 

## 빌드 및 실행 방법
- H2 DB 실행 <br>
  : 디렉토리 h2 > bin > h2w 실행.
- Application 실행 (intellij) <br>
  com.kakaopay.coupon.batch.BiCouponApplication > 우 클릭 후 Run 'BiCouponApplication'
- Postman 사용 (postman work file 첨부)
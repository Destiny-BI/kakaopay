package com.kakaopay.coupon.bicoupon.service.coupon;

import com.kakaopay.coupon.bicoupon.advice.exception.*;
import com.kakaopay.coupon.bicoupon.entity.coupon.Coupon;
import com.kakaopay.coupon.bicoupon.entity.coupon.CouponStsHistory;
import com.kakaopay.coupon.bicoupon.entity.coupon.Payment;
import com.kakaopay.coupon.bicoupon.entity.user.User;
import com.kakaopay.coupon.bicoupon.enums.PayStsEnum;
import com.kakaopay.coupon.bicoupon.model.coupon.CouponInfo;
import com.kakaopay.coupon.bicoupon.model.coupon.ParamsCoupon;
import com.kakaopay.coupon.bicoupon.repo.UserJpaRepo;
import com.kakaopay.coupon.bicoupon.repo.coupon.CouponJpaRepo;
import com.kakaopay.coupon.bicoupon.repo.coupon.CouponStsHistoryJpaRepo;
import com.kakaopay.coupon.bicoupon.repo.coupon.PaymentJpaRepo;
import com.kakaopay.coupon.bicoupon.enums.CouponStsEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Transactional
@RequiredArgsConstructor
@Service
public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    private final CouponJpaRepo couponJpaRepo;
    private final PaymentJpaRepo paymentJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final CouponStsHistoryJpaRepo couponStsHstJpaRepo;

    /**
     * 1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API
     *   - 쿠폰 생성
     * @param genCount
     */
    public void generateCoupon(int genCount) {
        Coupon coupon = null;
        // 날짜 검증
        //_validateDateType(paramsCoupon.getExpireDt());
        List<Coupon> couponList = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String toDate = sdf.format(c.getTime());

        c.add(c.DATE, 7);
        String expireDt = sdf.format(c.getTime());
        // 기간 검증
        //_validatePeriod(toDate, paramsCoupon.getExpireDt());

        for (int i = 0; i < genCount; i++) {
            coupon = new Coupon("BICOUPON"
                    , CouponStsEnum.ISSUED.getValue()
                    , new BigDecimal(5000)
                    , toDate
                    , expireDt);

            couponList.add(coupon);
        }
        couponJpaRepo.saveAll(couponList);
    }

    /**
     * 2. 생성된 쿠폰 중 하나를 사용자에게 지급하는 API
     *   - 쿠폰 지급
     *   - output : 쿠폰번호
     * @param uid
     * @return
     */
    public CouponInfo payCoupon(String uid) {
        // 사용자 조회
        User user = getUser(uid);
        // 쿠폰 조회
        // exception : 쿠폰을 찾을 수 없습니다.
        Coupon coupon = couponJpaRepo.findFirstByCouponStsCd(CouponStsEnum.ISSUED.getValue()).orElseThrow(CCouponNotFoundException::new);
        // 쿠폰 상태변경이력 생성
        _saveCouponChangeHistory(coupon, false);
        // 쿠폰을 지급 처리 (검증)
        coupon.pay(user);
        couponJpaRepo.save(coupon);

        // 지급 처리
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String toDate = sdf.format(date);

        Payment payment = new Payment(user, coupon, PayStsEnum.ACTIVE.getValue(), toDate, "99991231");
        paymentJpaRepo.save(payment);

        return _assembleOutputCouponInfo(coupon);
    }

    /**
     * 3. 사용자에게 지급된 쿠폰을 조회하는 API
     *   - 조회
     * @param uid
     * @return
     */
    public List<CouponInfo> getPaidCouponList(String uid) {
        List<CouponInfo> couponList = new ArrayList<>();
        // 사용자의 지급상태가 지급인 쿠폰 내역 조회.
        User user = getUser(uid);
        List<Payment> paymentList = paymentJpaRepo.findAllByUser(user);

        for (Payment payment : paymentList) {
            couponList.add(_assembleOutputCouponInfo(payment.getCoupon()));
        }
        return couponList;
    }

    /**
     * 4. 지급된 쿠폰 중 하나를 사용하는 API
     *   - 쿠폰 사용
     *   - 쿠폰 재사용은 불가
     *   - input : 쿠폰번호
     * @param uid
     * @param couponId
     */
    public void useCoupon(String uid, String couponId) {
        // 사용자 조회
        User user = getUser(uid);
        // 지급 정보 조회
        // exception : 지급 정보가 없습니다.
        Payment payment = paymentJpaRepo.findByUserAndCouponId(user, _changeCouponNbrToUUID(couponId)).orElseThrow(CPayNotFoundException::new);
        // 지급된 쿠폰 조회
        Coupon coupon = payment.getCoupon();
        // 쿠폰 상태 변경 이력 생성
        _saveCouponChangeHistory(coupon, false);
        // 쿠폰 사용 (검증)
        coupon.use();
        couponJpaRepo.save(coupon);
    }

    /**
     * 5. 지급된 쿠폰 중 하나를 사용 취소하는 API
     *   - 사용 취소
     *   - 취소된 쿠폰은 재사용 가능
     *   - input : 쿠폰번호
     * @param uid
     * @param couponNbr
     */
    public void cancelCoupon(String uid, String couponNbr) {
        // 사용자 조회
        User user = getUser(uid);
        // 지급 정보 조회
        // exception : 지급 정보가 없습니다.
        Payment payment = paymentJpaRepo.findByUserAndCouponId(user, _changeCouponNbrToUUID(couponNbr)).orElseThrow(CPayNotFoundException::new);
        // 지급된 쿠폰 조회
        Coupon coupon = payment.getCoupon();
        // 쿠폰 상태 변경 이력 생성
        _saveCouponChangeHistory(coupon, true);
        // 쿠폰 취소(검증)
        coupon.cancel();
        couponJpaRepo.save(coupon);
    }

    /**
     * 6. 발급된 쿠폰 중 당일 만료된 전체 쿠폰 목록을 조회하는 API
     *   - 만료일이 하루 전 대상 조회(만료일이 지나야 만료된 것이라 가정)
     * @param today
     * @return
     */
    public List<CouponInfo> getExpiredCouponListOnToday(String today) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        if (today != null) {
            c.setTime(_validateDateType(today));
        }
        c.add(c.DATE, -1);
        String preDate = sdf.format(c.getTime());

        List<CouponInfo> outputList = new ArrayList<CouponInfo>();
        List<Coupon> couponList = couponJpaRepo.findAllByExpireDt(preDate);
        for (Coupon coupon : couponList) {
            outputList.add(_assembleOutputCouponInfo(coupon));
        }

        return outputList;
    }

    /**
     * 쿠폰을 조회한다.
     * @param couponId
     * @return
     */
//    private Coupon getCoupon(String couponId) {
//        // exception : 쿠폰을 찾을 수 없습니다.
//        return couponJpaRepo.findById(_changeCouponNbrToUUID(couponId)).orElseThrow(CCouponNotFoundException::new);
//    }

    /**
     * 사용자를 조회한다.
     * @param uid
     * @return
     */
    private User getUser(String uid) {
        // exception : 존재하지 않는 회원입니다.
        return userJpaRepo.findByUid(uid).orElseThrow(CUserNotFoundException::new);
    }

    /**
     * 입력된 일자가 유효한지 검증
     * @param date
     */
    private Date _validateDateType(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date formatDate = null;
        try {
            formatDate = sdf.parse(date);
        } catch (ParseException e) {
            // exception : 날짜가 잘못되었습니다.
            throw new CDateFailException();
        }
        return formatDate;
    }

    /**
     * 입력된 시작 종료일이 유효한지 검증
     * @param stDt
     * @param edDt
     */
    private void _validatePeriod(String stDt, String edDt) {
        if (stDt.compareTo(edDt) >= 0) {
            // exception : 날짜가 잘못되었습니다.
            throw new CDateFailException();
        }
    }

    /**
     * 쿠폰 포멧을 확인하고 UUID 로 변환한다.
     * @param couponNbr
     * @return
     */
    private UUID _changeCouponNbrToUUID(String couponNbr) {
        if (!Pattern.matches("^[a-zA-Z0-9]{8}\\-[a-zA-Z0-9]{4}\\-[a-zA-Z0-9]{4}\\-[a-zA-Z0-9]{4}\\-[a-zA-Z0-9]{12}$", couponNbr)) {
            // exception : 쿠폰번호가 잘못되었습니다.
            throw new CCouponNbrFailException();
        }
        UUID uuid = UUID.fromString(couponNbr);
        return uuid;
    }

    /**
     * 쿠폰의 상태 변경이력을 생성
     * @param coupon
     */
    private void _saveCouponChangeHistory(Coupon coupon, boolean cnclYn) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String toDate = sdf.format(date);

        Long chngSeqNbr = couponStsHstJpaRepo.findMaxSeq(coupon);
        if (chngSeqNbr == null) {
            chngSeqNbr = Long.valueOf(0);
        }
        CouponStsHistory history = new CouponStsHistory(coupon, chngSeqNbr+1, coupon.getCouponStsCd(), toDate, cnclYn? "Y" : "N");

        couponStsHstJpaRepo.save(history);
    }

    /**
     * Coupon 을 output 형태로 전환한다.
     * @param coupon
     * @return
     */
    private CouponInfo _assembleOutputCouponInfo(Coupon coupon) {
        CouponInfo couponInfo = null;
        if (coupon != null) {
            couponInfo = new CouponInfo();
            couponInfo.setUser(coupon.getUserId() == null ? "없음" : coupon.getUserId());
            couponInfo.setCouponName(coupon.getCouponNm());
            couponInfo.setCouponNumber(coupon.getCouponId().toString());
            couponInfo.setCouponStatus(CouponStsEnum.getEnum(coupon.getCouponStsCd()).getName());
            couponInfo.setDiscountAmount(coupon.getDiscountAmt());
            couponInfo.setIssueDate(coupon.getIssueDt());
            couponInfo.setExpireDate(coupon.getExpireDt());
        }
        return couponInfo;
    }
}

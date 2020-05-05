package com.kakaopay.coupon.bicoupon.controller.coupon;

import com.kakaopay.coupon.bicoupon.advice.exception.CUserNotFoundException;
import com.kakaopay.coupon.bicoupon.entity.user.User;
import com.kakaopay.coupon.bicoupon.model.response.CommonResult;
import com.kakaopay.coupon.bicoupon.model.response.ListResult;
import com.kakaopay.coupon.bicoupon.model.response.SingleResult;
import com.kakaopay.coupon.bicoupon.repo.UserJpaRepo;
import com.kakaopay.coupon.bicoupon.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController             // 결과값을 JSON으로 출력
@RequestMapping(value = "/bicoupon/user")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;      // 결과를 처리할 Service

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser(@RequestHeader(name = "X-AUTH-TOKEN", required = true) String token) {
        // 결과 데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
        return responseService.getListResult(userJpaRepo.findAll());
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @GetMapping(value = "/user")
//    public SingleResult<User> findUserById() {
//        // 결과데이터가 단건일 경우 getSingleResult를 이용해서 결과를 출력한다.
//        // SecurityContext 에서 인증받은 회원의 정보를 얻어온다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String id = authentication.getName();
//        return responseService.getSingeResult(userJpaRepo.findByUid(id).orElseThrow(CUserNotFoundException::new));
//    }
//
//    // 회원 수정
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PutMapping(value = "/user")
//    public SingleResult<User> modify(@RequestHeader(required = true, name = "X-AUTH-TOKEN") String token, @RequestParam int msrl, @RequestParam String name) {
//        User user = userJpaRepo.findById(Long.valueOf(msrl)).get();
////        User upUser = User.builder()
////                .msrl()
////                .name(name)
////                .build();
//        user.builder().name(name).build();
//
//        return responseService.getSingeResult(userJpaRepo.save(user));
//    }
//
//    // 회원 삭제
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @DeleteMapping(value = "/user/{msrl}")
//    public CommonResult delete(@RequestHeader(required = true, name = "X-AUTH-TOKEN") String token, @PathVariable long msrl) {
//        userJpaRepo.deleteById(msrl);
//        // 성공 결과 정보만 필요한 경우 getSuccessResult() 를 이용하여 결과를 출력한다.
//        return responseService.getSuccessResult();
//    }
}

package com.kakaopay.coupon.bicoupon.controller.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.coupon.bicoupon.controller.config.security.JwtTokenProvider;
import com.kakaopay.coupon.bicoupon.model.coupon.CouponInfo;
import com.kakaopay.coupon.bicoupon.model.response.SingleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CouponControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private WebApplicationContext context;

    private String token;

    @BeforeEach
    public void setup() throws Exception {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context) .addFilters(new CharacterEncodingFilter("UTF-8", true))
//            .alwaysDo(print()) .build();
        token = jwtTokenProvider.createToken(String.valueOf(1), Arrays.asList("ROLE_ADMIN"));
    }

    @Test
    void issueCoupon() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("count", "4");
        // MvcResult result =
        mockMvc.perform(post("/bicoupon/management/coupon/creation").params(params)
                .header("X-AUTH-TOKEN", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists()
                //.andExpect(jsonPath("$.data").exists()
                );
        // .andReturn();
    }

    @Test
    void payCoupon() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "spzcho87@gmail.com");
        MvcResult result = mockMvc.perform(put("/bicoupon/management/coupon/payment").params(params)
                .header("X-AUTH-TOKEN", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists()
                )
                .andReturn();
    }

    @Test
    void findPaidCouponList() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "spzcho87@gmail.com");
        mockMvc.perform(get("/bicoupon/management/coupon/information/payment").params(params)
                .header("X-AUTH-TOKEN", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.list").exists()
                );
    }

    @Test
    void useCoupon() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("couponNbr", "42a1d4b1-47d5-4c27-8272-3edf63ca6a50");
        mockMvc.perform(put("/bicoupon/management/coupon/use").params(params)
                .header("X-AUTH-TOKEN", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists()
                );
    }

    @Test
    void cancelCoupon() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("couponNbr", "42a1d4b1-47d5-4c27-8272-3edf63ca6a50");
        mockMvc.perform(put("/bicoupon/management/coupon/use-cancellation").params(params)
                .header("X-AUTH-TOKEN", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists()
                );
    }

    @Test
    void findExpiredCouponOnToday() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("today", "20200606");
        mockMvc.perform(get("/bicoupon/management/coupon/information/expiration").params(params)
                .header("X-AUTH-TOKEN", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.list").exists()
                );
    }
}
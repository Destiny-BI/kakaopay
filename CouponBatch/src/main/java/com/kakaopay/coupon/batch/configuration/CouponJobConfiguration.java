package com.kakaopay.coupon.batch.configuration;

import com.kakaopay.coupon.batch.model.Coupon;
import com.kakaopay.coupon.batch.model.MessageForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CouponJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory  entityManagerFactory;

    private final int chunkSize = 10;

    private final static Logger logger = LoggerFactory.getLogger(CouponJobConfiguration.class);

    @Bean
    public Job couponJob(JobBuilderFactory jobBuilderFactory, Step couponStep1) {
        return jobBuilderFactory.get("couponJob")
                .preventRestart()
//                .incrementer(new RunIdIncrementer())
                .start(couponStep1)
                .build();
    }

    @Bean
    @JobScope
    public Step couponStep1(StepBuilderFactory stepBuilderFactory) throws Exception {
        logger.info("==================== step start");
        return stepBuilderFactory.get("couponStep")
                .<Coupon, MessageForm> chunk(chunkSize)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Coupon> reader(@Value("#{jobParameters[toDay]}") String toDay) throws Exception {
//        JpaPagingItemReader<Coupon> jpaPagingItemReader = new JpaPagingItemReader<>();
//        // 만료 3일 전 쿠폰 조회
//        jpaPagingItemReader.setQueryString("select c from Coupon c where c.couponStsCd = 'P' and c.expireDt = :baseDt");

        // 만기 3일 전 계산

        JpaPagingItemReader<Coupon> reader = new JpaPagingItemReader<>() {
            @Override
            public int getPage() {
                return 0;
            }
        };

        reader.setQueryString("select c from Coupon c where c.couponStsCd = 'P' and c.expireDt = :baseDt");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = sdf.parse(toDay);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(c.DATE, 3);
        String baseDt = sdf.format(c.getTime());

        Map<String, Object> map = new HashMap<>();
        map.put("baseDt", baseDt);

        logger.info("================== baseDt : {}" , baseDt);

        reader.setParameterValues(map);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(chunkSize);

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<Coupon, MessageForm> processor() {
        logger.info("======================== processor 진입");
        return coupon -> {
            MessageForm msg = new MessageForm();
            msg.setUserId(coupon.getUserId());
            msg.setCouponNbr(coupon.getCouponIdToString());
            msg.setMsg("쿠폰이 3일 후 만료됩니다.");
            return msg;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<MessageForm> writer() {
        return list -> {
            for (MessageForm msg : list) {
                logger.info("만료된 쿠폰 = {}", msg);
            }
        };
    }


}

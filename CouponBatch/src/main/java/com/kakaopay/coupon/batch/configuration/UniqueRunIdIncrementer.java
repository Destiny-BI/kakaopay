package com.kakaopay.coupon.batch.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import java.util.Objects;

public class UniqueRunIdIncrementer extends RunIdIncrementer {
    private String RUN_ID = "run.id";

    private static final Logger logger = LoggerFactory.getLogger(UniqueRunIdIncrementer.class);

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        logger.debug("-------------------------======= param : {}", params.getString(RUN_ID));
        return new JobParametersBuilder()
                .addLong(RUN_ID, Long.valueOf(params.getString(RUN_ID, "0")))
                .toJobParameters();
    }
}

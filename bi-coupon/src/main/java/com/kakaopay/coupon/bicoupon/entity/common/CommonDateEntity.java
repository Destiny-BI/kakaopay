package com.kakaopay.coupon.bicoupon.entity.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonDateEntity {
    @CreatedDate        // Entity 생성 시 자동으로 생성
    private LocalDateTime createdAt;
    @LastModifiedDate   // Entity 수정 시 자동으로 생성
    private LocalDateTime modifiedAt;
}

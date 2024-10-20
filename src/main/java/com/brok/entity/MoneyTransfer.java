package com.brok.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransfer {
    @Id
    @GeneratedValue
    private UUID id;
    private String IBAN;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private MoneyTransferType type;
    @ManyToOne
    @JsonIgnore
    private User user;
    @CreatedDate
    private LocalDateTime date;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime lastModified;
    @LastModifiedBy
    private String lastModifiedBy;
}

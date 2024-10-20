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
public class Wallet {
    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal amount;
    private BigDecimal blockAmount;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @CreatedDate
    private LocalDateTime date;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime lastModified;
    @LastModifiedBy
    private String lastModifiedBy;
    @Version
    private int version;

    public BigDecimal getAmount() {
        if (this.amount == null) {
            return BigDecimal.ZERO;
        }
        return amount;
    }

    public BigDecimal getBlockAmount() {
        if (this.blockAmount == null) {
            return BigDecimal.ZERO;
        }
        return blockAmount;
    }
}

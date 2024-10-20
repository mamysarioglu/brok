package com.brok.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderHistory {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;
    private String symbol;
    private long size;
    private BigDecimal price;
    @Transient
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    @CreatedDate
    private LocalDateTime date;
}

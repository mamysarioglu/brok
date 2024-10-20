package com.brok.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE Orders O SET O.status='CANCELED' where O.id=?")
public class Orders {
    @Id
    @UuidGenerator
    private UUID id;
    private long size;
    private long matchedSize;
    private BigDecimal price;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;
    @Enumerated(value = EnumType.STRING)
    private OrderSide side;
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @ManyToOne
    @JoinColumn(name = "symbol", insertable = false, updatable = false,referencedColumnName = "symbol")
    private Stock stock;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customerId")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime lastModified;
    @LastModifiedBy
    private String lastModifiedBy;
}

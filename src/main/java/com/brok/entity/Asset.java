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

import java.time.LocalDateTime;
import java.util.UUID;

@Table(
        indexes = {
                @Index(
                        name = "Asset_customerId_symbol_index",
                        columnList =  "symbol, customerId",
                        unique = true
                )
        },
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "symbol","customerId"
        })
})
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Asset {
    @Id
    @GeneratedValue
    private UUID id;
    private long size;
    private long blockSize;
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @ManyToOne
    @JoinColumn(name = "symbol",insertable = false, updatable = false,referencedColumnName = "symbol")
    private Stock stock;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
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

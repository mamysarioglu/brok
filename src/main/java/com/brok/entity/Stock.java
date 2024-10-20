package com.brok.entity;

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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Stock {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String symbol;
    private double price;
    private String name;
    @CreatedDate
    private LocalDateTime date;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime lastModified;
    @LastModifiedBy
    private String lastModifiedBy;
}

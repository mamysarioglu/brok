package com.brok.repository;

import com.brok.entity.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, UUID> {
    @Query("SELECT m FROM MoneyTransfer m join m.user u where u.username=:username")
    List<MoneyTransfer> findByUsername(@Param("username") String username);
}

package com.brok.repository;

import com.brok.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, UUID> {
    @Query("SELECT O FROM OrderHistory O JOIN O.fromUser U JOIN O.toUser T WHERE U.username=:username OR T.username=:username")
    List<OrderHistory> getOrders(@Param("username") String username);
}

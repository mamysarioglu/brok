package com.brok.repository;

import com.brok.dto.PendingOrderDTO;
import com.brok.entity.OrderSide;
import com.brok.entity.OrderStatus;
import com.brok.entity.Orders;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {

    @Query("SELECT new com.brok.dto.PendingOrderDTO(S.name,S.id,O.price,sum(O.size-O.matchedSize)) FROM Orders O JOIN O.stock S " +
            " WHERE O.status=:status and O.side=:side" +
            " group by O.price ")
    List<PendingOrderDTO> findByStatusAndSide(@Param("status") OrderStatus status, @Param("side") OrderSide side);

    @Query("SELECT O FROM Orders O JOIN O.user U WHERE U.username=:username")
    List<Orders> getMyOrders(@Param("username") String username);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT O FROM Orders O join O.stock S where O.status = com.brok.entity.OrderStatus.PENDING" +
            " and O.price <=:price" +
            " and S.symbol =:symbol" +
            " and O.side = com.brok.entity.OrderSide.SELL" +
            " order by O.price asc ")
    Optional<List<Orders>> getForBuy(@Param("price") BigDecimal price, @Param("symbol") String symbol);


    @Query("SELECT O FROM Orders O join O.stock S where O.status = com.brok.entity.OrderStatus.PENDING" +
            " and O.price >=:price" +
            " and S.symbol =:symbol" +
            " and O.side = com.brok.entity.OrderSide.BUY" +
            " order by O.price asc ")
    Optional<List<Orders>> getForSell(@Param("price") BigDecimal price, @Param("symbol") String symbol);

    @Modifying
    @Transactional
    @Query("UPDATE Orders O SET O.matchedSize =:matchedSize, O.status=:status where O.id=:id")
    void update(@Param("id") UUID id, @Param("status") OrderStatus status, @Param("matchedSize") long matchedSize);

}

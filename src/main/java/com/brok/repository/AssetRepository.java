package com.brok.repository;

import com.brok.entity.Asset;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Asset a join a.user u where u.username=:username")
    List<Asset> findAllByUsername(@Param("username") String username);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Asset a join a.user u where u.username=:username and a.symbol=:symbol")
    Optional<Asset> getAsset(@Param("symbol") String symbol, @Param("username") String username);
}

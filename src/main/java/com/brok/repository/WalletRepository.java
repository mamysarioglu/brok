package com.brok.repository;

import com.brok.entity.User;
import com.brok.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

   Optional<Wallet> findByUser(User user);
   @Query("SELECT W FROM Wallet W WHERE W.user.username=:username")
   Optional<Wallet> findByUsername(@Param("username") String username);


}

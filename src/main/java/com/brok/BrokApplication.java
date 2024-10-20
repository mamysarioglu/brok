package com.brok;

import com.brok.entity.Asset;
import com.brok.entity.Stock;
import com.brok.entity.User;
import com.brok.entity.Wallet;
import com.brok.repository.AssetRepository;
import com.brok.repository.StockRepository;
import com.brok.repository.UserRepository;
import com.brok.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class BrokApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokApplication.class, args);
    }


    @Order(1)
    @Bean
    @Profile("dev")
    CommandLineRunner commandLineRunnerSTOCK(StockRepository stockRepository) {
        return a -> {
            Stock TL = Stock.builder().name("Türk Lirası").symbol("TL").price(1).build();
            stockRepository.save(TL);
            Stock thy = Stock.builder().name("TURK HAVA YOLLAR").symbol("THY").price(100.5).build();
            stockRepository.save(thy);
            Stock mogan = Stock.builder().name("MOGAN").symbol("MOG").price(11).build();
            stockRepository.save(mogan);
            Stock tofas = Stock.builder().name("TOFAS").symbol("TOF").price(185.1).build();
            stockRepository.save(tofas);
            Stock mia = Stock.builder().name("MIA Teknoloji").symbol("MIA").price(40).build();
            stockRepository.save(mia);
            Stock apple = Stock.builder().name("APPLE").symbol("APP").price(10000).build();
            stockRepository.save(apple);
        };
    }

    @Bean
    @Profile("dev")
    CommandLineRunner commandLineRunnerUSER(UserRepository userRepository, AssetRepository assetRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("Hello World");

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .roles("ROLE_USER").build();
            User MUHAMMED = User.builder()
                    .username("MUHAMMED")
                    .roles("ROLE_USER")
                    .password(passwordEncoder.encode("MUHAMMED"))
                    .build();
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ROLE_ADMIN").build();
            user = userRepository.save(user);

            userRepository.save(admin);

            userRepository.findAll().forEach(System.out::println);

            Wallet wallet = Wallet.builder().amount(new BigDecimal(1000.))
                    .user(MUHAMMED)
                    .build();
            MUHAMMED.setWallet(wallet);
            MUHAMMED = userRepository.save(MUHAMMED);

            Asset asset = Asset.builder()
                    .user(user)
                    .symbol("THY")
                    .size(100)
                    .build();
            assetRepository.save(asset);
        };
    }
}
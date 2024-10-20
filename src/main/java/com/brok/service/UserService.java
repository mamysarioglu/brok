package com.brok.service;

import com.brok.dto.RegisterDTO;
import com.brok.entity.User;
import com.brok.entity.Wallet;
import com.brok.repository.UserRepository;
import com.brok.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;

    public void save(RegisterDTO dto) throws RuntimeException {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode( dto.getPassword()))
                .roles("ROLE_USER")
                .wallet(Wallet.builder().amount(BigDecimal.ZERO).build())
                .build();
        userRepository.save(user);

    }

    public User findByUsername(String username) {
       return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}

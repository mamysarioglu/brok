package com.brok.securty;

import com.brok.entity.User;
import com.brok.entity.UserSecurity;
import com.brok.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrokageUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public BrokageUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserSecurity::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

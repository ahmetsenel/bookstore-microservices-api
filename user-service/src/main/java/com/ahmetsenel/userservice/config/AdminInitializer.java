package com.ahmetsenel.userservice.config;

import com.ahmetsenel.userservice.entity.Role;
import com.ahmetsenel.userservice.entity.User;
import com.ahmetsenel.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ROLE_ADMIN)) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@bookstore.com");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setRole(Role.ROLE_ADMIN);

        userRepository.save(admin);
    }
}
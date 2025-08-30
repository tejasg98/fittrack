package com.appventory.fittrack.config;

import com.appventory.fittrack.model.User;
import com.appventory.fittrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.user.admin.email}") private String adminEmail;
    @Value("${app.user.admin.password}") private String adminPassword;

    @Value("${app.user.john.email}") private String johnEmail;
    @Value("${app.user.john.password}") private String johnPassword;

    @Value("${app.user.jane.email}") private String janeEmail;
    @Value("${app.user.jane.password}") private String janePassword;

    @Value("${app.user.guest.email}") private String guestEmail;
    @Value("${app.user.guest.password}") private String guestPassword;

    @Value("${app.user.extra1.email}") private String extra1Email;
    @Value("${app.user.extra1.password}") private String extra1Password;

    @Value("${app.user.extra2.email}") private String extra2Email;
    @Value("${app.user.extra2.password}") private String extra2Password;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) { 

            User admin = User.builder()
                    .fullName("Rajesh Sharma")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .dateOfBirth(LocalDate.of(1988, 3, 15))
                    .weight(78.0)
                    .height(175.0)
                    .roles(Set.of("ROLE_ADMIN"))
                    .build();

            User john = User.builder()
                    .fullName("Amit Verma")
                    .email(johnEmail)
                    .password(passwordEncoder.encode(johnPassword))
                    .dateOfBirth(LocalDate.of(1992, 5, 20))
                    .weight(72.0)
                    .height(168.0)
                    .roles(Set.of(ROLE_USER))
                    .build();

            User jane = User.builder()
                    .fullName("Sneha Kapoor")
                    .email(janeEmail)
                    .password(passwordEncoder.encode(janePassword))
                    .dateOfBirth(LocalDate.of(1995, 8, 10))
                    .weight(60.0)
                    .height(162.0)
                    .roles(Set.of(ROLE_USER))
                    .build();


            User guest = User.builder()
                    .fullName("Priya Desai")
                    .email(guestEmail)
                    .password(passwordEncoder.encode(guestPassword))
                    .dateOfBirth(LocalDate.of(2000, 7, 25))
                    .weight(58.0)
                    .height(165.0)
                    .roles(Set.of(ROLE_USER))
                    .build();

            User extra1 = User.builder()
                    .fullName("Rohan Mehta")
                    .email(extra1Email)
                    .password(passwordEncoder.encode(extra1Password))
                    .dateOfBirth(LocalDate.of(1994, 9, 18))
                    .weight(70.0)
                    .height(172.0)
                    .roles(Set.of(ROLE_USER))
                    .build();

            User extra2 = User.builder()
                    .fullName("Anjali Reddy")
                    .email(extra2Email)
                    .password(passwordEncoder.encode(extra2Password))
                    .dateOfBirth(LocalDate.of(1991, 4, 12))
                    .weight(63.0)
                    .height(160.0)
                    .roles(Set.of(ROLE_USER))
                    .build();

            // Save all users
            userRepository.save(admin);
            userRepository.save(john);
            userRepository.save(jane);
            userRepository.save(guest);
            userRepository.save(extra1);
            userRepository.save(extra2);
        }
    }
}


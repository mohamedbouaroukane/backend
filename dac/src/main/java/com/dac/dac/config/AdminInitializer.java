package com.dac.dac.config;

import com.dac.dac.constants.UserRole;
import com.dac.dac.entity.Admin;
import com.dac.dac.entity.User;
import com.dac.dac.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Scanner scanner = new Scanner(System.in);


        if (userRepository.findAllByUserRole(UserRole.ADMIN).isEmpty()) {
            System.out.print("Enter admin full name: ");
            String fullName = scanner.nextLine();

            System.out.print("Enter admin email: ");
            String email = scanner.nextLine();

            // Prompt for admin password
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine();

            System.out.print("Enter admin phone: ");
            String phone = scanner.nextLine();
            User adminUser = new Admin();
            adminUser.setEmail(email);
            adminUser.setPassword(passwordEncoder.encode(password));
            adminUser.setFullName(fullName);
            adminUser.setPhone(phone);
            adminUser.setEnabled(true);
            adminUser.setUserRole(UserRole.ADMIN);
            userRepository.save(adminUser);
            System.out.println("Admin user created with email: " + email);
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
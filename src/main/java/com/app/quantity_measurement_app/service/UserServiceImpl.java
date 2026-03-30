package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.model.User;
import com.app.quantity_measurement_app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User saveOrUpdateOAuthUser(String googleId, String email, String name, String givenName,
                                      String familyName, String pictureUrl, String locale, boolean emailVerified) {
        Optional<User> existingUser = userRepository.findByGoogleId(googleId);
        if (existingUser.isEmpty()) {
            existingUser = userRepository.findByEmail(email);
        }

        User user = existingUser.orElseGet(User::new);
        user.setGoogleId(googleId);
        user.setEmail(email);
        user.setName(name);
        user.setGivenName(givenName);
        user.setFamilyName(familyName);
        user.setPictureUrl(pictureUrl);
        user.setLocale(locale);
        user.setEmailVerified(emailVerified);
        user.setAuthProvider(user.getPassword() != null ? User.AuthProvider.HYBRID : User.AuthProvider.GOOGLE);
        user.setEnabled(true);
        user.setLastLoginAt(LocalDateTime.now());

        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }

        User savedUser = userRepository.save(user);
        logger.info("{} OAuth user: {}", existingUser.isPresent() ? "Updated existing" : "Created new", email);
        return savedUser;
    }

    @Override
    @Transactional
    public User registerLocalUser(String email, String password, String name) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getPassword() != null) {
                throw new IllegalArgumentException("User already exists with email: " + email);
            }

            user.setPassword(passwordEncoder.encode(password));
            user.setName(name);
            user.setGivenName(name);
            user.setAuthProvider(User.AuthProvider.HYBRID);
            user.setLastLoginAt(LocalDateTime.now());
            logger.info("Enabled local login for existing OAuth user: {}", email);
            return userRepository.save(user);
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .givenName(name)
                .emailVerified(false)
                .authProvider(User.AuthProvider.LOCAL)
                .enabled(true)
                .lastLoginAt(LocalDateTime.now())
                .build();

        logger.info("Created local user: {}", email);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateLastLogin(User user) {
        user.setLastLoginAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        logger.info("Deleted user with id: {}", id);
    }
}

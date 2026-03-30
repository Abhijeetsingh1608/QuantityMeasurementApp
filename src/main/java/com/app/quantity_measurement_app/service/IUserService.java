package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.model.User;

import java.util.Optional;

public interface IUserService {

    User saveOrUpdateOAuthUser(String googleId, String email, String name, String givenName,
                               String familyName, String pictureUrl, String locale, boolean emailVerified);

    User registerLocalUser(String email, String password, String name);

    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User updateLastLogin(User user);

    void deleteUser(Long id);
}

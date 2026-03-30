package com.app.quantity_measurement_app.security;

import com.app.quantity_measurement_app.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User extends CustomUserPrincipal {

    public CustomOAuth2User(OAuth2User oauth2User, User user) {
        super(user, oauth2User.getAttributes());
    }

    public Long getUserId() {
        return getUser().getId();
    }

    public String getGoogleId() {
        return getUser().getGoogleId();
    }

    public String getPictureUrl() {
        return getUser().getPictureUrl();
    }
}

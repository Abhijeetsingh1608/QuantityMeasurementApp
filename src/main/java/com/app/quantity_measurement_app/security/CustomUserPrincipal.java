package com.app.quantity_measurement_app.security;

import com.app.quantity_measurement_app.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomUserPrincipal implements OAuth2User, UserDetails {

    private final User user;
    private final Map<String, Object> attributes;

    public CustomUserPrincipal(User user) {
        this(user, Map.of());
    }

    public CustomUserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = new HashMap<>(attributes);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public String getName() {
        return user.getName() != null ? user.getName() : user.getEmail();
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return user.getEmail();
    }
}

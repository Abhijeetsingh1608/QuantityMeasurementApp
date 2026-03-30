package com.app.quantity_measurement_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_google_id", columnList = "google_id", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true)
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_id", unique = true)
    private String googleId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "locale")
    private String locale;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "enabled")
    @Builder.Default
    private boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<QuantityMeasurementEntity> measurements = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Role {
        USER,
        ADMIN
    }

    public enum AuthProvider {
        LOCAL,
        GOOGLE,
        HYBRID
    }
}

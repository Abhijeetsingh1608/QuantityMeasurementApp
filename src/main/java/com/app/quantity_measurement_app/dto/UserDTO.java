package com.app.quantity_measurement_app.dto;

import com.app.quantity_measurement_app.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String pictureUrl;
    private String role;
    private String authProvider;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public static UserDTO fromUser(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .givenName(user.getGivenName())
                .familyName(user.getFamilyName())
                .pictureUrl(user.getPictureUrl())
                .role(user.getRole().name())
                .authProvider(user.getAuthProvider().name())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}

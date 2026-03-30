package com.app.quantity_measurement_app.security;

import com.app.quantity_measurement_app.config.AppProperties;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof CustomUserPrincipal customUser) {
            logger.info("OAuth2 authentication successful for user: {}", customUser.getEmail());
            String token = jwtService.generateToken(customUser.getUser());
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            String redirectUri = UriComponentsBuilder
                    .fromUriString(appProperties.getOauth2().getRedirectUri())
                    .queryParam("token", token)
                    .queryParam("provider", customUser.getUser().getAuthProvider().name())
                    .build()
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectUri);
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

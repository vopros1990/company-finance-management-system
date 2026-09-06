package com.example.company_finance_management_system.identity.security.oauth2;

import com.example.company_finance_management_system.identity.api.v1.dto.response.AuthResponse;
import com.example.company_finance_management_system.identity.security.OAuth2AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthenticationService authenticationService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken auth)
            handleOauth2Auth(auth, response);

    }

    private void handleOauth2Auth(OAuth2AuthenticationToken authentication, HttpServletResponse response) throws IOException {

        switch (authentication.getPrincipal()) {

            case OidcUser user when authentication.getPrincipal() instanceof OidcUser:
                handleOidc(user, response);

            default:

        }

    }

    private void handleOidc(OidcUser user, HttpServletResponse response) throws IOException {

        AuthResponse authResponse = authenticationService.authenticateByOidc(user);

        OutputStream out = response.getOutputStream();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setStatus(200);

        objectMapper.writeValue(out, authResponse);

        out.flush();

    }
}

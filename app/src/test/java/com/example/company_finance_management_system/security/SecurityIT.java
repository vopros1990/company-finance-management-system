package com.example.company_finance_management_system.security;

import com.example.company_finance_management_system.common.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityIT extends AbstractPostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void auditLog_withoutAuthentication_then401() throws Exception {

        mockMvc.perform(get("/api/v1/finance/transactions/log"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(
            username = "auditor@mail.ru",
            roles = "AUDITOR"
    )
    public void auditLog_isAuditor_thenOk() throws Exception {

        mockMvc.perform(get("/api/v1/finance/transactions/log"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(
            username = "auditor@mail.ru",
            roles = "ACCOUNTANT"
    )
    public void auditLog_isAccountant_then403() throws Exception {

        mockMvc.perform(get("/api/v1/finance/transactions/log"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(
            username = "auditor@mail.ru",
            roles = "ADMIN"
    )
    public void auditLog_isAdmin_thenOk() throws Exception {

        mockMvc.perform(get("/api/v1/finance/transactions/log"))
                .andExpect(status().isOk());

    }

    @Test
    public void auditLog_isOidc_thenOk() throws Exception {

        mockMvc.perform(
                get("/api/v1/finance/transactions/log")
                        .with(
                                oidcLogin()
                                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        )
                )
                .andExpect(status().isOk());

    }

    @Test
    public void whenAuth_withGoogleOidc_thenOauth2Flow() throws Exception {

        mockMvc.perform(get("/api/v1/auth/oauth2/google"))
                .andExpect(status().is3xxRedirection());

    }

}

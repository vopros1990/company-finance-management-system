package com.example.company_finance_management_system.finance.api.v1.controller;

import com.example.company_finance_management_system.common.AbstractPostgresIntegrationTest;
import com.example.company_finance_management_system.finance.entity.Transaction;
import com.example.company_finance_management_system.finance.entity.TransactionStatus;
import com.example.company_finance_management_system.finance.repository.AccountRepository;
import com.example.company_finance_management_system.finance.repository.CategoryRepository;
import com.example.company_finance_management_system.finance.repository.TransactionRepository;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.repository.DepartmentRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static com.example.company_finance_management_system.common.TestDataFactory.amount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerIT extends AbstractPostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Transactional
    public void whenUpdateConfirmedTransaction_thenReturnsConflictAndKeepsAmount() throws Exception {

        Long departmentId = createDepartment();

        assertTrue(departmentRepository.existsById(departmentId));

        Long categoryId = createCategory();

        assertTrue(categoryRepository.existsById(categoryId));

        Long accountId = createAccount(departmentId);

        assertTrue(categoryRepository.existsById(accountId));

        Long transactionId = createIncomeTransaction("1000", departmentId, categoryId, accountId);

        Transaction draft = transactionRepository.findById(transactionId)
                .orElseThrow();

        assertEquals(TransactionStatus.DRAFT, draft.getStatus());

        assertThat(amount("1000")).isEqualByComparingTo(draft.getAmount());

        confirmTransaction(transactionId);

        transactionRepository.flush();

        Transaction confirmed = transactionRepository.findById(transactionId)
                .orElseThrow();

        assertEquals(TransactionStatus.CONFIRMED, confirmed.getStatus());

        tryUpdateTransactionAmount(transactionId, "500");

        transactionRepository.flush();

        Transaction actual = transactionRepository.findById(transactionId)
                .orElseThrow();

        assertEquals(TransactionStatus.CONFIRMED, actual.getStatus());

        assertThat(amount("1000")).isEqualByComparingTo(actual.getAmount());

    }

    private Long createDepartment() throws Exception {

        MvcResult result = mockMvc.perform(
                        post("/api/v1/departments")
                                .with(asAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "name": "Тестовое подразделение",
                                    "responsibleUserId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Number id = JsonPath.read(content, "$.id");

        return id.longValue();

    }

    private Long createCategory() throws Exception {

        MvcResult result = mockMvc.perform(
                        post("/api/v1/finance/categories")
                                .with(asAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "name": "Тестовая категория",
                                    "parentCategoryId": null
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Number id = JsonPath.read(content, "$.id");

        return id.longValue();

    }

    private Long createAccount(Long departmentId) throws Exception {

        MvcResult result = mockMvc.perform(
                        post("/api/v1/finance/accounts")
                                .with(asAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "type": "BANK_ACCOUNT",
                                    "currency": "RUB",
                                    "departmentId": %d
                                }
                                """.formatted(departmentId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Number id = JsonPath.read(content, "$.id");

        return id.longValue();

    }

    private Long createIncomeTransaction(String amount, Long departmentId, Long categoryId, Long accountId) throws Exception {

        MvcResult result = mockMvc.perform(
                        post("/api/v1/finance/transactions")
                                .with(asAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "type": "INCOME",
                                    "currency": "RUB",
                                    "amount": "%s",
                                    "departmentId": %d,
                                    "categoryId": %d,
                                    "accountTargetId": %d,
                                    "counterpartyId": null
                                }
                                """.formatted(amount, departmentId, categoryId, accountId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Number id = JsonPath.read(content, "$.id");

        return id.longValue();

    }

    private void confirmTransaction(Long id) throws Exception {

        mockMvc.perform(patch("/api/v1/finance/transactions/confirm/" + id)
                .with(asAdmin()))
                .andExpect(status().isNoContent());

    }

    private void tryUpdateTransactionAmount(Long id, String amount) throws Exception {

        mockMvc.perform(patch("/api/v1/finance/transactions/" + id)
                        .with(asAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": "%s"
                                }
                                """.formatted(amount)))
                .andExpect(status().isConflict());

    }

    private RequestPostProcessor asAdmin() {

        User admin = User.builder()
                .id(1L)
                .name("admin")
                .email("admin@test.local")
                .passwordHash("test-password")
                .role(UserRole.ADMIN)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(admin);

        Authentication adminAuthentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        return authentication(adminAuthentication);

    }



}

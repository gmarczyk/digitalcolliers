package com.gmarczyk.taskbackend.commission.interfaces.rest;

import com.gmarczyk.taskbackend.auth.application.AuthenticatedUser;
import com.gmarczyk.taskbackend.commission.application.CommissionCalculationRequestService;
import com.gmarczyk.taskbackend.commission.application.CommissionCalculationService;
import com.gmarczyk.taskbackend.configuration.SecurityBeansConfiguration;
import com.gmarczyk.taskbackend.configuration.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommissionSummaryController.class)
@Import({SecurityBeansConfiguration.class, SecurityConfiguration.class})
class CommissionSummaryControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    CommissionCalculationService commissionCalculationService;
    @MockBean
    CommissionCalculationRequestService commissionCalculationRequestService;
    @MockBean
    UserDetailsService userDetailsService;

    String authenticatedUserName;
    String authenticatedUserPassword;
    String authHeader;

    @BeforeEach
    public void init() {
        authenticatedUserName = "admin";
        authenticatedUserPassword = "admin1";
        when(userDetailsService.loadUserByUsername(authenticatedUserName)).thenReturn(new AuthenticatedUser(authenticatedUserName, passwordEncoder.encode(authenticatedUserPassword)));
        authHeader = "Basic " + Base64.getEncoder().encodeToString((authenticatedUserName + ":" + authenticatedUserPassword).getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void shouldCalculateSummaryForAllWhenNoCustomerProvided() throws Exception {
        givenMultipleSummaryResultsForAllSummaryCalculation();
        var resultActions = whenCallingEndpoint(mvc, "/summary", authHeader);

        then200Returned(resultActions);
        thenSummaryPreparedForAllCustomers();
        thenCommissionCalculationStoredInDbTimes(2);
    }

    @Test
    void shouldCalculateSummaryForAllWhenInvalidCustomerIdString() throws Exception {
        givenMultipleSummaryResultsForAllSummaryCalculation();
        var resultActions = whenCallingEndpoint(mvc, "/summary?customerIds=1,5blabla", authHeader);

        then200Returned(resultActions);
        thenSummaryPreparedForAllCustomers();
        thenCommissionCalculationStoredInDbTimes(2);
    }

    @Test
    void shouldCalculateSummaryForProvidedCustomerIds() throws Exception {
        var summariesForCustomers = List.of(
                givenSummaryResultsForCustomerId(1),
                givenSummaryResultsForCustomerId(2)
        );
        var resultActions = whenCallingEndpoint(mvc, "/summary?customerIds=1,2", authHeader);

        then200Returned(resultActions);
        thenSummaryPreparedForCustomerIds(1, 2);
        thenCommissionCalculationStoredInDbTimes(2);
    }

    private void givenMultipleSummaryResultsForAllSummaryCalculation() {
        var givenFirstSummaryResult = mock(CommissionCalculationService.CustomerCommissionsSummary.class);
        var givenSecondSummaryResult = mock(CommissionCalculationService.CustomerCommissionsSummary.class);
        when(commissionCalculationService.prepareSummaryForAllCustomers()).thenReturn(List.of(givenFirstSummaryResult, givenSecondSummaryResult));
    }

    private CommissionCalculationService.CustomerCommissionsSummary givenSummaryResultsForCustomerId(long id) {
        var result = mock(CommissionCalculationService.CustomerCommissionsSummary.class);
        when(result.getCustomerId()).thenReturn(id);
        when(commissionCalculationService.prepareSummaryForCustomer(id)).thenReturn(Optional.of(result));
        return result;
    }

    private ResultActions whenCallingEndpoint(MockMvc mvc, String x, String authHeader) throws Exception {
        var resultActions = mvc.perform(get(CommissionSummaryController.PATH + x)
                .header(HttpHeaders.AUTHORIZATION, authHeader));
        return resultActions;
    }

    private void then200Returned(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }

    private void thenSummaryPreparedForAllCustomers() {
        verify(commissionCalculationService, times(1)).prepareSummaryForAllCustomers();
    }

    private void thenSummaryPreparedForCustomerIds(long... ids) {
        Arrays.stream(ids).forEach(id -> {
            verify(commissionCalculationService, times(1)).prepareSummaryForCustomer(id);
        });
    }

    private void thenCommissionCalculationStoredInDbTimes(int times) {
        verify(commissionCalculationRequestService, times(times)).storeRequest(any());
    }

}
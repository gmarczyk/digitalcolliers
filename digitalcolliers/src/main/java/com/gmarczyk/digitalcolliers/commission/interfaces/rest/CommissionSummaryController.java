package com.gmarczyk.digitalcolliers.commission.interfaces.rest;

import com.gmarczyk.digitalcolliers.auth.application.AuthenticatedUser;
import com.gmarczyk.digitalcolliers.commission.application.CommissionCalculationRequestService;
import com.gmarczyk.digitalcolliers.commission.application.CommissionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = CommissionSummaryController.PATH)
@RequiredArgsConstructor
public class CommissionSummaryController {

    public static final String PATH = "/v1/commissions";

    private final CommissionCalculationService commissionCalculationService;
    private final CommissionCalculationRequestService commissionCalculationRequestService;

    @GetMapping("/summary")
    public ResponseEntity<Collection<CommissionCalculationService.CustomerCommissionsSummary>> getTransactions(
            final @AuthenticationPrincipal AuthenticatedUser user,
            final @RequestParam("customerIds") Optional<String> possibleCustomerIds) {

        final Set<Long> customerIds = possibleCustomerIds.map(this::extractCustomerIds).orElse(null);
        final List<CommissionCalculationService.CustomerCommissionsSummary> customerCommissionsSummaries;
        if (customerIds == null) {
            customerCommissionsSummaries = prepareSummaryForAllCustomers();
        } else {
            customerCommissionsSummaries = prepareSummaryForCustomerIds(customerIds);
        }

        customerCommissionsSummaries.forEach(summary -> commissionCalculationRequestService.storeRequest(summary));
        return ResponseEntity.ok(customerCommissionsSummaries);
    }

    private Set<Long> extractCustomerIds(String customerIdsCommaSeparated) {
        try {
            return Arrays.stream(customerIdsCommaSeparated.split(","))
                         .map(String::trim)
                         .map(Long::valueOf)
                         .collect(Collectors.toSet());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private List<CommissionCalculationService.CustomerCommissionsSummary> prepareSummaryForAllCustomers() {
        return commissionCalculationService.prepareSummaryForAllCustomers();
    }

    private List<CommissionCalculationService.CustomerCommissionsSummary> prepareSummaryForCustomerIds(Set<Long> customerIds) {
        return customerIds.stream()
                          .map(id -> commissionCalculationService.prepareSummaryForCustomer(id))
                          .flatMap(Optional::stream)
                          .toList();
    }

}

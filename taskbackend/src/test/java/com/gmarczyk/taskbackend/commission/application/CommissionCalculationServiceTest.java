package com.gmarczyk.taskbackend.commission.application;

import com.gmarczyk.taskbackend.fees.FeeWagesRepository;
import com.gmarczyk.taskbackend.fees.domain.FeeWage;
import com.gmarczyk.taskbackend.transactions.application.TransactionsRepository;
import com.gmarczyk.taskbackend.transactions.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CommissionCalculationServiceTest {

    @Mock
    TransactionsRepository transactionsRepository;
    @Mock
    FeeWagesRepository feeWagesRepository;
    @InjectMocks
    CommissionCalculationService cut;

    Long FIRST_CUSTOMER = 1l;
    Long SECOND_CUSTOMER = 2l;

    Instant FIRST_CUSTOMER_LAST_TRANSACTION_DATE = Instant.parse("2030-12-15T10:00:00Z");
    Instant SECOND_CUSTOMER_LAST_TRANSACTION_DATE = Instant.parse("2031-12-15T10:00:00Z");

    List<FeeWage> feeWages = List.of(
            new FeeWage(BigDecimal.valueOf(1000.0), BigDecimal.valueOf(3.5)),
            new FeeWage(BigDecimal.valueOf(1500.0), BigDecimal.valueOf(2.5)),
            new FeeWage(BigDecimal.valueOf(2000.0), BigDecimal.valueOf(1.5))
    );

    List<Transaction> transactions = List.of(
            new Transaction(1l, BigDecimal.valueOf(0.000000), Instant.now(), FIRST_CUSTOMER, "Fname1", "Lname1"),
            new Transaction(2l, BigDecimal.valueOf(999.9990), Instant.now(), FIRST_CUSTOMER, "Fname1", "Lname1"),
            new Transaction(3l, BigDecimal.valueOf(1400.333), Instant.now(), FIRST_CUSTOMER, "Fname1", "Lname1"),
            new Transaction(4l, BigDecimal.valueOf(1600.000), Instant.now(), FIRST_CUSTOMER, "Fname1", "Lname1"),
            new Transaction(5l, BigDecimal.valueOf(2666.666), FIRST_CUSTOMER_LAST_TRANSACTION_DATE, FIRST_CUSTOMER, "Fname1", "Lname1"),
            new Transaction(6l, BigDecimal.valueOf(1000.000), Instant.now(), SECOND_CUSTOMER, "Fname2", "Lname2"),
            new Transaction(7l, BigDecimal.valueOf(1500.000), Instant.now(), SECOND_CUSTOMER, "Fname2", "Lname2"),
            new Transaction(8l, BigDecimal.valueOf(2000.000), SECOND_CUSTOMER_LAST_TRANSACTION_DATE, SECOND_CUSTOMER, "Fname2", "Lname2")
    );

    CommissionCalculationService.CustomerCommissionsSummary FIRST_CUSTOMER_EXPECTED_SUMMARY = new CommissionCalculationService.CustomerCommissionsSummary(
            "Fname1", "Lname1", FIRST_CUSTOMER,
            5,
            BigDecimal.valueOf(6666.998),
            BigDecimal.valueOf(134.00828),
            FIRST_CUSTOMER_LAST_TRANSACTION_DATE
    );

    CommissionCalculationService.CustomerCommissionsSummary SECOND_CUSTOMER_EXPECTED_SUMMARY = new CommissionCalculationService.CustomerCommissionsSummary(
            "Fname2", "Lname2", SECOND_CUSTOMER,
            3,
            BigDecimal.valueOf(4500.0),
            BigDecimal.valueOf(77.5),
            SECOND_CUSTOMER_LAST_TRANSACTION_DATE
    );

    @BeforeEach
    public void initMocks() {
        lenient().when(feeWagesRepository.findAll()).thenReturn(feeWages);
        lenient().when(transactionsRepository.findAll()).thenReturn(transactions);
        lenient().when(transactionsRepository.findAllMappedByCustomerId()).thenReturn(transactions.stream().collect(Collectors.groupingBy(Transaction::getCustomerId)));
        lenient().when(transactionsRepository.findAllForCustomer(any())).thenAnswer(invocation -> {
            Long customerId = invocation.getArgument(0);
            return transactions.stream().filter(t -> t.getCustomerId() == customerId).collect(Collectors.toList());
        });
    }

    @Test
    void shouldCalculateSummariesProperlyWhenMultipleCustomersDataAvailable() {
        var optFirstCustomerSummary = cut.prepareSummaryForCustomer(FIRST_CUSTOMER);
        var optSecondCustomerSummary = cut.prepareSummaryForCustomer(SECOND_CUSTOMER);

        assertTrue(optFirstCustomerSummary.isPresent());
        assertTrue(optSecondCustomerSummary.isPresent());

        assertEquals(FIRST_CUSTOMER_EXPECTED_SUMMARY, optFirstCustomerSummary.get());
        assertEquals(SECOND_CUSTOMER_EXPECTED_SUMMARY, optSecondCustomerSummary.get());
    }

    @Test
    void shouldCalculateSummariesProperlyForAllAvailableCustomers() {
        var allCustomersSummaries = cut.prepareSummaryForAllCustomers();
        assertEquals(2, allCustomersSummaries.size());

        assertEquals(FIRST_CUSTOMER_EXPECTED_SUMMARY, allCustomersSummaries.stream().filter(s -> s.getCustomerId().equals(FIRST_CUSTOMER)).findFirst().get());
        assertEquals(SECOND_CUSTOMER_EXPECTED_SUMMARY, allCustomersSummaries.stream().filter(s -> s.getCustomerId().equals(SECOND_CUSTOMER)).findFirst().get());
    }

}
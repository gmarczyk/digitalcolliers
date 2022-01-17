package com.gmarczyk.digitalcolliers.commission.application;


import com.gmarczyk.digitalcolliers.fees.FeeWagesRepository;
import com.gmarczyk.digitalcolliers.fees.domain.FeeWage;
import com.gmarczyk.digitalcolliers.transactions.application.TransactionsRepository;
import com.gmarczyk.digitalcolliers.transactions.domain.Transaction;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommissionCalculationService {

    private final TransactionsRepository transactionsRepository;
    private final FeeWagesRepository feeWagesRepository;

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class CustomerCommissionsSummary {
        String firstName;
        String lastName;
        Long customerId;

        int transactionsCount;
        BigDecimal totalTransactionsValue;
        BigDecimal totalTransactionsFee;
        Instant lastTransactionDate;
    }

    public List<CustomerCommissionsSummary> prepareSummaryForAllCustomers() {
        return transactionsRepository.findAllMappedByCustomerId().keySet().stream()
                                     .map(customerId -> prepareSummaryForCustomer(customerId))
                                     .flatMap(Optional::stream)
                                     .toList();
    }

    public Optional<CustomerCommissionsSummary> prepareSummaryForCustomer(Long customerId) {
        Collection<Transaction> allTransactionsForCustomer = transactionsRepository.findAllForCustomer(customerId);
        if (allTransactionsForCustomer.isEmpty()) {
            return Optional.empty();
        }

        Transaction firstTransaction = allTransactionsForCustomer.stream().findFirst().get();

        return Optional.of(new CustomerCommissionsSummary(
                firstTransaction.getCustomerFirstName(),
                firstTransaction.getCustomerLastName(),
                customerId,
                getTotalTransactionsCountForCustomer(allTransactionsForCustomer),
                calculateTotalTransactionsValueForCustomer(allTransactionsForCustomer),
                calculateTotalCommissionForCustomer(allTransactionsForCustomer),
                getLastTransactionDateForCustomer(allTransactionsForCustomer)
        ));
    }

    public int getTotalTransactionsCountForCustomer(Collection<Transaction> allTransactionsForCustomer) {
        if (allTransactionsForCustomer.isEmpty()) {
            return 0;
        }

        return allTransactionsForCustomer.size();
    }

    public BigDecimal calculateTotalTransactionsValueForCustomer(Collection<Transaction> allTransactionsForCustomer) {
        if (allTransactionsForCustomer.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return allTransactionsForCustomer.stream()
                                         .map(transaction -> transaction.getTransactionAmount())
                                         .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalCommissionForCustomer(Collection<Transaction> allTransactionsForCustomer) {
        if (allTransactionsForCustomer.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<FeeWage> feeWagesSorted = feeWagesRepository.findAll().stream().sorted(Comparator.comparing(FeeWage::getBoundaryValue)).toList();

        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < feeWagesSorted.size(); i++) {
            final BigDecimal lower;
            final BigDecimal upper;
            if (i == 0) {
                lower = null;
                upper = feeWagesSorted.get(i).getBoundaryValue();
            } else if (i == feeWagesSorted.size() - 1) {
                lower = feeWagesSorted.get(i - 1).getBoundaryValue();
                upper = null;
            } else {
                lower = feeWagesSorted.get(i - 1).getBoundaryValue();
                upper = feeWagesSorted.get(i).getBoundaryValue();
            }

            BigDecimal percentageFee = feeWagesSorted.get(i).getFeePercentageValue().divide(BigDecimal.valueOf(100));
            total = total.add(calculateTotalFeeForTransactionsApplicableToWage(allTransactionsForCustomer, lower, upper, percentageFee));
        }

        return total.stripTrailingZeros();
    }

    private BigDecimal calculateTotalFeeForTransactionsApplicableToWage(Collection<Transaction> transactions, BigDecimal lower, BigDecimal upper, BigDecimal percentageFee) {
        return transactions.stream()
                           .filter(transaction -> {
                               if (lower == null) {
                                   return transaction.getTransactionAmount().compareTo(upper) < 0;
                               } else if (upper == null) {
                                   return transaction.getTransactionAmount().compareTo(lower) >= 0;
                               } else {
                                   return (transaction.getTransactionAmount().compareTo(lower) >= 0) && (transaction.getTransactionAmount().compareTo(upper) < 0);
                               }
                           })
                           .map(transationApplicableToRange -> transationApplicableToRange.getTransactionAmount().multiply(percentageFee))
                           .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Instant getLastTransactionDateForCustomer(Collection<Transaction> allTransactionsForCustomer) {
        if (allTransactionsForCustomer.isEmpty()) {
            return null;
        }

        return allTransactionsForCustomer.stream()
                                         .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                                         .findFirst()
                                         .map(Transaction::getTransactionDate)
                                         .get();
    }

}

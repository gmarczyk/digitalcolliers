package com.gmarczyk.taskbackend.transactions.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Getter
public class Transaction {

    Long transactionId;
    BigDecimal transactionAmount;
    Instant transactionDate;

    Long customerId;
    String customerFirstName;
    String customerLastName;

}

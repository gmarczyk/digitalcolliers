package com.gmarczyk.digitalcolliers.transactions.application;

import com.gmarczyk.digitalcolliers.transactions.domain.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionsRepository {

    Collection<Transaction> findAll();

    Map<Long, List<Transaction>> findAllMappedByCustomerId();

    Collection<Transaction> findAllForCustomer(Long customerId);

}

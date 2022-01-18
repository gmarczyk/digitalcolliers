package com.gmarczyk.taskbackend.transactions.application;

import com.gmarczyk.taskbackend.transactions.domain.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TransactionsRepository {

    Collection<Transaction> findAll();

    Map<Long, List<Transaction>> findAllMappedByCustomerId();

    Collection<Transaction> findAllForCustomer(Long customerId);

}

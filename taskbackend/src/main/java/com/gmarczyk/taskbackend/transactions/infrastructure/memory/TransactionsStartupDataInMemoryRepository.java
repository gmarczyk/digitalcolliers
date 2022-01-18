package com.gmarczyk.taskbackend.transactions.infrastructure.memory;

import com.gmarczyk.taskbackend.shared.StartupDataInMemoryRepository;
import com.gmarczyk.taskbackend.shared.StartupDataLoader;
import com.gmarczyk.taskbackend.transactions.application.TransactionsRepository;
import com.gmarczyk.taskbackend.transactions.domain.Transaction;
import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TransactionsStartupDataInMemoryRepository extends StartupDataInMemoryRepository<Transaction> implements TransactionsRepository {

    private final Map<Long, List<Transaction>> transactionsByCustomerId = new HashMap<>();

    public TransactionsStartupDataInMemoryRepository(Collection<StartupDataLoader<Transaction>> dataLoaders) {
        super(dataLoaders);
    }

    @PostConstruct
    public void load() {
        this.transactionsByCustomerId.putAll(super.getData().stream().collect(Collectors.groupingBy(Transaction::getCustomerId)));
    }

    @Override
    public Collection<Transaction> findAll() {
        return super.getData();
    }

    @Override
    public Map<Long, List<Transaction>> findAllMappedByCustomerId() {
        return this.transactionsByCustomerId;
    }

    @Override
    public Collection<Transaction> findAllForCustomer(Long customerId) {
        return transactionsByCustomerId.containsKey(customerId) ? transactionsByCustomerId.get(customerId) : ListUtils.EMPTY_LIST;
    }

}

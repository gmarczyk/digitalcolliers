package com.gmarczyk.taskbackend.fees.infrastructure.memory;

import com.gmarczyk.taskbackend.fees.FeeWagesRepository;
import com.gmarczyk.taskbackend.fees.domain.FeeWage;
import com.gmarczyk.taskbackend.shared.StartupDataInMemoryRepository;
import com.gmarczyk.taskbackend.shared.StartupDataLoader;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class FeeWagesStartupDataInMemoryRepository extends StartupDataInMemoryRepository<FeeWage> implements FeeWagesRepository {

    public FeeWagesStartupDataInMemoryRepository(Collection<StartupDataLoader<FeeWage>> startupDataLoaders) {
        super(startupDataLoaders);
    }

    @Override
    public Collection<FeeWage> findAll() {
        return super.getData();
    }
}

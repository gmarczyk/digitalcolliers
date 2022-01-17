package com.gmarczyk.digitalcolliers.fees.infrastructure.memory;

import com.gmarczyk.digitalcolliers.fees.FeeWagesRepository;
import com.gmarczyk.digitalcolliers.fees.domain.FeeWage;
import com.gmarczyk.digitalcolliers.shared.StartupDataInMemoryRepository;
import com.gmarczyk.digitalcolliers.shared.StartupDataLoader;
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

package com.gmarczyk.taskbackend.fees;

import com.gmarczyk.taskbackend.fees.domain.FeeWage;

import java.util.Collection;

public interface FeeWagesRepository {

    Collection<FeeWage> findAll();

}

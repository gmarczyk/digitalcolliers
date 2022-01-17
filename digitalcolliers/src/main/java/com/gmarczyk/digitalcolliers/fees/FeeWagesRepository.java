package com.gmarczyk.digitalcolliers.fees;

import com.gmarczyk.digitalcolliers.fees.domain.FeeWage;

import java.util.Collection;
import java.util.Map;

public interface FeeWagesRepository {

    Collection<FeeWage> findAll();

}

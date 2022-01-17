package com.gmarczyk.digitalcolliers.commission.infrastructure.persistence;

import com.gmarczyk.digitalcolliers.commission.domain.ComissionCalculationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionCalculationRequestRepository extends MongoRepository<ComissionCalculationRequest, Long> {

}

package com.gmarczyk.taskbackend.commission.infrastructure.persistence;

import com.gmarczyk.taskbackend.commission.domain.ComissionCalculationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionCalculationRequestRepository extends MongoRepository<ComissionCalculationRequest, Long> {

}

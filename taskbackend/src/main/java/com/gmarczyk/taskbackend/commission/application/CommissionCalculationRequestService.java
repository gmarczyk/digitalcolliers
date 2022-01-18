package com.gmarczyk.taskbackend.commission.application;

import com.gmarczyk.taskbackend.commission.domain.ComissionCalculationRequest;
import com.gmarczyk.taskbackend.commission.infrastructure.persistence.CommissionCalculationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommissionCalculationRequestService {

    private final CommissionCalculationRequestRepository commissionCalculationRequestRepository;

    public void storeRequest(CommissionCalculationService.CustomerCommissionsSummary calculatedSummary) {
        commissionCalculationRequestRepository.insert(
                new ComissionCalculationRequest(
                        calculatedSummary.getCustomerId(),
                        calculatedSummary.getTotalTransactionsFee(),
                        Instant.now()
                )
        );
    }
}

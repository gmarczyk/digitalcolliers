package com.gmarczyk.taskbackend.commission.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComissionCalculationRequest {

    Long customerId;
    BigDecimal calculatedTotalCommission;
    Instant commissionCalculationDate;

}

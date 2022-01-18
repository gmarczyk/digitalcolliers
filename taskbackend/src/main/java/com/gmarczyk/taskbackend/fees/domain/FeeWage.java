package com.gmarczyk.taskbackend.fees.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class FeeWage {

    BigDecimal boundaryValue;
    BigDecimal feePercentageValue;

}

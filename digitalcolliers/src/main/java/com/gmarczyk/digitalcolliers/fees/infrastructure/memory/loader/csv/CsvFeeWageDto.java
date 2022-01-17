package com.gmarczyk.digitalcolliers.fees.infrastructure.memory.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.text.NumberFormat;
import java.util.Locale;

@Data
public class CsvFeeWageDto {

    public static final NumberFormat FEE_WAGE_PERCENTAGE_FORMAT = NumberFormat.getInstance(Locale.FRANCE);

    @CsvBindByName(column = "transaction_value_less_than")
    Long boundaryValue;

    @CsvBindByName(column = "fee_percentage_of_transaction_value")
    String feePercentageValue;


}

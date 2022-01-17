package com.gmarczyk.digitalcolliers.transactions.infrastructure.memory.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
public class CsvTransactionDto {

    public static final NumberFormat TRANSACTION_AMOUNT_FORMAT = NumberFormat.getInstance(Locale.FRANCE);
    public static final DateTimeFormatter TRANSACTION_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.of("UTC"));

    @CsvBindByName(column = "transaction_id")
    Long transactionId;

    @CsvBindByName(column = "transaction_amount")
    String transactionAmount;

    @CsvBindByName(column = "customer_first_name")
    String customerFirstName;

    @CsvBindByName(column = "customer_id")
    Long customerId;

    @CsvBindByName(column = "customer_last_name")
    String customerLastName;

    @CsvBindByName(column = "transaction_date")
    String transactionDate;

}

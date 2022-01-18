package com.gmarczyk.taskbackend.transactions.infrastructure.memory.loader.csv;

import com.gmarczyk.taskbackend.shared.CsvFileParser;
import com.gmarczyk.taskbackend.shared.StartupDataLoader;
import com.gmarczyk.taskbackend.transactions.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CsvTransactionsStartupDataLoader implements StartupDataLoader<Transaction> {

    public static final String TRANSACTIONS_CSV_RESOURCE_PATH = "/data/transactions.csv";

    private final CsvFileParser<CsvTransactionDto> csvParser;

    @Override
    public Collection<Transaction> load() {
        return csvParser.loadCsv(CsvTransactionDto.class, TRANSACTIONS_CSV_RESOURCE_PATH).stream()
                        .map(csvDto -> {
                            try {
                                return toTransaction(csvDto);
                            } catch (ParseException e) {
                                throw new IllegalStateException("Parse exception", e);
                            }
                        }).toList();
    }

    private Transaction toTransaction(CsvTransactionDto csvTransactionDto) throws ParseException {
        return new Transaction(
                csvTransactionDto.getTransactionId(),
                BigDecimal.valueOf(CsvTransactionDto.TRANSACTION_AMOUNT_FORMAT.parse(csvTransactionDto.getTransactionAmount()).doubleValue()),
                CsvTransactionDto.TRANSACTION_DATE_FORMAT.parse(csvTransactionDto.getTransactionDate(), Instant::from),
                csvTransactionDto.getCustomerId(),
                csvTransactionDto.getCustomerFirstName(),
                csvTransactionDto.getCustomerLastName()
        );
    }

}

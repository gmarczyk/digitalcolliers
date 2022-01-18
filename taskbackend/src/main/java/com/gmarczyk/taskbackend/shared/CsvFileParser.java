package com.gmarczyk.taskbackend.shared;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Component
public class CsvFileParser<T> {

    public Collection<T> loadCsv(Class<T> clazz, String resourcePath) {
        InputStreamReader csvReader = initInputStreamReader(resourcePath);
        CsvToBean<T> csvParser = initCsvParser(csvReader, clazz);
        return csvParser.parse();
    }

    private InputStreamReader initInputStreamReader(String resourcePath) {
        InputStream resource = getClass().getResourceAsStream(resourcePath);
        InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8);
        return reader;
    }

    private CsvToBean<T> initCsvParser(InputStreamReader reader, Class clazz) {
        HeaderColumnNameMappingStrategy ms = new HeaderColumnNameMappingStrategy();
        ms.setType(clazz);

        CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withType(clazz)
                .withMappingStrategy(ms)
                .build();

        return csvToBean;
    }

}

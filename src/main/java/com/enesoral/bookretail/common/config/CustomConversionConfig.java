package com.enesoral.bookretail.common.config;

import com.enesoral.bookretail.common.converter.DocumentToYearMonthConverter;
import com.enesoral.bookretail.common.converter.YearMonthToDocumentConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class CustomConversionConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
                new YearMonthToDocumentConverter(),
                new DocumentToYearMonthConverter()
        ));
    }
}

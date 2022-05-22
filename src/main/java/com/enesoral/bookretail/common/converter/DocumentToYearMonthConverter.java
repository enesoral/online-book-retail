package com.enesoral.bookretail.common.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@ReadingConverter
public class DocumentToYearMonthConverter implements Converter<Document, YearMonth> {

    static final String YEAR = "year";
    static final String MONTH = "month";

    @Override
    public YearMonth convert(Document document) {
        final int year = (int) document.get(YEAR);
        final int month = (int) document.get(MONTH);
        return YearMonth.of(year, month);
    }
}

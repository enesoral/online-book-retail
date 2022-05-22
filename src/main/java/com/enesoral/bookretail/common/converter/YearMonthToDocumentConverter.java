package com.enesoral.bookretail.common.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@WritingConverter
public class YearMonthToDocumentConverter implements Converter<YearMonth, Document> {

    static final String YEAR = "year";
    static final String MONTH = "month";

    @Override
    public Document convert(YearMonth yearMonth) {
        Document document = new Document();
        document.put(YEAR, yearMonth.getYear());
        document.put(MONTH, yearMonth.getMonth().getValue());
        return document;
    }
}

package ai.wapl.noteapi.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;

@Converter
public class DateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        return NoteUtil.dateToString(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return NoteUtil.stringToDate(dbData);
    }

}

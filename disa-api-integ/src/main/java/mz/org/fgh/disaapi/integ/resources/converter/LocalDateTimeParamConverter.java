package mz.org.fgh.disaapi.integ.resources.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.ws.rs.ext.ParamConverter;

public class LocalDateTimeParamConverter implements ParamConverter<LocalDateTime> {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime fromString(String value) {
        if (value == null)
            return null;
        return LocalDateTime.parse(value, formatter);
    }

    @Override
    public String toString(LocalDateTime value) {
        if (value == null)
            return null;
        return value.format(formatter);
    }
}

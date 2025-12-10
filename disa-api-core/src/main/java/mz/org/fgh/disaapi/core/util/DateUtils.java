package mz.org.fgh.disaapi.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converte string para LocalDateTime, aceitando formatos yyyy-MM-dd ou yyyy-MM-dd HH:mm:ss
     * 
     * @param dateStr String da data a ser convertida
     * @param isStartDate true se é data início (usa 00:00:00), false se é data fim (usa 23:59:59)
     * @return LocalDateTime convertido
     * @throws DateTimeParseException se o formato da data for inválido
     */
    public static LocalDateTime parseDateTime(String dateStr, boolean isStartDate) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Data não pode ser null ou vazia");
        }

        String trimmedDate = dateStr.trim();

        try {
            // Tentar primeiro com formato completo (yyyy-MM-dd HH:mm:ss)
            if (trimmedDate.length() > 10 && trimmedDate.contains(":")) {
                return LocalDateTime.parse(trimmedDate, DATETIME_FORMATTER);
            }
            
            // Se for apenas data (yyyy-MM-dd), adicionar hora apropriada
            if (trimmedDate.length() == 10) {
                return parseAsDateOnly(trimmedDate, isStartDate);
            }
            
            throw new DateTimeParseException("Formato não suportado", trimmedDate, 0);
            
        } catch (DateTimeParseException e) {
            // Tentar novamente como formato de data apenas
            try {
                return parseAsDateOnly(trimmedDate, isStartDate);
            } catch (Exception ex) {
                throw new DateTimeParseException(
                    "Formato de data inválido: " + trimmedDate + ". Use yyyy-MM-dd ou yyyy-MM-dd HH:mm:ss", 
                    trimmedDate, 
                    0
                );
            }
        }
    }

    /**
     * Converte string apenas com data (yyyy-MM-dd) para LocalDateTime
     * 
     * @param dateStr String no formato yyyy-MM-dd
     * @param isStartDate true para início do dia (00:00:00), false para fim do dia (23:59:59)
     * @return LocalDateTime convertido
     */
    private static LocalDateTime parseAsDateOnly(String dateStr, boolean isStartDate) {
        if (isStartDate) {
            return LocalDateTime.parse(dateStr + " 00:00:00", DATETIME_FORMATTER);
        } else {
            return LocalDateTime.parse(dateStr + " 23:59:59", DATETIME_FORMATTER);
        }
    }
}

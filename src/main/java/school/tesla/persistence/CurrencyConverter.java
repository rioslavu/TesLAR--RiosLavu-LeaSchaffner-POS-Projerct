package school.tesla.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.tesla.value.Currency;

@Converter
public class CurrencyConverter implements AttributeConverter<Currency, String> {
    @Override
    public String convertToDatabaseColumn(Currency value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case EUR -> "EUR";
            case USD -> "USD";
            case CHF -> "CHF";
        };
    }

    @Override
    public Currency convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case "EUR" -> Currency.EUR;
            case "USD" -> Currency.USD;
            case "CHF" -> Currency.CHF;
            default -> throw new DataQualityException(Currency.class, code);
        };
    }
}

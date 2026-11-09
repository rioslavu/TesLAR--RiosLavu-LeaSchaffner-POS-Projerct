package school.tesla.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.tesla.charging.ConnectorType;

@Converter
public class ConnectorTypeConverter implements AttributeConverter<ConnectorType, String> {
    @Override
    public String convertToDatabaseColumn(ConnectorType value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case CCS2 -> "CCS2";
            case NACS -> "NACS";
            case TYPE2 -> "TYPE2";
        };
    }

    @Override
    public ConnectorType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case "CCS2" -> ConnectorType.CCS2;
            case "NACS" -> ConnectorType.NACS;
            case "TYPE2" -> ConnectorType.TYPE2;
            default -> throw new DataQualityException(ConnectorType.class, code);
        };
    }
}

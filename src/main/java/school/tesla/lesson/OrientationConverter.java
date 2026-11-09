package school.tesla.lesson;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.tesla.persistence.DataQualityException;

@Converter
public class OrientationConverter implements AttributeConverter<Orientation, String> {
    @Override
    public String convertToDatabaseColumn(Orientation value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case PORTRAIT -> "P";
            case LANDSCAPE -> "L";
            case SQUARE -> "S";
        };
    }

    @Override
    public Orientation convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case "P" -> Orientation.PORTRAIT;
            case "L" -> Orientation.LANDSCAPE;
            case "S" -> Orientation.SQUARE;
            default -> throw new DataQualityException(Orientation.class, code);
        };
    }
}

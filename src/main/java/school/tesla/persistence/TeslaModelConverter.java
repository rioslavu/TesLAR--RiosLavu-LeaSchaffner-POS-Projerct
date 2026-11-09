package school.tesla.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.tesla.vehicle.TeslaModel;

@Converter
public class TeslaModelConverter implements AttributeConverter<TeslaModel, String> {
    @Override
    public String convertToDatabaseColumn(TeslaModel value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case MODEL_S -> "S";
            case MODEL_L -> "L";
            case MODEL_R -> "R";
            case MODEL_Lpromax -> "LP";
        };
    }

    @Override
    public TeslaModel convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case "S" -> TeslaModel.MODEL_S;
            case "L" -> TeslaModel.MODEL_L;
            case "R" -> TeslaModel.MODEL_R;
            case "LP" -> TeslaModel.MODEL_Lpromax;
            default -> throw new DataQualityException(TeslaModel.class, code);
        };
    }
}

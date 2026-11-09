package school.tesla.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.tesla.vehicle.BatteryChemistry;

@Converter
public class BatteryChemistryConverter implements AttributeConverter<BatteryChemistry, String> {
    @Override
    public String convertToDatabaseColumn(BatteryChemistry value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case NCA -> "NCA";
            case NMC -> "NMC";
            case LFP -> "LFP";
        };
    }

    @Override
    public BatteryChemistry convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case "NCA" -> BatteryChemistry.NCA;
            case "NMC" -> BatteryChemistry.NMC;
            case "LFP" -> BatteryChemistry.LFP;
            default -> throw new DataQualityException(BatteryChemistry.class, code);
        };
    }
}

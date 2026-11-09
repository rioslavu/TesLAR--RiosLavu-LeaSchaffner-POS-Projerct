package school.tesla.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.tesla.service.ServiceType;

@Converter
public class ServiceTypeConverter implements AttributeConverter<ServiceType, String> {
    @Override
    public String convertToDatabaseColumn(ServiceType value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case INSPECTION -> "INSP";
            case TIRE_CHANGE -> "TIRE";
            case BRAKE_SERVICE -> "BRAKE";
            case BATTERY_CHECK -> "BATT";
            case SOFTWARE_UPDATE -> "SW";
            case REPAIR -> "REPAIR";
        };
    }

    @Override
    public ServiceType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case "INSP" -> ServiceType.INSPECTION;
            case "TIRE" -> ServiceType.TIRE_CHANGE;
            case "BRAKE" -> ServiceType.BRAKE_SERVICE;
            case "BATT" -> ServiceType.BATTERY_CHECK;
            case "SW" -> ServiceType.SOFTWARE_UPDATE;
            case "REPAIR" -> ServiceType.REPAIR;
            default -> throw new DataQualityException(ServiceType.class, code);
        };
    }
}

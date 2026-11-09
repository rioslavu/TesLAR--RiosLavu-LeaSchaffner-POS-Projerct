package school.tesla.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.tesla.service.AppointmentStatus;

@Converter
public class AppointmentStatusConverter implements AttributeConverter<AppointmentStatus, String> {
    @Override
    public String convertToDatabaseColumn(AppointmentStatus value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case REQUESTED -> "REQ";
            case CONFIRMED -> "CONF";
            case IN_PROGRESS -> "PROG";
            case COMPLETED -> "DONE";
            case CANCELLED -> "CANC";
        };
    }

    @Override
    public AppointmentStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case "REQ" -> AppointmentStatus.REQUESTED;
            case "CONF" -> AppointmentStatus.CONFIRMED;
            case "PROG" -> AppointmentStatus.IN_PROGRESS;
            case "DONE" -> AppointmentStatus.COMPLETED;
            case "CANC" -> AppointmentStatus.CANCELLED;
            default -> throw new DataQualityException(AppointmentStatus.class, code);
        };
    }
}

package school.tesla.persistence;

public class DataQualityException extends RuntimeException {
    public DataQualityException(Class<? extends Enum<?>> enumType, Object value) {
        super("Unknown value '%s' for enum of type %s".formatted(value, enumType.getSimpleName()));
    }
}

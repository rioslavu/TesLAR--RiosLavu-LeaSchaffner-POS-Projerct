package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Vin(@Column(name = "vin", nullable = false, length = 17) String value) {
    private static final Pattern FORMAT = Pattern.compile("[A-HJ-NPR-Z0-9]{17}");

    public Vin {
        ValueChecks.matches("vin", value, FORMAT);
    }
}

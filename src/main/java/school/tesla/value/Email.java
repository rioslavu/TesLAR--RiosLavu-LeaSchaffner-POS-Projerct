package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Email(@Column(name = "email", nullable = false, length = 128) String value) {
    private static final Pattern FORMAT = Pattern.compile("[^@\\s]+@[^@\\s.]+(?:\\.[^@\\s.]+)+");

    public Email {
        ValueChecks.length("email", value, 128);
        ValueChecks.matches("email", value, FORMAT);
    }
}

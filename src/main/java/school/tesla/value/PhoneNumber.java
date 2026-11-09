package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record PhoneNumber(@Column(name = "phone", length = 16) String value) {
    private static final Pattern FORMAT = Pattern.compile("\\+[1-9]\\d{1,14}");

    public PhoneNumber {
        ValueChecks.matches("phone", value, FORMAT);
    }
}

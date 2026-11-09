package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record StallNo(@Column(name = "stall_no", nullable = false, length = 3) String value) {
    private static final Pattern FORMAT = Pattern.compile("\\d{1,2}[A-D]");

    public StallNo {
        ValueChecks.matches("stall_no", value, FORMAT);
    }
}

package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record SwVersion(@Column(name = "software_version", length = 255) String value) {
    private static final Pattern FORMAT = Pattern.compile("\\d{4}\\.(?:0[1-9]|[1-4]\\d|5[0-3])\\.\\d+");

    public SwVersion {
        ValueChecks.length("software_version", value, 255);
        ValueChecks.matches("software_version", value, FORMAT);
    }
}

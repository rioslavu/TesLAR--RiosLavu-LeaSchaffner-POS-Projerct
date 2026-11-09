package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record CenterName(@Column(name = "name", nullable = false, length = 64) String value) {
    public CenterName {
        ValueChecks.length("centerName", value, 64);
    }
}

package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record LastName(@Column(name = "last_name", nullable = false, length = 64) String value) {
    public LastName {
        ValueChecks.length("lastName", value, 64);
    }
}

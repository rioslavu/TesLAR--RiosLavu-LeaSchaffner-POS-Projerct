package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record FirstName(@Column(name = "first_name", nullable = false, length = 64) String value) {
    public FirstName {
        ValueChecks.length("firstName", value, 64);
    }
}

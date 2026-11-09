package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Mileage(@Column(name = "mileage", nullable = false) int value) {
    public Mileage {
        ValueChecks.range("mileage", value, 0, Integer.MAX_VALUE);
    }
}

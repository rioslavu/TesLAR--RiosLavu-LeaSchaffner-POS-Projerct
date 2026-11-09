package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record PowerKw(@Column(name = "max_power_kw", nullable = false) int value) {
    public PowerKw {
        ValueChecks.range("max_power_kw", value, 1, 500);
    }
}

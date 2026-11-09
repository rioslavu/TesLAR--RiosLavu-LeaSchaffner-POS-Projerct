package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Longitude(@Column(name = "longitude", nullable = false) double value) {
    public Longitude {
        ValueChecks.range("longitude", value, -180, 180);
    }
}

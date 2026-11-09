package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Latitude(@Column(name = "latitude", nullable = false) double value) {
    public Latitude {
        ValueChecks.range("latitude", value, -90, 90);
    }
}

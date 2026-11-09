package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record StationName(@Column(name = "name", nullable = false, length = 64) String value) {
    public StationName {
        ValueChecks.length("stationName", value, 64);
    }
}

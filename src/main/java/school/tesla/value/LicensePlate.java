package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record LicensePlate(@Column(name = "license_plate", length = 12) String value) {
    public LicensePlate {
        ValueChecks.length("licensePlate", value, 12);
    }
}

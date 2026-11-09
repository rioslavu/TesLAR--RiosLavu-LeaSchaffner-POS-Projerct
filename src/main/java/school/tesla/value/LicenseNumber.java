package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record LicenseNumber(@Column(name = "drivers_license", nullable = false, length = 32) String value) {
    public LicenseNumber {
        ValueChecks.length("licenseNumber", value, 32);
    }
}

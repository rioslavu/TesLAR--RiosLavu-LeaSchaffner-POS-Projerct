package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EnergyKwh(@Column(name = "energy_kwh", nullable = false) double value) {
    public EnergyKwh {
        ValueChecks.range("energy_kwh", value, 0, Double.MAX_VALUE);
    }
}

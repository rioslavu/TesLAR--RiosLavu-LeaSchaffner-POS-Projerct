package school.tesla.vehicle;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import school.tesla.persistence.BatteryChemistryConverter;
import school.tesla.value.EnergyKwh;
import school.tesla.value.Percentage;

import java.util.Objects;

@Embeddable
public record BatteryPack(
        @Convert(converter = BatteryChemistryConverter.class)
        @Column(name = "chemistry_code", nullable = false, length = 3) BatteryChemistry chemistry,
        @Embedded
        @AttributeOverride(name = "value", column = @Column(name = "capacity_kwh", nullable = false))
        EnergyKwh capacity,
        @Embedded Percentage stateOfHealth
) {
    public BatteryPack {
        Objects.requireNonNull(chemistry, "chemistry");
        Objects.requireNonNull(capacity, "capacity");
        Objects.requireNonNull(stateOfHealth, "stateOfHealth");
    }
}

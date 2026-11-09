package school.tesla.charging;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import school.tesla.persistence.ConnectorTypeConverter;
import school.tesla.value.PowerKw;
import school.tesla.value.StallNo;

import java.util.Objects;

@Embeddable
public record ChargingStall(
        @Embedded StallNo stallNo,
        @Convert(converter = ConnectorTypeConverter.class)
        @Column(name = "connector_code", nullable = false, length = 5) ConnectorType connector,
        @Embedded PowerKw maxPower
) {
    public ChargingStall {
        Objects.requireNonNull(stallNo, "stallNo");
        Objects.requireNonNull(connector, "connector");
        Objects.requireNonNull(maxPower, "maxPower");
    }
}

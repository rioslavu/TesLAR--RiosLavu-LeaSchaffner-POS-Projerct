package school.tesla.value;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.util.Objects;

@Embeddable
public record GeoCoordinate(@Embedded Latitude latitude, @Embedded Longitude longitude) {
    public GeoCoordinate {
        Objects.requireNonNull(latitude, "latitude");
        Objects.requireNonNull(longitude, "longitude");
    }
}

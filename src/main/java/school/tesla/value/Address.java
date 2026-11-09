package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.util.Objects;

@Embeddable
public record Address(
        @Column(name = "street_name_number", nullable = false, length = 128) String streetNameNumber,
        @Column(name = "zip_code", nullable = false, length = 10) String zipCode,
        @Column(name = "city", nullable = false, length = 64) String city,
        @Embedded CountryCode country
) {
    public Address {
        ValueChecks.length("streetNameNumber", streetNameNumber, 128);
        ValueChecks.length("zipCode", zipCode, 10);
        ValueChecks.length("city", city, 64);
        Objects.requireNonNull(country, "country");
    }
}

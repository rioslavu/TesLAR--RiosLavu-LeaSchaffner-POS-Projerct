package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Embeddable
public record CountryCode(@Column(name = "country", nullable = false, length = 2) String value) {
    private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

    public CountryCode {
        Objects.requireNonNull(value, "country");
        if (!ISO_COUNTRIES.contains(value)) {
            throw new IllegalArgumentException("country must be an uppercase ISO 3166-1 alpha-2 code");
        }
    }
}

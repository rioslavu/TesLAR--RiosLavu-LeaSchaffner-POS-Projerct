package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import school.tesla.persistence.CurrencyConverter;

import java.util.Objects;

@Embeddable
public record Money(
        @Embedded Amount amount,
        @Convert(converter = CurrencyConverter.class)
        @Column(name = "currency", nullable = false, length = 3) Currency currency
) {
    public Money {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
    }
}

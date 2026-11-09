package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
public record Amount(@Column(name = "amount", nullable = false, precision = 19, scale = 4) BigDecimal value) {
    public Amount {
        Objects.requireNonNull(value, "amount");
        if (value.signum() < 0) {
            throw new IllegalArgumentException("amount must be nonnegative");
        }
        try {
            value = value.setScale(4, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("amount supports up to four decimal places", e);
        }
        if (value.precision() > 19) {
            throw new IllegalArgumentException("amount supports up to 15 integer digits");
        }
    }
}

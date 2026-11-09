package school.tesla.service;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import school.tesla.persistence.ServiceTypeConverter;
import school.tesla.value.Description;
import school.tesla.value.Money;

import java.time.Duration;
import java.util.Objects;

@Embeddable
public record ServiceTask(
        @Convert(converter = ServiceTypeConverter.class)
        @Column(name = "type_code", nullable = false, length = 6) ServiceType type,
        @Embedded Description description,
        @Column(name = "labor_time") Duration laborTime,
        @Embedded
        @AttributeOverride(name = "amount.value",
                column = @Column(name = "price_amount", nullable = false, precision = 19, scale = 4))
        @AttributeOverride(name = "currency", column = @Column(name = "price_currency", nullable = false, length = 3))
        Money price
) {
    public ServiceTask {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(price, "price");
    }
}

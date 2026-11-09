package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Percentage(@Column(name = "state_of_health", nullable = false) int value) {
    public Percentage {
        ValueChecks.range("state_of_health", value, 0, 100);
    }
}

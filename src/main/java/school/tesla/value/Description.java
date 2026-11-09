package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Description(@Column(name = "description", length = 512) String value) {
    public Description {
        ValueChecks.length("description", value, 512);
    }
}

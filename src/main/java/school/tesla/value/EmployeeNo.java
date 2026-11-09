package school.tesla.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EmployeeNo(@Column(name = "employee_no", nullable = false, length = 8) String value) {
    public EmployeeNo {
        ValueChecks.length("employeeNo", value, 8);
    }
}

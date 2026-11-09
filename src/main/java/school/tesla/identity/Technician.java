package school.tesla.identity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import school.tesla.persistence.TeslaModelConverter;
import school.tesla.service.ServiceCenter;
import school.tesla.value.Email;
import school.tesla.value.EmployeeNo;
import school.tesla.value.FirstName;
import school.tesla.value.LastName;
import school.tesla.vehicle.TeslaModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "technicians")
public class Technician extends User {
    @Embedded
    private EmployeeNo employeeNo;

    @ElementCollection
    @CollectionTable(name = "technician_certifications", joinColumns = @JoinColumn(name = "technician_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"technician_id", "model_code"}))
    @Convert(converter = TeslaModelConverter.class)
    @Column(name = "model_code", nullable = false, length = 2)
    private Set<TeslaModel> certifiedFor = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "works_at_id", nullable = false)
    private ServiceCenter worksAt;

    protected Technician() {
    }

    public Technician(
            FirstName firstName,
            LastName lastName,
            Email email,
            EmployeeNo employeeNo,
            Set<TeslaModel> certifiedFor,
            ServiceCenter worksAt
    ) {
        super(firstName, lastName, email);
        this.employeeNo = Objects.requireNonNull(employeeNo, "employeeNo");
        this.certifiedFor = new HashSet<>(Set.copyOf(certifiedFor));
        this.worksAt = Objects.requireNonNull(worksAt, "worksAt");
    }

    public EmployeeNo getEmployeeNo() {
        return employeeNo;
    }

    public Set<TeslaModel> getCertifiedFor() {
        return Set.copyOf(certifiedFor);
    }

    public ServiceCenter getWorksAt() {
        return worksAt;
    }
}

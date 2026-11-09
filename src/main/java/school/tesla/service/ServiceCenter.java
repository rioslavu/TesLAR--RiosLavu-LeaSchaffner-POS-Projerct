package school.tesla.service;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import school.tesla.value.Address;
import school.tesla.value.CenterName;

import java.util.Objects;

@Entity
@Table(name = "service_centers")
public class ServiceCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CenterName name;

    @Embedded
    private Address address;

    protected ServiceCenter() {
    }

    public ServiceCenter(CenterName name, Address address) {
        this.name = Objects.requireNonNull(name, "name");
        this.address = Objects.requireNonNull(address, "address");
    }

    public Long getId() {
        return id;
    }

    public CenterName getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}

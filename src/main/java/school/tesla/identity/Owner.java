package school.tesla.identity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import school.tesla.value.Address;
import school.tesla.value.Email;
import school.tesla.value.FirstName;
import school.tesla.value.LastName;
import school.tesla.value.LicenseNumber;
import school.tesla.value.PhoneNumber;

import java.util.Objects;

@Entity
@Table(name = "owners")
public class Owner extends User {
    @Embedded
    private LicenseNumber driversLicense;

    @Embedded
    private PhoneNumber phone;

    @Embedded
    private Address homeAddress;

    protected Owner() {
    }

    public Owner(
            FirstName firstName,
            LastName lastName,
            Email email,
            LicenseNumber driversLicense,
            PhoneNumber phone,
            Address homeAddress
    ) {
        super(firstName, lastName, email);
        this.driversLicense = Objects.requireNonNull(driversLicense, "driversLicense");
        this.phone = phone;
        this.homeAddress = Objects.requireNonNull(homeAddress, "homeAddress");
    }

    public LicenseNumber getDriversLicense() {
        return driversLicense;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }
}
